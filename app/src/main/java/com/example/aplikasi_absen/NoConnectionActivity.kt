package com.example.aplikasi_absen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.databinding.ActivityNoConnectionBinding

class NoConnectionActivity : AppCompatActivity() {
    val binding: ActivityNoConnectionBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        binding.btnQuitapp.setOnClickListener {
            finish()
        }

    }
}