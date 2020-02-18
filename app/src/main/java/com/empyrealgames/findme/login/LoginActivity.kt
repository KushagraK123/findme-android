package com.empyrealgames.findme.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.empyrealgames.findme.R



class LoginActivity : AppCompatActivity() {
   // private lateinit var fusedLocationClient: FusedLocationProviderClient
    //var location:Location? = null
    //  private var storedVerificationId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    /*    startService(Intent(this, LocationFetchService::class.java))
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                }

             else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION),                   1)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                this.location = location
            }

        button.setOnClickListener {
            if(location!=null)
            locationText.text = (location!!.latitude.toString() + " " + location!!.longitude.toString())
            else{
                locationText.text = "555"
            }
        }*/
     /*   b_submit_number.setOnClickListener {  }
        b_submit_otp.setOnClickListener { verifyPhoneNumberWithCode(storedVerificationId!!, et_otp.text.toString()) }*/
    }










}
