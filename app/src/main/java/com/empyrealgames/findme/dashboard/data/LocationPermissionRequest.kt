package com.empyrealgames.findme.dashboard.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationPermissionRequest" )
data class LocationPermissionRequest(@PrimaryKey var phone:String)
