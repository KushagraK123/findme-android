package com.empyrealgames.findme.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ActivityDash
import com.empyrealgames.findme.login.LoginActivity
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preferenceManager = PreferenceManager()
        mAuth = FirebaseAuth.getInstance()
        val b:Boolean = mAuth.currentUser!=null
        if(b){
            preferenceManager.setPhone(mAuth.currentUser!!.phoneNumber!!, applicationContext)
            preferenceManager.setUserName(mAuth.currentUser!!.displayName!!, applicationContext)
        }
        val thread = Thread{
            Thread.sleep(1000)
            if(b){
                startActivity(Intent(this, ActivityDash::class.java).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }else{
                startActivity(Intent(this, LoginActivity::class.java).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))

            }

        }
        thread.start()



    }

}
