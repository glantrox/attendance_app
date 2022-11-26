package com.example.aplikasi_absen

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.ActivityDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class DashboardActivity : AppCompatActivity() {


    private var timeAttended = Calendar.getInstance()
    private val binding: ActivityDashboardBinding by viewBinding()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        AttendanceActivity().attendanceCallback = {
            Toast.makeText(this@DashboardActivity, "anjing", Toast.LENGTH_SHORT).show()
        }


        binding.swipeContainer.setOnRefreshListener {
            Toast.makeText(this@DashboardActivity, "Refreshed!", Toast.LENGTH_SHORT).show()
            recreate()

        }

        binding.avHomeImgGotoSettings.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, SettingsActivity::class.java))
        }

        ListKaryawanActivity().refreshCallBack = {
            recreate()
        }


        val btnLogout = binding.avHomeImgBtnLogout

        btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this@DashboardActivity)
            .setTitle("Log out from Account")
            .setNegativeButton("No` ") {_,_ ->
            }
                .setPositiveButton("Yes") {_,_ ->
                    startActivity(Intent(this@DashboardActivity, StartActivity::class.java))
                    SharedPreferences(this@DashboardActivity).isLoggedin = -1
                    finish()
                }

            .create()
            .show()
        }


        val tvAttendStatus = binding.avHomeTvAttendStatus
        val tvCountKaryawan = binding.avHomeTvCountKaryawan
        val tvCardName = binding.avHomeTvCardName
        val tvCardID = binding.avHomeTvCardID
        val cardAttend = binding.avHomeCardAttendsNow
        val gotoListEmployees = binding.avHomeCardGotoListEmployee

        binding.avHomeCardAttendsNow.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, AttendanceActivity::class.java))
        }

        gotoListEmployees.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, ListKaryawanActivity::class.java))
        }



        lifecycleScope.launch {
            val api2 = SupabaseApi.create().countKaryawan()
            tvCountKaryawan.text = "${api2[0].count}"

        }





        val currentTime = Calendar.getInstance()
        val resetTime = Calendar.getInstance()
        val timeAbsenPagi = Calendar.getInstance()
        val timeBatasAbsen = Calendar.getInstance()

        // Waktu Absen Pagi
        timeAbsenPagi[Calendar.HOUR_OF_DAY] = 5
        timeAbsenPagi[Calendar.MINUTE] = 0

        // Batas Waktu Absen Selesai
        timeBatasAbsen[Calendar.HOUR_OF_DAY] = 7
        timeBatasAbsen[Calendar.MINUTE] = 20



        resetTime[Calendar.HOUR_OF_DAY] = 24


        if(currentTime > resetTime) {
            SharedPreferences(this@DashboardActivity).isCardAttended = 0
        }



        lifecycleScope.launch {
            try {
                binding.progressgotoAttendance.getProgressDrawable().setColorFilter(
                    Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
                binding.progressgotoAttendance.isVisible = true
                binding.iconIndicator.isVisible = false
                val id = SharedPreferences(this@DashboardActivity).isLoggedin
                val api = SupabaseApi.create().getdataKaryawan("eq.${id}" )

//            val api3 = SupabaseApi.create().postAttendance(attend)

                tvCardName.setText("${api[0].fullName}")
                tvCardID.setText("Employee ID | ${api[0].id} ")
                binding.iconIndicator.isVisible = true
                binding.progressgotoAttendance.isGone = true
                binding.swipeContainer.isRefreshing = false
            }catch (e:Exception) {
                println(e.message.toString())
                Toast.makeText(this@DashboardActivity, "Unable to Connect to Server", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@DashboardActivity, NoConnectionActivity::class.java))
                finish()
            }

        }

        lifecycleScope.launch {
            val id = SharedPreferences(this@DashboardActivity).isLoggedin
            val response = SupabaseApi.create().selflistAttendance("*","eq.${id}", "eq.now()")
            if(response.isNullOrEmpty()) {
                binding.avHomeTvAttendStatus.text = "You're not Attended Today"
                binding.iconIndicator.setImageResource(R.drawable.ic_baseline_meeting_room_24)
                binding.avHomeCardAttendsNow.setCardBackgroundColor(Color.parseColor("#FFD18B"));
            } else if(response[0].attendStatus == "checked_out") {
                binding.avHomeTvAttendStatus.text = "You are Checked Out"
                binding.avHomeCardAttendsNow.setCardBackgroundColor(Color.parseColor("#F64E4E"));

                binding.iconIndicator.isGone = true
            } else if(response[0].absenceReason!!.isNotEmpty()) {
                binding.avHomeTvAttendStatus.text = "You are Absent Today "
                binding.avHomeCardAttendsNow.setCardBackgroundColor(Color.parseColor("#F64E4E"));
                binding.iconIndicator.setImageResource(R.drawable.ic_baseline_person_off_24)
            } else {
                binding.avHomeTvAttendStatus.text = "You are Attended"
                binding.iconIndicator.setImageResource(R.drawable.ic_baseline_check_24)
            }


        }

//        tvCardName.text = "${}"






    }

}