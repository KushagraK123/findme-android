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
    var allLocationPermissionRequests :LiveData<List<LocationPermissionRequest>> = MutableLiveData()

    init {
        val connectionDao = ConnectionDatabase.getConnectionDatabase(application).connectionDao()
        repository = ConnectionRepository(connectionDao)
        fetchConnections()
        fetchRequests()
        fetchLocationPermissionRequests()
        allConnections = repository.allConnections
        allRequests = repository.allRequests
        allLocations = repository.allLocations
        allLocationPermissionRequests = repository.alLocationPermissionRequests
    }
    fun fetchLocationPermissionRequests(){
        repository.deleteAllLocalLocationPermissionRequests()
        getLocationPermissionRequests(::insertLocalLocationPermissionRequest)

    }

    fun insertLocalLocationPermissionRequest(locationPermissionRequest: LocationPermissionRequest){
        repository.insertLocalLocationPermissionRequest(locationPermissionRequest)
    }

    fun clearAllData() {
        println("clearing data")
        repository.clearAllData()
    }

    fun insertLocalLocation(location: Location){
        println(
            "Insert location in repo viewmodel $location"
        )

        repository.insertLocalLocation(location)
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

    fun deleteLocalLocationPermissionRequest(phone: String) {
        repository.deleteLocationPermissionRequest(LocationPermissionRequest(phone))
    }

    fun deleteConnection(phone: String, onSuccess: (String) -> Unit, onFailed: () -> Unit) {
        com.empyrealgames.findme.firebase.deleteConnection(phone, onSuccess, onFailed)
    }

    fun deleteLocalRequest(phone: String){
        repository.deleteLocalRequest(Request(phone))
    }

    fun acceptRequest(phone: String, onSuccess: (String) -> Unit, onFailed: () -> Unit) {
        com.empyrealgames.findme.firebase.acceptRequest(phone, onSuccess, onFailed)
    }

    fun deleteRequest(phone: String, onSuccess: (String) -> Unit, onFailed: () -> Unit) {
        com.empyrealgames.findme.firebase.deleteRequest(phone, onSuccess, onFailed)
    }

     private fun fetchConnections() {
        repository.deleteAllLocalConnections()
         getConnectionLists(::insertConnection)
    }

    private fun fetchRequests() {
        repository.deleteAllLocalRequests()
        getRequestsLists(::insertRequest)
    }
     fun fetchLocations(connection:String){
        repository.deleteAllLocalLocation()
        getLocationsList(connection, ::insertLocalLocation)
    }

    fun acceptLocationPermissionRequest(phone: String, onSuccess:(String)->Unit, onFailed:()->Unit){
        com.empyrealgames.findme.firebase.acceptLocationPermissionRequest(phone = phone, onSuccess = onSuccess, onFailed = onFailed )
    }

    fun deleteLocationPermissionRequest(phone: String, onSuccess: (String) -> Unit, onFailed:()->Unit){
        com.empyrealgames.findme.firebase.deleteLocationPermissionRequest(phone = phone, onSuccess = onSuccess, onFailed = onFailed)
    }


}

