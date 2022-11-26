package com.example.aplikasi_absen

import androidx.preference.PreferenceManager
import android.content.Context

class SharedPreferences(context: Context) {
    companion object {
        const val LOGIN_KEY = "id_karyawan"
        const val ATTEND_KEY = "attend_key"
        const val CARDATTEND_KEY = "cardattend_key"

    }

    private val currentLogin = PreferenceManager.getDefaultSharedPreferences(context)
    var isLoggedin = currentLogin.getInt(LOGIN_KEY, -1)
    set(value) = currentLogin.edit().putInt(LOGIN_KEY, value).apply()

    private val currentAttend = PreferenceManager.getDefaultSharedPreferences(context)
    var isAttended = currentAttend.getInt(ATTEND_KEY, -1)
    set(value) = currentAttend.edit().putInt(ATTEND_KEY, value).apply()

    private val cardAttend = PreferenceManager.getDefaultSharedPreferences(context)
    var isCardAttended = currentAttend.getInt(CARDATTEND_KEY, 0)
        set(value) = currentAttend.edit().putInt(CARDATTEND_KEY, value).apply()

}