package com.empyrealgames.findme.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

val UID = "uid"
val USERNAME = "username"

fun isUserLoggedIn(): Boolean {
    return FirebaseAuth.getInstance().currentUser != null
}

fun setUpPrefs(){

}

fun createAccount(
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
                        setUpPrefs()
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

