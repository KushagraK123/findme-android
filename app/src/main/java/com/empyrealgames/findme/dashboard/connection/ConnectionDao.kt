package com.empyrealgames.findme.dashboard.connection

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ConnectionDao {
    @Query("SELECT * FROM Connection ")
    fun allConnections ():LiveData<List<Connection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllConnections(vararg connection: Connection)

    @Delete
    fun deleteConnection(vararg connection: Connection)


    @Query("DELETE  FROM Connection")
    fun deleteAllConnections()


    @Query("SELECT * FROM Request ")
    fun allRequests ():LiveData<List<Request>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRequests(vararg requests: Request)

    @Delete
    fun deleteRequest(vararg request: Request)


    @Query("DELETE  FROM Request")
    fun deleteAllRequests()

}
