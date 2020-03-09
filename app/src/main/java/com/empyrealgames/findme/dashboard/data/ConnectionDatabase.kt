package com.empyrealgames.findme.dashboard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Connection::class, Request::class, Location::class], version = 4)
abstract class ConnectionDatabase : RoomDatabase() {


    abstract fun connectionDao(): ConnectionDao

    companion object {
        private var INSTANCE: ConnectionDatabase? = null

        private val roomCallBack = object : RoomDatabase.Callback() {

        }

        fun getConnectionDatabase(context: Context): ConnectionDatabase {

            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConnectionDatabase::class.java,
                    "connection_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }




}
