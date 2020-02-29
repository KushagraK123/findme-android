package com.empyrealgames.findme.dashboard.data


import android.os.AsyncTask
import androidx.lifecycle.LiveData


class ConnectionRepository(private var connectionDao: ConnectionDao) {

    var allConnections: LiveData<List<Connection>> = connectionDao.allConnections()
    var allRequests: LiveData<List<Request>> = connectionDao.allRequests()
    var allLocations: LiveData<List<Location>> = connectionDao.allLocations()

    fun insertLocalConnection(connection: Connection) {
        InsertConnectionAsyncTask(
            connectionDao
        ).execute(connection)
    }

    fun insertLocalRequest(request: Request) {
        InsertRequestAsyncTask(
            connectionDao
        ).execute(request)
    }

    fun deleteAllLocalConnections() {
        DeleteAllConnectionAsyncTask(
            connectionDao
        ).execute(0)
    }

    fun deleteAllLocalRequests() {
        DeleteAllRequestsAsyncTask(
            connectionDao
        ).execute(0)
    }

    fun deleteLocalConnection(connection: Connection) {
        DeleteConnectionAsyncTask(
            connectionDao
        ).execute(connection)

    }

    fun deleteLocalRequest(request: Request) {
        DeleteRequestAsyncTask(
            connectionDao
        ).execute(request)

    }
    fun deleteAllLocalLocation() {
        DeleteAllConnectionAsyncTask(
            connectionDao
        ).execute()
    }


    fun insertLocalLocation(location: Location) {
        InsertAllLocations(
            connectionDao
        ).execute(location)
    }



    private class InsertConnectionAsyncTask(connectionDao: ConnectionDao) :
        AsyncTask<Connection, Void, Int>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Connection?): Int {
            connectionDao!!.insertAllConnections(params[0]!!)
            return 0

        }
    }


    private class DeleteConnectionAsyncTask(connectionDao: ConnectionDao) :
        AsyncTask<Connection, Void, Int>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Connection?): Int {
            connectionDao!!.deleteConnection(params[0]!!)
            return 0

        }
    }

    private class DeleteRequestAsyncTask(connectionDao: ConnectionDao) :
        AsyncTask<Request, Void, Int>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Request?): Int {
            connectionDao!!.deleteRequest(params[0]!!)
            return 0

        }
    }
    private class InsertRequestAsyncTask(connectionDao: ConnectionDao) :
        AsyncTask<Request, Void, Int>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Request?): Int {
            connectionDao!!.insertAllRequests(params[0]!!)
            return 0

        }
    }

    private class DeleteAllConnectionAsyncTask(connectionDao: ConnectionDao) :
        AsyncTask<Int, Void, Int>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Int?): Int {
            connectionDao!!.deleteAllConnections()
            return 0
        }
    }


    private class DeleteAllRequestsAsyncTask(connectionDao: ConnectionDao) :
        AsyncTask<Int, Void, Int>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Int?): Int {
            connectionDao!!.deleteAllRequests()
            return 0
        }
    }


    private class DeleteAllLocationsAsyncTask(connectionDao: ConnectionDao) :
        AsyncTask<Unit, Unit, Unit>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Unit?) {
            connectionDao!!.deleteAllLocations()
        }
    }

    private class InsertAllLocations(connectionDao: ConnectionDao) :
        AsyncTask<Location, Unit, Unit>() {
        var connectionDao: ConnectionDao? = null

        init {
            this.connectionDao = connectionDao
        }

        override fun doInBackground(vararg params: Location?) {
            connectionDao!!.insertAllLocations(params[0]!!)
        }
    }


}
