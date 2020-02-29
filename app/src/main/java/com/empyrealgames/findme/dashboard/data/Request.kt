package com.empyrealgames.findme.dashboard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Request" )
data class Request(@PrimaryKey var phone:String)
