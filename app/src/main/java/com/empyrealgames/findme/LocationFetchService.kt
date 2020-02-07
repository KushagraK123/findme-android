package com.empyrealgames.findme

import android.app.IntentService
import android.content.Intent

class LocationFetchService : IntentService(LocationFetchService::class.simpleName){
    override fun onHandleIntent(intent: Intent?) {
        println("Service started")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Service Destroyed")
    }
}