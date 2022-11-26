package com.example.aplikasi_absen.model


import com.google.gson.annotations.SerializedName

data class CheckedOut(
    @SerializedName("attend_status")
    val attendStatus: String,
    @SerializedName("checked_out")
    val checkedOut: String,

)