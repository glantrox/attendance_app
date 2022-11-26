package com.example.aplikasi_absen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.ActivityListKaryawanBinding
import com.example.aplikasi_absen.model.LoginResponseItem
import kotlinx.coroutines.launch

class ListKaryawanActivity : AppCompatActivity() {
    var refreshCallBack : (() -> Unit)? = null
    val binding: ActivityListKaryawanBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_karyawan)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        binding.swipeContainer.setOnRefreshListener {
            Toast.makeText(this@ListKaryawanActivity, "Refreshed!", Toast.LENGTH_SHORT).show()
            recreate()
        }



        val btnBack = binding.avListKaryawanImgBtnBack

        btnBack.setOnClickListener {
            refreshCallBack?.invoke()
            super.onBackPressed()
            finish()
        }


        fun setKaryawanAdapter(karyawan: List<LoginResponseItem>) {
            val recyclerView = binding.avListKaryawanRVListKaryawan
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this@ListKaryawanActivity)
            val karyawanAdapter = KaryawanAdapter(karyawan)
            recyclerView.adapter = karyawanAdapter

            karyawanAdapter.deleteClickListener = {
                lifecycleScope.launch {
                    if(it.id == SharedPreferences(this@ListKaryawanActivity).isLoggedin) {
                        Toast.makeText(this@ListKaryawanActivity, "You cannot Delete current logged in Account!", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            val apiDelete = SupabaseApi.create().deleteKaryawan("eq.${it.id}")
                            Toast.makeText(this@ListKaryawanActivity, "${it.fullName} has been Fired", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@ListKaryawanActivity, "${it.fullName} has been Fired", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@ListKaryawanActivity, ListKaryawanActivity::class.java))
                            finish()
                        }

                    }
                }

            }


        }

        lifecycleScope.launch {
            binding.progressbarAttendance.isVisible = true
            val api = SupabaseApi.create().listKaryawan()
            setKaryawanAdapter(api)
            binding.progressbarAttendance.isVisible = false
            binding.swipeContainer.isRefreshing = false

        }







    }


}