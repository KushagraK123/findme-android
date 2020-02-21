package com.empyrealgames.findme.pref

import android.content.Context
import android.content.SharedPreferences
import java.util.prefs.Preferences

class PreferenceManager{
    val USERNAME  = "USERNAME"
    val PHONE = "PHONE"
    fun setUserName(username:String, context: Context){
        val  preferences:SharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        preferences.edit().putString(USERNAME, username).apply()
    }


    fun getUserName(context: Context):String?{
        return context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE).getString(USERNAME, "")
    }

    fun getPhone(context: Context):String?{
        return context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE).getString(PHONE, "")
    }

    fun setPhone(phone:String, context: Context){
        val  preferences:SharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        preferences.edit().putString(PHONE, phone).apply()
    }

}