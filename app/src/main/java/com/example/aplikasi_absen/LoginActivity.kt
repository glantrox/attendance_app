package com.example.aplikasi_absen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.EncryptUtils
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val binding:  ActivityLoginBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnBack = findViewById<ImageView>(R.id.avLogin_imageview_back)



        btnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }


        val btnLogin = binding.avLoginCardBtnLogin
        btnLogin.setOnClickListener {

            val api = SupabaseApi.create()
            val email = binding.signEmail.text.toString()
            val password = binding.signPassword.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Email is Empty", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Password is Empty", Toast.LENGTH_SHORT).show()
            } else {

                val encryptedPassword = EncryptUtils.encryptMD5ToString(password)

                lifecycleScope.launch {
                    val login = api.signKaryawan("*","eq.${email}", "eq.${encryptedPassword}")
                    if (login.equals(null)) {
                        Toast.makeText(this@LoginActivity, "GAGAL", Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    val currentUser = login[0].id
                    SharedPreferences(this@LoginActivity).isLoggedin = currentUser
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                }

            }

        }


        //Navigation to Register Activity
        val gotoRegister = findViewById<TextView>(R.id.avLogin_textview_Register)
        gotoRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

    }
}