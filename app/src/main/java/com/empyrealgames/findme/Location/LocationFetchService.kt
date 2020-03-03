package com.empyrealgames.findme.Location

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class LocationFetchService : IntentService(LocationFetchService::class.simpleName){
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onHandleIntent(intent: Intent?) {


        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, LocationReciever::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        alarmMgr?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 1000,
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent
        )


        println("Service started")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Service Destroyed")
    }
}