package com.example.storyapp.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(Const.PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun setStringPreference(prefKey: String, value: String) {
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    fun clearPreferences() {
        editor.clear().apply()
    }

    val getToken = prefs.getString(Const.KEY_TOKEN, "")
    val getUserId = prefs.getString(Const.KEY_USER_ID, "")
    val isLogin = prefs.getBoolean(Const.KEY_IS_LOGIN, false)
    val getUserName = prefs.getString(Const.KEY_USER_NAME, "")
    val getEmail = prefs.getString(Const.KEY_EMAIL, "")


}