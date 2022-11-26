package com.example.aplikasi_absen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.ActivityAttendanceBinding
import com.example.aplikasi_absen.model.AttendanceResponse
import com.example.aplikasi_absen.model.CheckedOut
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.*

class AttendanceActivity : AppCompatActivity(R.layout.activity_attendance) {

    val binding: ActivityAttendanceBinding by viewBinding()
    var attendanceCallback: (() -> Unit)? = null

    override fun onBackPressed() {
        attendanceCallback?.invoke()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CheckIn().onDismissCallback = {
            recreate()
        }


        binding.swipeContainer.setOnRefreshListener {
            Toast.makeText(this@AttendanceActivity, "Refreshed!", Toast.LENGTH_SHORT).show()
            recreate()
        }

        binding.avAttendImgBtnBack.setOnClickListener {
            attendanceCallback?.invoke()
            super.onBackPressed()
            finish()
        }



        val idLoggedin = SharedPreferences(this@AttendanceActivity).isLoggedin.toString()



        lifecycleScope.launch {
            val responseSelf = SupabaseApi.create().selflistAttendance("*","eq.${idLoggedin}", "eq.now()")
            Log.d("check_out}", "${responseSelf}")
            if (responseSelf.isEmpty()) {
                binding.backgroundIndicator.setBackgroundResource(R.color.mainOrange)
                binding.avAttendImgIndicatorCheck.setImageResource(R.drawable.ic_baseline_meeting_room_24)
                binding.avAttendanceImgBtnCheckIn.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.mainOrange),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.avAttendanceImgBtnCheckOut.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                window.setStatusBarColor(ContextCompat.getColor(this@AttendanceActivity,R.color.mainOrange))
                binding.avAttendTvIndicatorCheck.text = "You're not Attended"
            } else

            if (responseSelf[0].attendStatus == "checked_out") {
                binding.backgroundIndicator.setBackgroundResource(R.color.defautRed)
                binding.avAttendImgIndicatorCheck.setImageResource(R.drawable.ic_baseline_no_meeting_room_24)
                binding.avAttendanceImgBtnCheckIn.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.avAttendanceImgBtnCheckOut.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                window.setStatusBarColor(ContextCompat.getColor(this@AttendanceActivity,R.color.defautRed))
                binding.avAttendTvIndicatorCheck.text = "You're Checked Out"

            } else if(responseSelf[0].absenceReason.isNotEmpty()) {
                binding.backgroundIndicator.setBackgroundResource(R.color.defautRed)
                binding.avAttendImgIndicatorCheck.setImageResource(R.drawable.ic_baseline_person_off_24)
                binding.avAttendanceImgBtnCheckIn.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.avAttendanceImgBtnCheckOut.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                window.setStatusBarColor(ContextCompat.getColor(this@AttendanceActivity,R.color.defautRed))
                binding.avAttendTvIndicatorCheck.text = "You're Absent Today"

            } else {
                binding.backgroundIndicator.setBackgroundResource(R.color.defautlGreen)
                binding.avAttendImgIndicatorCheck.setImageResource(R.drawable.ic_baseline_person_24)
                binding.avAttendanceImgBtnCheckIn.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.gray),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.avAttendanceImgBtnCheckOut.setColorFilter(ContextCompat
                    .getColor(this@AttendanceActivity, R.color.defautlGreen),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
                window.setStatusBarColor(ContextCompat.getColor(this@AttendanceActivity,R.color.defautlGreen))
                binding.avAttendTvIndicatorCheck.text = "You're Attended Today"
            }
        }


        val recyclerview = binding.avAttendRecyclerviewAttendance

