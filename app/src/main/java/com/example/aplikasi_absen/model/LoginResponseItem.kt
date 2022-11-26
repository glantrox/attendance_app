package com.example.aplikasi_absen.model


import com.google.gson.annotations.SerializedName

data class LoginResponseItem(
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String
)