package com.empyrealgames.findme.dashboard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class Location(@PrimaryKey var time:String="", var lat:String="", var long:String="")