        fun setAdapter(attendance: AttendanceResponse) {
            Log.d("CEKDATA", "${attendance.size}")
            val adapter = AdapterAttendance(attendance)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(this@AttendanceActivity)
            CheckIn().onDismissCallback = {
                adapter.notifyDataSetChanged()
            }

            adapter.itemClickListener = {
                val fragmentDetail = FragmentDetailAbsen().apply {
                    arguments = bundleOf(
                        FragmentDetailAbsen.EXTRA_CHECKEDOUT to it[0].checkedOut,
                        FragmentDetailAbsen.EXTRA_CHECKIN to it[0].attendedAt,
                        FragmentDetailAbsen.EXTRA_NAMEKARYAWAN to it[0].nameKaryawan,
                        FragmentDetailAbsen.EXTRA_ID to it[0].id,
                        FragmentDetailAbsen.EXTRA_ABSENCEREASON to it[0].absenceReason,
                        FragmentDetailAbsen.EXTRA_ATTENDSTATUS to it[0].attendStatus,
                        FragmentDetailAbsen.EXTRA_DATE to it[0].attendDate

                    )
                }



                fragmentDetail.show(supportFragmentManager, "details")
            }


        }

        lifecycleScope.launch {
           try {
               binding.progressbarAttendance.isVisible = true
               binding.indicatorRv.isGone = true
               val apiList = SupabaseApi.create().listAttendance("*","eq.now()")
               if(apiList.isEmpty()) {
                   binding.indicatorRv.isVisible = true
               } else {
                   setAdapter(apiList)
               }
               binding.progressbarAttendance.isGone = true
               binding.swipeContainer.isRefreshing = false
           }catch (e:Exception) {
               binding.indicatorRv.isVisible = true
               binding.indicatorRv.text = "No Internet Connection"
               binding.swipeContainer.isRefreshing = false
           }

        }

        binding.avAttendanceImgBtnCheckOut.setOnClickListener {
            lifecycleScope.launch {
                val response = SupabaseApi.create().selflistAttendance("*","eq.${idLoggedin}", "eq.now()")
                if(response.isEmpty()) {
                    Toast.makeText(this@AttendanceActivity, "Anda Belum Absen", Toast.LENGTH_SHORT).show()
                } else if(response[0].attendStatus == "checked_out") {
                    Toast.makeText(this@AttendanceActivity, "You're Checked Out Today", Toast.LENGTH_SHORT).show()
                } else if(response[0].absenceReason.isNotEmpty()) {
                    Toast.makeText(this@AttendanceActivity, "You are Absent", Toast.LENGTH_SHORT).show()
                } else {
                    MaterialAlertDialogBuilder(this@AttendanceActivity)
                        .setTitle("Check-Out Now?")
                        .setMessage("Are you sure want to check out now?")
                        .setNegativeButton("No") {_,_ ->

                        }
                        .setPositiveButton("Yes") {_,_ ->
                            try {
                                lifecycleScope.launch {
                                    binding.swipeContainer.isRefreshing = true
                                    val idAttend = SharedPreferences(this@AttendanceActivity).isAttended
                                    val checkout = CheckedOut(
                                        "checked_out",
                                        "now()"
                                    )
                                    val apiCheckout = SupabaseApi.create().checkoutAttend("eq.${idAttend}", checkout)
                                    binding.swipeContainer.isRefreshing = false
                                    Toast.makeText(this@AttendanceActivity, "You're Checked Out!", Toast.LENGTH_SHORT).show()
                                    recreate()



                                }
                            }catch (e: Exception) {
                                Toast.makeText(this@AttendanceActivity, "Failed to Check-Out", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@AttendanceActivity, DashboardActivity::class.java))
                            }
                        }

                        .create()
                        .show()
                }
            }


        }

        binding.avAttendanceImgBtnCheckIn.setOnClickListener {
            try {
                lifecycleScope.launch {
                    val response = SupabaseApi.create().selflistAttendance("*","eq.${idLoggedin}", "eq.now()")
                    if (response.isNotEmpty()){
                        Toast.makeText(this@AttendanceActivity, "You're Attended Today", Toast.LENGTH_SHORT).show()
                    } else if (response.isEmpty()) {
                        val checkinFragment = CheckIn().apply {  }
                        checkinFragment.show(supportFragmentManager, "checkin")
                    } else if (response[0].absenceReason.isNotEmpty()) {
                        android.widget.Toast.makeText(this@AttendanceActivity, "You're Absent", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@AttendanceActivity, "Failed to Check-In", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AttendanceActivity, DashboardActivity::class.java))
            }

        }


        }



}
