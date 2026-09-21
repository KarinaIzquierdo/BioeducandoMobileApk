package com.bioeducando.mobile.data.local

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
        private const val USER_ROLE = "user_role"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val USER_PHOTO = "user_photo"
        private const val USER_ID = "user_id"
    }

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveRole(role: String) {
        val editor = prefs.edit()
        editor.putString(USER_ROLE, role)
        editor.apply()
    }

    fun getRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun saveUser(id: Int, name: String, email: String, photoUrl: String?) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.putString(USER_NAME, name)
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_PHOTO, photoUrl)
        editor.apply()
    }

    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun getUserPhotoUrl(): String? {
        return prefs.getString(USER_PHOTO, null)
    }

    fun getUserId(): Int {
        return prefs.getInt(USER_ID, -1)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_ROLE)
        editor.remove(USER_NAME)
        editor.remove(USER_EMAIL)
        editor.remove(USER_PHOTO)
        editor.remove(USER_ID)
        editor.apply()
    }
}
