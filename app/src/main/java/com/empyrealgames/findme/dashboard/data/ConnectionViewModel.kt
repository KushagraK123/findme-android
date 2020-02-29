package com.empyrealgames.findme.dashboard.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.empyrealgames.findme.firebase.*


class ConnectionViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: ConnectionRepository
    var allConnections: LiveData<List<Connection>> = MutableLiveData()
    var allRequests: LiveData<List<Request>> = MutableLiveData()
    var allLocations:LiveData<List<Location>> = MutableLiveData()

    init {
        val connectionDao = ConnectionDatabase.getConnectionDatabase(application).connectionDao()
        repository = ConnectionRepository(connectionDao)
        allConnections = repository.allConnections
        allRequests = repository.allRequests
        allLocations = repository.allLocations
        fetchConnections()
        fetchRequests()
    }

    fun insertLocalLocation(location: Location){
        repository.insertLocalLocation(location)
    }

    fun deleteAllLocalLocations() {
        repository.deleteAllLocalLocation()
    }

    fun insertConnection(connection: Connection) {
        repository.insertLocalConnection(connection)
    }

    fun insertRequest(request: Request) {
        repository.insertLocalRequest(request)
    }

    fun deleteLocalConnection(phone: String){
        repository.deleteLocalConnection(Connection(phone))
    }


    fun deleteConnection(phone: String) {
        deleteConnection(getApplication(),phone, ::deleteLocalConnection)
    }

    fun deleteLocalRequest(phone: String){
        repository.deleteLocalRequest(Request(phone))
    }

    fun acceptRequest(phone: String) {
        com.empyrealgames.findme.firebase.acceptRequest(getApplication(), phone, ::deleteRequest)
    }

    fun deleteRequest(phone: String) {
        deleteRequest(getApplication(), phone, ::deleteLocalRequest)
    }

     private fun fetchConnections() {
        repository.deleteAllLocalConnections()
         getConnectionLists(getApplication(), ::insertConnection)
    }

    private fun fetchRequests() {
        repository.deleteAllLocalRequests()
        getRequestsLists(getApplication(), ::insertRequest)
    }
     fun fetchLocations(connection:String){
        repository.deleteAllLocalLocation()
        getLocationsList(getApplication(), connection, ::insertLocalLocation)
    }

}

