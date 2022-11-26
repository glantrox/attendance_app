package com.example.aplikasi_absen.model


import com.google.gson.annotations.SerializedName

data class AttendanceResponseItem(
    @SerializedName("absence_reason")
    val absenceReason: String,
    @SerializedName("attend_status")
    val attendStatus: String,
    @SerializedName("attended_at")
    val attendedAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name_karyawan")
    val nameKaryawan: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("checked_out")
    val checkedOut: String,
    @SerializedName("attend_date")
    val attendDate: String?

)