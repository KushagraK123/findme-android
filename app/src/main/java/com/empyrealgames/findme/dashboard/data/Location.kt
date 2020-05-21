package com.empyrealgames.findme.dashboard.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class Location(@PrimaryKey var time:String="",
                    @ColumnInfo(name = "longitude") var longitude:String="",
                    @ColumnInfo(name = "lat") var lat:String="",
                    @ColumnInfo(name = "phone") var phone:String=""
                    )
