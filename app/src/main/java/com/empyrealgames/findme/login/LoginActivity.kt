package com.empyrealgames.findme.login

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.empyrealgames.findme.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.frag_login.*
import java.util.concurrent.TimeUnit




class LoginActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var location:Location? = null
    lateinit var auth:FirebaseAuth
    lateinit var mAuth: FirebaseAuth
    private var storedVerificationId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
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
        auth = FirebaseAuth.getInstance()
    }





    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("login", "signInWithCredential:success")

                    val user = task.result?.user
                    // [START_EXCLUDE]
     //               updateUI(STATE_SIGNIN_SUCCESS, user)
                    // [END_EXCLUDE]
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("login", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        // [START_EXCLUDE silent]
       //                 fieldVerificationCode.error = "Invalid code."
                        // [END_EXCLUDE]
                    }
                    // [START_EXCLUDE silent]
                    // Update UI
         //           updateUI(STATE_SIGNIN_FAILED)
                    // [END_EXCLUDE]
                }
            }
    }





}
