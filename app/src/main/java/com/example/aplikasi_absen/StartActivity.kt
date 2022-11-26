package com.example.aplikasi_absen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aplikasi_absen.databinding.ActivityStartBinding
import com.google.android.material.card.MaterialCardView

class StartActivity : AppCompatActivity() {
    private val binding: ActivityStartBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        if(SharedPreferences(this@StartActivity).isLoggedin >= 0) {
            val intent = Intent(this@StartActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()

        }



            //Navigations to Sign in and Sign up
            val gotoRegister: MaterialCardView = findViewById(R.id.avStart_card_signup)
            val gotoLogin: MaterialCardView = findViewById(R.id.avStart_card_signin)
            gotoRegister.setOnClickListener {
                startActivity(Intent(this@StartActivity, RegisterActivity::class.java))
            }
            gotoLogin.setOnClickListener {
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
            }
    }
}