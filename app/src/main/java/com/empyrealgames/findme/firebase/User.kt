package com.empyrealgames.findme.firebase

import android.content.Context
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

val UID = "uid"
val USERNAME = "username"

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

fun setUpPrefs( mAuth:FirebaseAuth, context: Context){
    val preferenceManager = PreferenceManager()
    preferenceManager.setUserName(mAuth.currentUser!!.displayName!!, context!!)
    preferenceManager.setPhone(mAuth.currentUser!!.phoneNumber!!, context!!)
}

fun createAccount(
    context: Context,
    fName:String,
    lName:String,
    onComplete : ()->Unit,
    onFailed : ()->Unit

) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    if(auth.currentUser!=null){
        val update = UserProfileChangeRequest.Builder()
            .setDisplayName(fName + " " + lName).build()
        auth.currentUser!!.updateProfile(update).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = hashMapOf(UID to auth.currentUser!!.uid,
                    USERNAME to auth.currentUser!!.displayName)
                db.collection(USERS).document(auth.currentUser!!.phoneNumber!!).set(user, SetOptions.merge())
                    .addOnSuccessListener { documentReference ->
                        setUpPrefs(auth, context)
                        onComplete()
                    }
                    .addOnFailureListener { e ->
                        auth.signOut()
                        onFailed()
                    }
            }else{
                onFailed()
            }
        }
    }else{
        onFailed()
    }

}



fun sendOtp(
    phone:String,
    onComplete : ()->Unit,
    onFailed : ()->Unit){




}