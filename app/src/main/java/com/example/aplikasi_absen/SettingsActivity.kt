package com.example.aplikasi_absen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    val binding: ActivitySettingsBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        binding.swipeContainer.setOnRefreshListener {
            recreate()
        }

        val id = SharedPreferences(this@SettingsActivity).isLoggedin

        binding.avSettingsImgBtnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        lifecycleScope.launch {
            binding.swipeContainer.isRefreshing = true
            val api = SupabaseApi.create().getdataKaryawan("eq.${id}")
            binding.swipeContainer.isRefreshing = false
            binding.avSettingsTvNameAccount.text = api[0].fullName
            binding.avSettingsTvEmailAccount.text = api[0].email
            binding.avSettingsTvPhonenumberAccount.text = api[0].phone
        }

        binding.avSettingsCardGotoEdit.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, UpdateActivity::class.java))
        }

    }
}