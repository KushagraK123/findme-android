package com.empyrealgames.findme.firebase

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
        val batch = firestore.batch()
        batch.set(
            firestore.collection(USERS).document(phone!!).collection(LOCATION).document(time),
            location,
            SetOptions.merge()
        )
        batch.commit().addOnSuccessListener {
            println("updated location in  firebaseLocation, yo")
        }.addOnFailureListener {
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
        val batch = firestore.batch()
        batch.update(
            firestore.collection("users").document(phone),
            LOCATION_REQUESTS,
            FieldValue.arrayUnion(currentUser)
        )
        batch.commit()
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailed?.invoke()
            }
    }
}


fun acceptLocationPermissionRequest(
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null

) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        val map = mapOf(LOCATION_PERMISSION_GIVEN to true)
        val batch = firestore.batch()
        batch.set(
            firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(
                phone
            ), map, SetOptions.merge()
        )
        val map2 = mapOf(LOCATION_PERMISSION_ACCESS to true)

        batch.set(
            firestore.collection(USERS).document(phone).collection(CONNECTIONS).document(currentUser),
            map2,
            SetOptions.merge()
        )
        batch.update(
            firestore.collection(USERS).document(currentUser),
            LOCATION_REQUESTS,
            FieldValue.arrayRemove(phone)
        )

        batch.commit()
            .addOnSuccessListener {
                onSuccess(phone)
            }.addOnFailureListener {
                onFailed?.invoke()
            }
    }
}


fun deleteLocationPermissionRequest(
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null

) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        firestore.collection(USERS).document(currentUser).update(
            LOCATION_REQUESTS, FieldValue.arrayRemove(phone)
        ).addOnSuccessListener {
            onSuccess(phone)
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
        val batch = firestore.batch()

        batch.set(
            firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(phone)
            , map, SetOptions.merge()
        )

        val map2 = mapOf(LOCATION_PERMISSION_ACCESS to false)
        batch.set(
            firestore.collection(USERS).document(phone).collection(CONNECTIONS).document(currentUser)
            , map2, SetOptions.merge()
        )
        batch.commit().addOnSuccessListener {
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

        val batch = firestore.batch()
        val map = mapOf(LOCATION_PERMISSION_GIVEN to true)

        batch.set(
            firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(phone)
            , map, SetOptions.merge()
        )

        val map2 = mapOf(LOCATION_PERMISSION_ACCESS to true)
        batch.set(
            firestore.collection(USERS).document(phone).collection(CONNECTIONS).document(currentUser)
            , map2, SetOptions.merge()
        )

        batch.commit()
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
                onFailed?.invoke()
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
                    onFailed?.invoke()
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

