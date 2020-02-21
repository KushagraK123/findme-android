package com.empyrealgames.findme.dashboard.connection


import android.os.AsyncTask
import androidx.lifecycle.LiveData


class ConnectionRepository(private var connectionDao: ConnectionDao) {

    var allConnections: LiveData<List<Connection>> = connectionDao.allConnections()
    var allRequests: LiveData<List<Request>> = connectionDao.allRequests()


    fun insertConnection(connection: Connection) {
        InsertConnectionAsyncTask(
            connectionDao
        ).execute(connection)
    }

    fun insertRequest(request: Request) {
        InsertRequestAsyncTask(
            connectionDao
        ).execute(request)
    }

    fun deleteAllConnections() {
        DeleteAllConnectionAsyncTask(
            connectionDao
        ).execute(0)
    }

    fun deleteAllRequests() {
        DeleteAllRequestsAsyncTask(
            connectionDao
        ).execute(0)
    }

    fun deleteConnection(connection: Connection) {
        DeleteConnectionAsyncTask(
            connectionDao
        ).execute(connection)

    }

    fun deleteRequest(request: Request) {
        DeleteRequestAsyncTask(
            connectionDao
        ).execute(request)

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


}
