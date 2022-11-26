package com.example.aplikasi_absen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.FragmentCheckInBinding
import com.example.aplikasi_absen.model.Attendance
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.log


class CheckIn : BottomSheetDialogFragment() {

    val binding: FragmentCheckInBinding by viewBinding()
    var onDismissCallback: (() -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
    View? = inflater.inflate(R.layout.fragment_check_in, container, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idlogin = SharedPreferences(requireContext()).isLoggedin

        val btnback = view.findViewById<ImageView>(R.id.bsCheckin_img_btnBack)

        btnback?.setOnClickListener {
            onDismissCallback?.invoke()
            dialog?.dismiss()
        }

        val cardAttend = view.findViewById<MaterialCardView>(R.id.card_attend)
        val currentTime = Calendar.getInstance()

        val storage = Firebase.storage
        fun openCameraPicker() {
            PictureSelector.create(this)
                .openCamera(SelectMimeType.ofImage())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        val image = result?.first()
                        image?.let {
                            storage.getReference("fotoabsen").putFile(it.path.toUri())
                                .addOnCompleteListener {
                                    if(!it.isSuccessful) {
                                        Toast.makeText(requireContext(), "Gagal Upload File", Toast.LENGTH_SHORT).show()
                                        return@addOnCompleteListener
                                    }
                                    val imageUrl = it.result.storage.downloadUrl.toString()
                                    Log.d("CEKDATAIMAGE", imageUrl)
//                                    lifecycleScope.launch {
//                                        binding.progressbarAttendance.isVisible = false
//                                        val api = SupabaseApi.create()
//                                        val apiName = SupabaseApi.create().getdataKaryawan("eq.${idlogin}")
//                                        val attend = Attendance("","attended",apiName[0].fullName, idlogin.toString(), imageUrl)
//                                        val attendApi = SupabaseApi.create().postAttendance(attend)
//                                        SharedPreferences(requireContext()).isAttended = attendApi[0].id!!
//                                        binding.progressbarAttendance.isVisible = true
//                                        Log.d("CEKDATA", "$attendApi")
//                                        Toast.makeText(requireContext(), "You are Attended!", Toast.LENGTH_SHORT).show()
//                                        dialog?.dismiss()
//                                        onDismissCallback?.invoke()
//                                    }
                                }
                        }
                        Log.d("CEKDATAIMAGE2", image.toString())


                    }
                    override fun onCancel() {}
                })
        }


        cardAttend.setOnClickListener {
            lifecycleScope.launch {
                binding.progressbarAttendance.isVisible = false
                val api = SupabaseApi.create()
                val apiName = SupabaseApi.create().getdataKaryawan("eq.${idlogin}")
                val attend = Attendance("","attended",apiName[0].fullName, idlogin.toString(),"")
                val attendApi = SupabaseApi.create().postAttendance(attend)
                SharedPreferences(requireContext()).isAttended = attendApi[0].id
                onDismissCallback?.invoke()
                binding.progressbarAttendance.isVisible = true
                Log.d("CEKDATA", "$attendApi")
                Toast.makeText(requireContext(), "You are Attended!", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }


        }

        val inputAbsent = view.findViewById<TextInputEditText>(R.id.inputAbsent)

        inputAbsent.setOnEditorActionListener { textView, id, keyEvent ->
            val query = "${textView.text}"



            if(id == EditorInfo.IME_ACTION_DONE){
                lifecycleScope.launch {

                    val apiName = SupabaseApi.create().getdataKaryawan("eq.${idlogin}")
                    val absent = Attendance(query,"absent", apiName[0].fullName, idlogin.toString(), "")
                    val apiAbsent = SupabaseApi.create().postAttendance(absent)
                    SharedPreferences(requireContext()).isAttended = apiAbsent[0].id
                    onDismissCallback?.invoke()
                    Log.d("idattend","${apiAbsent[0].id}")
                    Toast.makeText(requireContext(), "Absent status sent!", Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }


            }

            true

        }


    }

}