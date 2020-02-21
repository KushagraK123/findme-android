package com.empyrealgames.findme.dashboard.connection

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Request" )
data class Request(@PrimaryKey var phone:String)
