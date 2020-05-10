package com.empyrealgames.findme.firebase

import com.empyrealgames.findme.dashboard.data.Connection
import com.empyrealgames.findme.dashboard.data.Request
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


var CONNECTIONS = "connection"
var USERS = "users"
var REQUESTS = "requests"
var LOCATION_PERMISSION_ACCESS = "location_permission_access"
var LOCATION_PERMISSION_GIVEN = "location_permission_given"
var LOCATION_REQUESTS = "location_requests"



fun acceptRequest(
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null
) {

    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {

        val batch = firestore.batch()

        val map1 = mapOf(LOCATION_PERMISSION_ACCESS to true, LOCATION_PERMISSION_GIVEN to true)
        batch.set(
            firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(
                phone
            ), map1
        )
        val map2 = mapOf(LOCATION_PERMISSION_ACCESS to true, LOCATION_PERMISSION_GIVEN to true)
        batch.set(
            firestore.collection(USERS).document(phone).collection(CONNECTIONS).document(
                currentUser
            ), map2
        )
        batch.update(
            firestore.collection(USERS).document(currentUser),
            REQUESTS,
            FieldValue.arrayRemove(phone)
        )

        batch.commit()
            .addOnSuccessListener {
                onSuccess(phone)
            }.addOnFailureListener {
                onFailed?.invoke()
                println("${it.message} ${it.cause}")
            }
    }
}


fun deleteRequest(
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        val batch = firestore.batch()
        batch.update(
            firestore.collection("users").document(currentUser),
            "requests",
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


fun deleteConnection(
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        val batch = firestore.batch()
        batch.delete(
            firestore.collection("users").document(currentUser).collection(CONNECTIONS).document(
                phone
            )
        )
        batch.delete(
            firestore.collection("users").document(phone).collection(CONNECTIONS)
                .document(currentUser)
        )
        batch.commit()
            .addOnSuccessListener {
                onSuccess(phone)
            }.addOnFailureListener {
                onFailed?.invoke()
            }
    }
}


fun getConnectionLists(
    insertConnectionInRepo: (Connection) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val phone = preferenceManager.getPhone()
    val firestore = FirebaseFirestore.getInstance()
    if (!phone.isNullOrBlank()) {
        firestore.collection(USERS).document(phone).collection(CONNECTIONS)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onFailed?.invoke()
                }
                if (snapshot != null && !snapshot.documents.isEmpty()) {
                    for (document in snapshot.documents) {
                        println("getting connections  not null + ConnViewModel")
                        insertConnectionInRepo(
                            Connection(
                                document.id,
                                locationPermissionAccess = document.getBoolean(
                                    LOCATION_PERMISSION_ACCESS
                                )!!,
                                locationPermissionGiven = document.getBoolean(
                                    LOCATION_PERMISSION_GIVEN
                                )!!
                            )
                        )
                    }
                }

            }
    }
}


fun getRequestsLists(
    insertRequestInRepo: (Request) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val firestore = FirebaseFirestore.getInstance()

    val phone = preferenceManager.getPhone()
    if (!phone.isNullOrBlank()) {
        firestore.collection(USERS).document(phone).addSnapshotListener { snapshot, e ->
            if (e != null) {
                onFailed?.invoke()
            }
            if (snapshot != null) {
                if (snapshot.get(REQUESTS) != null) {
                    println("getting requests, requests not null + ConnViewModel")
                    var list = snapshot.get(REQUESTS)
                    if (list != null) {
                        list = list as List<String>
                        for (item in list) {
                            println("view modelInserting request  " + item)
                            insertRequestInRepo(Request(item))
                        }
                    }
                }
            }

        }

    }

}