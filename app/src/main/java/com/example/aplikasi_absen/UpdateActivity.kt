package com.example.aplikasi_absen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.ActivityUpdateBinding
import com.example.aplikasi_absen.model.UpdateResponseItem
import kotlinx.coroutines.launch

class UpdateActivity : AppCompatActivity() {
    val binding: ActivityUpdateBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        val id = SharedPreferences(this@UpdateActivity).isLoggedin

        binding.avUpdateImgBtnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        val editFullName = binding.editFullName
        val editAddress = binding.editAddress
        val editEmail = binding.editEmail
        val editPhoneNumber = binding.editPhoneNumber

        lifecycleScope.launch {
            val apiEdit = SupabaseApi.create().getdataKaryawan("eq.${id}")

            editFullName.setText(apiEdit[0].fullName)
            editAddress.setText(apiEdit[0].address)
            editEmail.setText(apiEdit[0].email)
            editPhoneNumber.setText(apiEdit[0].phone)



        }

        binding.avUpdateImgBtnSave.setOnClickListener {
        lifecycleScope.launch {

            val update = UpdateResponseItem(
                editAddress.toString(),
                editEmail.toString(),
                editFullName.toString(),
                editPhoneNumber.toString()
            )
            val apiUpdate = SupabaseApi.create().updateKaryawan("eq.${id}", update)

        }
            super.onBackPressed()
            finish()
        }







    }
}