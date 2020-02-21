package com.empyrealgames.findme.dashboard.connection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class ConnectionViewModel(application: Application) : AndroidViewModel(application) {

    val REQUESTS = "requests"
    val CONNECTIONS = "connection"

    private val repository: ConnectionRepository
    var allConnections: LiveData<List<Connection>> = MutableLiveData()
    var allRequests: LiveData<List<Request>> = MutableLiveData()
    val firestore = FirebaseFirestore.getInstance()
    val preferenceManager = PreferenceManager()

    init {
        val connectionDao = ConnectionDatabase.getConnectionDatabase(application).connectionDao()
        repository = ConnectionRepository(connectionDao)
        allConnections = repository.allConnections
        allRequests = repository.allRequests
        fetchConnections()
        fetchRequests()
    }

    fun insertConnection(connection: Connection) {
        repository.insertConnection(connection)
    }

    fun insertRequest(request: Request) {
        repository.insertRequest(request)
    }


    fun deleteConnection(phone: String) {
        val currentUser = preferenceManager.getPhone(getApplication())
        if (currentUser != null) {
            firestore.collection("users").document(currentUser).update(
                "connection", FieldValue.arrayRemove(phone)
            ).addOnSuccessListener {
                firestore.collection("users").document(phone).update(
                    "connection", FieldValue.arrayRemove(currentUser)
                ).addOnSuccessListener {
                    deleteConnection(phone)
                }
            }
        }
    }

    fun acceptRequest(phone: String) {
        val currentUser = preferenceManager.getPhone(getApplication())
        if (currentUser != null) {
            firestore.collection("users").document(currentUser).update(
                "connection", FieldValue.arrayUnion(phone)
            ).addOnSuccessListener {
                firestore.collection("users").document(phone).update(
                    "connection", FieldValue.arrayUnion(currentUser)
                ).addOnSuccessListener {
                    deleteRequest(phone)
                }
              }
        }
    }

    fun deleteRequest(phone: String) {
        val currentUser = preferenceManager.getPhone(getApplication())
        if (currentUser != null) {
            firestore.collection("users").document(currentUser).update(
                "requests", FieldValue.arrayRemove(phone)
            ).addOnSuccessListener {
                repository.deleteRequest(Request(phone))
                println("ConnectionViewModel deleted request of " + phone )
            }
        }

    }

     private fun fetchConnections() {
        repository.deleteAllConnections()
        val phone = preferenceManager.getPhone(getApplication())
        if (!phone.isNullOrBlank()) {
            firestore.collection("users").document(phone).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("error in fetching connections Connection Repo " + e.message + " " + e.code)
                }
                if (snapshot != null) {
                    if (snapshot.get(CONNECTIONS) != null) {
                        var list = snapshot.get(CONNECTIONS)
                        println("getting connection requests not null + ConnViewModel")
                        if (list != null) {
                            list = list as List<String>
                            for (item in list) {
                                println("conn view modelInserting COnn  " + item)
                                insertConnection(Connection(item))
                            }
                        }
                    }
                }

            }

        }
    }

    private fun fetchRequests() {
        repository.deleteAllRequests()
        val phone = preferenceManager.getPhone(getApplication())
        if (!phone.isNullOrBlank()) {
            firestore.collection("users").document(phone).addSnapshotListener { snapshot, e ->
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
                                insertRequest(Request(item))
                            }
                        }
                    }
                }

            }

        }
    }
}

