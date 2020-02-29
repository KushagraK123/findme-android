package com.empyrealgames.findme.dashboard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Connection")
data class Connection(@PrimaryKey var phone:String, var hasLocationPermission:Boolean = false)
