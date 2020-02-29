package com.empyrealgames.findme.firebase

import android.content.Context
import com.empyrealgames.findme.dashboard.data.Location
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

val LOCATION = "location"
val LATITUTE = "lat"
val LONGITUDE = "long"
fun updateLocation( time:String,  lat:String, long:String){
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    if(auth.currentUser != null){
        val phone = auth.currentUser!!.phoneNumber
        val location = hashMapOf("lat" to lat,
            "long" to long)
        firestore.collection(USERS).document(phone!!).collection("location").document(time).set(location, SetOptions.merge()).addOnSuccessListener {
            println("updated location in  firebaseLocation, yo")
        }
            .addOnFailureListener {
                println("updating  location failed  firebaseLocation, yo")
            }
    }
}

fun getLocationsList(context: Context, connection: String, insertLocationInRepo:(Location)->Unit, onFailed: (() -> Unit)? = null){
    val preferenceManager = PreferenceManager()
    val firestore = FirebaseFirestore.getInstance()
    val currUser = preferenceManager.getPhone(context)
    if (!currUser.isNullOrBlank()) {
        firestore.collection(USERS).document(connection).collection(LOCATION).addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("error in location in Location Firebase File " + e.message + " " + e.code)
            }
            if (snapshot != null) {
                val size = snapshot.size()
                println("ftech $size locations of user $connection")
                for(document in snapshot.documents){
                    insertLocationInRepo(Location(document.id.toString(), document.get(LATITUTE).toString(), document.get(LONGITUDE).toString()))
                }
            }

        }

    }

}