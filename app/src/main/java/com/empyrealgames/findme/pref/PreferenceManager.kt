package com.empyrealgames.findme.pref

import com.google.firebase.auth.FirebaseAuth

class PreferenceManager{
    fun getUserName(): String? {
        return FirebaseAuth.getInstance().currentUser!!.displayName
    }

    fun getPhone(): String? {
        return FirebaseAuth.getInstance().currentUser!!.phoneNumber
    }
}