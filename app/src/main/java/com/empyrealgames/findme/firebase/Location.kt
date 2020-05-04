package com.empyrealgames.findme.firebase

import android.content.Context
import com.empyrealgames.findme.dashboard.data.Location
import com.empyrealgames.findme.dashboard.data.LocationPermissionRequest
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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
        val location = hashMapOf(
            LATITUTE to lat,
            LONGITUDE to long)
        firestore.collection(USERS).document(phone!!).collection(LOCATION).document(time).set(location, SetOptions.merge()).addOnSuccessListener {
            println("updated location in  firebaseLocation, yo")
        }
            .addOnFailureListener {
                println("updating  location failed  firebaseLocation, yo")
            }
    }
}


fun sendLocationPermissionRequest(
    phone: String,
    onSuccess: () -> Unit,
    onFailed: (() -> Unit)? = null

) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        firestore.collection("users").document(phone).update(
            LOCATION_REQUESTS, FieldValue.arrayUnion(currentUser)
        )
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailed?.invoke()
            }
    }
}


fun acceptLocationPermissionRequest(
    phone: String,
    onSuccess: () -> Unit,
    onFailed: (() -> Unit)? = null

) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        val map = mapOf(LOCATION_PERMISSION_GIVEN to true)
        firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(phone)
            .set(map, SetOptions.merge())
            .addOnSuccessListener {
                val map2 = mapOf(LOCATION_PERMISSION_ACCESS to true)
                firestore.collection(USERS).document(phone).collection(CONNECTIONS).document(currentUser)
                    .set(map2, SetOptions.merge())
            }
           .addOnSuccessListener {
                firestore.collection(USERS).document(currentUser)
                    .update(LOCATION_REQUESTS, FieldValue.arrayRemove(phone))
            }.addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailed?.invoke()
            }
    }
}


fun deleteLocationPermissionRequest(
    phone: String,
    onSuccess: () -> Unit,
    onFailed: (() -> Unit)? = null

) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        firestore.collection(USERS).document(currentUser).update(
            LOCATION_REQUESTS, FieldValue.arrayRemove(phone)
        ).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailed?.invoke()
        }
    }
}

fun deleteLocationPermission(
    phone: String,
    onSuccess: () -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        val map = mapOf(LOCATION_PERMISSION_GIVEN to false)
        firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(phone)
            .set(map, SetOptions.merge())
            .addOnSuccessListener {
                val map2 = mapOf(LOCATION_PERMISSION_ACCESS to false)
                firestore.collection(USERS).document(phone).collection(CONNECTIONS).document(currentUser)
                    .set(map2, SetOptions.merge())
            }.addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailed?.invoke()
            }
    }
}

fun grantLocationPermission(
    phone: String,
    onSuccess: (() -> Unit),
    onFailed: (() -> Unit)

){
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        println("currentuser is $currentUser phone is $phone")
        val map = mapOf(LOCATION_PERMISSION_GIVEN to true)
        firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(phone)
            .set(map, SetOptions.merge())
            .addOnSuccessListener {
                val map2 = mapOf(LOCATION_PERMISSION_ACCESS to true)
                firestore.collection(USERS).document(phone).collection(CONNECTIONS).document(currentUser)
                    .set(map2, SetOptions.merge())
            }
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailed()
            }
    }
}

fun getLocationsList(
    phone: String,
    insertLocationInRepo: (Location) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val firestore = FirebaseFirestore.getInstance()
    val currUser = preferenceManager.getPhone()
    if (!currUser.isNullOrBlank()) {
        firestore.collection(USERS).document(phone).collection(LOCATION)
            .addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("error in location in Location Firebase File " + e.message + " " + e.code)
            }
            if (snapshot != null) {
                val size = snapshot.size()
                println("ftech $size locations of user $phone")
                for(document in snapshot.documents){
                    insertLocationInRepo(
                        Location(
                            document.id,
                            document.get(LATITUTE).toString(),
                            document.get(LONGITUDE).toString()
                        )
                    )
                }
            }

        }

    }

}

fun getLocationPermissionRequests(
    insertLocationPermissionRequestInRepo: (LocationPermissionRequest) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val phone = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (!phone.isNullOrBlank()) {
        firestore.collection(USERS).document(phone)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("error in fetching location permission requests Connection Repo " + e.message + " " + e.code)
                }
                if (snapshot != null ) {
                    if (snapshot.get(LOCATION_REQUESTS) != null) {
                        println("getting requests, requests not null + ConnViewModel")
                        var list = snapshot.get(LOCATION_REQUESTS)
                        if (list != null) {
                            list = list as List<*>
                            for (item in list) {
                                println("view modelInserting request  " + item)
                                insertLocationPermissionRequestInRepo(LocationPermissionRequest(item.toString()))
                            }
                        }
                    }
                }

            }

    }
}

