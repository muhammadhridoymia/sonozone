package com.example.sonozone.Storage

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveAuth(token: String, name: String?) {
        prefs.edit()
            .putString("token", token)
            .putString("name", name)
            .apply()
    }

    fun getToken(): String? = prefs.getString("token", null)
    fun getName(): String? = prefs.getString("name", null)


    fun logout() {
        prefs.edit().clear().apply()
    }
}