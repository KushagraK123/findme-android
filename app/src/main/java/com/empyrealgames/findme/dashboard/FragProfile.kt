package com.empyrealgames.findme.dashboard

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.data.ConnectionViewModel
import com.empyrealgames.findme.login.LoginActivity
import com.empyrealgames.findme.utils.showLoadingDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.frag_profile.*


const val minimumClickTime = 600L

class FragProfile : Fragment(), View.OnClickListener, FirebaseAuth.AuthStateListener {
    lateinit var mAuth: FirebaseAuth
    lateinit var connectionViewModel: ConnectionViewModel
    private var lastTimeClicked = 0L
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_profile, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        connectionViewModel = ViewModelProvider(activity!!).get(ConnectionViewModel::class.java)
        //show progressbar
        updateData()
        mAuth.addAuthStateListener(this)
    }

    fun updateData() {
        if (mAuth.currentUser != null) {
            tv_mobile.text = mAuth.currentUser!!.phoneNumber
            tv_username.text = mAuth.currentUser!!.displayName
        }
        b_logout.setOnClickListener(this)
        //remove progress bar
        progressBar.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.b_logout -> {
                val currTime = System.currentTimeMillis()
                if(currTime - lastTimeClicked > minimumClickTime) {
                    lastTimeClicked = System.currentTimeMillis()
                    mAuth.signOut()
                }
                lastTimeClicked = currTime
            }
        }
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null && context != null) {
            showLoadingDialog(context!!).show()
            val thread = Thread {
                Thread.sleep(600)
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                context!!.startActivity(intent)
                if (context is Activity) {
                    (context as Activity).finish()
                }
                Runtime.getRuntime().exit(0)
            }
            thread.start()

        }
    }
}