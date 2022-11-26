package com.example.aplikasi_absen.model


import com.google.gson.annotations.SerializedName

data class UpdateResponseItem(
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("phone")
    val phone: String
)