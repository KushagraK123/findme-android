package com.empyrealgames.findme.Location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import com.empyrealgames.findme.firebase.updateLocation
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*


class LocationReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) { //register the alarm manager.
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    updateLocation(
                        SimpleDateFormat("dd-MM-0 HH:mm:ss", Locale.US).format(Date()),
                        location.longitude.toString(),
                        location.latitude.toString()
                    )
                }
            }.addOnFailureListener {
                println("failed " + it.message)

            }

        println("triggered alarm")
    }
}