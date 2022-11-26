package com.example.aplikasi_absen

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.databinding.DetailAbsenBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FragmentDetailAbsen : BottomSheetDialogFragment() {
    private lateinit var parser : SimpleDateFormat
    private lateinit var pengen : SimpleDateFormat

    private lateinit var parserDate : SimpleDateFormat
    private lateinit var pengenDate : SimpleDateFormat

    val binding: DetailAbsenBinding by viewBinding()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? = inflater.inflate(R.layout.detail_absen, container, true)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val nameKaryawan = arguments?.getString(EXTRA_NAMEKARYAWAN)
        val idKaryawan = arguments?.getInt(EXTRA_ID)
        val cekot = arguments?.getString(EXTRA_CHECKEDOUT)
        val cekin = arguments?.getString(EXTRA_CHECKIN)
        val attendStatus = arguments?.getString(EXTRA_ATTENDSTATUS)
        val absenceReason = arguments?.getString(EXTRA_ABSENCEREASON)
        val attendDate = arguments?.getString(EXTRA_DATE)




//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)



        parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        pengen = SimpleDateFormat("HH:mm")
        val checkedOut = if (cekot.isNullOrEmpty()) {
            ""
        } else {
            pengen.format(parser.parse(cekot))
        }
        val checkedIn = pengen.format(parser.parse(cekin))
        if(checkedOut.isNullOrEmpty()) {
            binding.tvcekot.isGone = true
            binding.tvcekot2.isGone = true

        } else  {
            binding.tvcekot.text = checkedOut
        }

        val dateParser = SimpleDateFormat("yyyy-MM-dd")
        val datePengen = SimpleDateFormat("MMMM dd, yyyy")
        val date = datePengen.format(dateParser.parse(attendDate))





        binding.nameKaryawan.text = nameKaryawan.toString()
        binding.tvAttendanceID.text = "Attendance ID : ${idKaryawan}\n${date}"
        if (absenceReason?.isNotEmpty() == true) {
         binding.tvAttendedAttop.text = "${absenceReason}"
         binding.statustvtop.text ="ABSENT"
         binding.cardTop.setCardBackgroundColor(Color.parseColor("#F64E4E"));
         binding.cardFoto.isGone = true
         binding.tvAttendbawah.text = nameKaryawan
         binding.tvcekin.text = "${checkedIn}  |  ${absenceReason}"
         binding.tvcekot2.text = "Absent"
         binding.tvcekot.text = absenceReason
        } else if (attendStatus == "checked_out") {
            binding.tvAttendedAttop.text = "Picture taken at ${checkedIn}"
            binding.statustvtop.text ="CHECKED OUT"
            binding.cardTop.setCardBackgroundColor(Color.parseColor("#F64E4E"));
            binding.tvcekin.text = checkedIn
        } else if (attendStatus == "attended") {
            binding.tvAttendedAttop.text = "Picture taken at ${checkedIn}"
            binding.statustvtop.text ="ATTENDED"
            binding.cardTop.setCardBackgroundColor(Color.parseColor("#B7DA38"));
            binding.tvcekin.text = checkedIn
        }







    }
    companion object {
        const val EXTRA_NAMEKARYAWAN = "key_name"
        const val EXTRA_ID = "key_id"
        const val EXTRA_CHECKEDOUT = "key_out"
        const val EXTRA_CHECKIN = "key_in"
        const val EXTRA_ATTENDSTATUS = "key_status"
        const val EXTRA_ABSENCEREASON = "key_absencereason"
        const val EXTRA_DATE = "key_date"



    }
}