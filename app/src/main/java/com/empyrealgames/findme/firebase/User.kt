package com.empyrealgames.findme.firebase

import android.content.Context
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.auth.FirebaseAuth

fun isUserLoggedIn(context: Context):Boolean{
    val preferenceManager = PreferenceManager()
    val mAuth = FirebaseAuth.getInstance()
    val b:Boolean = mAuth.currentUser!=null
    if(b){
        preferenceManager.setPhone(mAuth.currentUser!!.phoneNumber!!, context)
        preferenceManager.setUserName(mAuth.currentUser!!.displayName!!, context)
    }
    return b
}