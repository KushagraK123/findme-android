package com.empyrealgames.findme.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ActivityDash
import com.empyrealgames.findme.firebase.isUserLoggedIn
import com.empyrealgames.findme.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val b = isUserLoggedIn()
        val thread = Thread{
            Thread.sleep(500)
            if(b){
                startActivity(Intent(this, ActivityDash::class.java).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }else{
                startActivity(Intent(this, LoginActivity::class.java).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))

            }

        }
        thread.start()



    }

}
