package com.empyrealgames.findme.dashboard.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Connection")
data class Connection(@PrimaryKey var phone:String, var hasLocationPermission:Boolean = false):Serializable
