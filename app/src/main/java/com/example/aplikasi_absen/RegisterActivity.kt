package com.example.aplikasi_absen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.EncryptUtils
import com.example.aplikasi_absen.database.SupabaseApi
import com.example.aplikasi_absen.databinding.ActivityRegisterBinding
import com.example.aplikasi_absen.model.Register
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btnBack = binding.avRegisterImageviewBack
        val btnRegister = binding.avRegisterCardBtnRegister
        val api = SupabaseApi.create()





        btnRegister.setOnClickListener {
            val fullname = binding.iptFullname.text.toString()
            val address = binding.iptAddress.text.toString()
            val email = binding.iptEmail.text.toString()
            val password = binding.iptPassword.text.toString()
            val confirmPassword = binding.iptConfirmpassword.text.toString()
            val phonenumber = binding.iptPhonenumber.text.toString()


            if (fullname.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Full Name is Empty!", Toast.LENGTH_SHORT).show()
            } else if (address.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Address is Empty!", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Email is Empty!", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Password is Empty!", Toast.LENGTH_SHORT).show()
            } else if (phonenumber.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Phone Number is Empty!", Toast.LENGTH_SHORT).show()
            } else {

                if(confirmPassword != password) {
                    Toast.makeText(this@RegisterActivity, "Password Does'nt Match!", Toast.LENGTH_SHORT).show()
                }

                lifecycleScope.launch {

                    val encryptedPassword = EncryptUtils.encryptMD5ToString(password)

                    val register = Register(
                        address,
                        email,
                        fullname,
                        encryptedPassword,
                        phonenumber
                    )
                    val response = api.insertKaryawan(register)
//                    response[0].id
                }
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

        }

        btnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        //Navigate to Login Activity
        val textgotoLogin = findViewById<TextView>(R.id.avRegister_textview_login)
        textgotoLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}