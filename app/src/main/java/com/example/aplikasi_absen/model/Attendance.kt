package com.example.aplikasi_absen.model


import com.google.gson.annotations.SerializedName

data class Attendance(
    @SerializedName("absence_reason")
    val absenceReason: String,
    @SerializedName("attend_status")
    val attendStatus: String,
    @SerializedName("name_karyawan")
    val nameKaryawan: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("img_attend")
    val imgAttend: String?
)