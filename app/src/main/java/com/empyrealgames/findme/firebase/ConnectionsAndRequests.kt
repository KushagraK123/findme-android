package com.empyrealgames.findme.firebase

import android.content.Context
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
    context: Context,
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null
) {

    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone(context)
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        val map = mapOf(LOCATION_PERMISSION_ACCESS to true, LOCATION_PERMISSION_GIVEN to true)
        firestore.collection(USERS).document(currentUser).collection(CONNECTIONS).document(phone)
            .set(map)
            .addOnSuccessListener {
                val map = mapOf(LOCATION_PERMISSION_ACCESS to true, LOCATION_PERMISSION_GIVEN to true)
                firestore.collection(USERS).document(phone).collection(CONNECTIONS)
                    .document(currentUser).set(map)
                    .addOnSuccessListener {
                        firestore.collection(USERS).document(currentUser).update(
                            REQUESTS, FieldValue.arrayRemove(phone)
                        ).addOnSuccessListener {
                            onSuccess(phone)
                        }
                    }
            }
    }
}


fun deleteRequest(
    context: Context,
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone(context)
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        firestore.collection("users").document(currentUser).update(
            "requests", FieldValue.arrayRemove(phone)
        ).addOnSuccessListener {
            onSuccess(phone)
        }
    }
}


fun deleteConnection(
    context: Context,
    phone: String,
    onSuccess: (String) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val currentUser = preferenceManager.getPhone(context)
    val firestore = FirebaseFirestore.getInstance()
    if (currentUser != null) {
        firestore.collection("users").document(currentUser).collection(CONNECTIONS).document(phone)
            .delete()
            .addOnSuccessListener {
                firestore.collection("users").document(phone).collection(CONNECTIONS)
                    .document(currentUser).delete()
                    .addOnSuccessListener {
                        onSuccess(phone)
                    }
            }
    }

}

fun getConnectionLists(
    context: Context,
    insertConnectionInRepo: (Connection) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val phone = preferenceManager.getPhone(context)
    val firestore = FirebaseFirestore.getInstance()
    if (!phone.isNullOrBlank()) {
        firestore.collection(USERS).document(phone).collection(CONNECTIONS)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("error in fetching connections Connection Repo " + e.message + " " + e.code)
                }
                if (snapshot != null && !snapshot.documents.isEmpty()) {
                    for (document in snapshot.documents) {
                        println("getting connections  not null + ConnViewModel")
                        insertConnectionInRepo(
                            Connection(
                                document.id,
                                locationPermissionAccess =  document.getBoolean(LOCATION_PERMISSION_ACCESS)!!,
                                locationPermissionGiven = document.getBoolean(LOCATION_PERMISSION_GIVEN)!!
                            )
                        )
                    }
                }

            }

    }
}


fun getRequestsLists(
    context: Context,
    insertRequestInRepo: (Request) -> Unit,
    onFailed: (() -> Unit)? = null
) {
    val preferenceManager = PreferenceManager()
    val firestore = FirebaseFirestore.getInstance()

    val phone = preferenceManager.getPhone(context)
    if (!phone.isNullOrBlank()) {
        firestore.collection(USERS).document(phone).addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("error in fetching requests  Repo " + e.message + " " + e.code)
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