package com.example.aplikasi_absen.database

import com.example.aplikasi_absen.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SupabaseApi {

    @POST("karyawan")
    suspend fun insertKaryawan(
        @Body register: Register,
        @Header("apikey") apikey:String = APIKEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json"
    ): RegisterResponse

    @PATCH("karyawan")
    suspend fun updateKaryawan(
        @Query("id") id:String,
        @Body update: UpdateResponseItem,
        @Header("apikey") apikey:String = APIKEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json"
    ): UpdateResponse

    @GET("karyawan")
    suspend fun signKaryawan(
        @Query("select") select:String,
        @Query("email") email:String,
        @Query("password") password:String,
        @Header("apikey") apikey:String = APIKEY
    ): LoginResponse

    @GET("karyawan")
    suspend fun getdataKaryawan(
        @Query("id") id: String,
        @Header("apikey") apikey:String = APIKEY
    ): LoginResponse

    @POST("attendance")
    suspend fun postAttendance(
        @Body attend: Attendance,
        @Header("apikey") apikey:String = APIKEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json"

    ): AttendanceResponse

    @GET("karyawan")
    suspend fun countKaryawan(
        @Query("select") select:String = "count",
        @Header("apikey") apikey:String = APIKEY

    ): CountKaryawan

    @GET("karyawan")
    suspend fun listKaryawan(
        @Query("select") select:String = "*",
        @Header("apikey") apikey:String = APIKEY

        ): List<LoginResponseItem>

    @GET("attendance")
    suspend fun listAttendance(
        @Query("select") select:String = "*",
        @Query("attend_date") attend_date:String,
        @Header("apikey") apikey:String = APIKEY

    ): AttendanceResponse

    @GET("attendance")
    suspend fun selflistAttendance(
        @Query("select") select:String = "*",
        @Query("user_id") user_id:String ,
        @Query("attend_date") attend_date:String,
        @Header("apikey") apikey:String = APIKEY

    ): AttendanceResponse



    @PATCH("attendance")
    suspend fun checkoutAttend(
        @Query("id") id:String,
        @Body checkout:CheckedOut,
        @Header("apikey") apikey:String = APIKEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json"

    ): CheckedOutRespon

    @DELETE("karyawan")
    suspend fun deleteKaryawan(
        @Query("id") id:String,
        @Header("apikey") apikey:String = APIKEY,

        ): RegisterResponse



    companion object {

        private const val APIKEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5vdmxoa2d4anZjeXVyY3ZuaXljIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NjgzNDY5NzAsImV4cCI6MTk4MzkyMjk3MH0.3ZGFUTGdh5f-fFG-_hEMHSh-3f4ciUPnHvIBNN3Zzog"




        fun create(): SupabaseApi {
            return Retrofit.Builder()
                .baseUrl("https://novlhkgxjvcyurcvniyc.supabase.co/rest/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SupabaseApi::class.java)
        }
    }

}