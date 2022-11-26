package com.example.aplikasi_absen.model


import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String
)