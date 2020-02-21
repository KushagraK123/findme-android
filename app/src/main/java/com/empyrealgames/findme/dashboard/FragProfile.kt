package com.empyrealgames.findme.dashboard

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.empyrealgames.findme.R
import com.empyrealgames.findme.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.frag_profile.*

class FragProfile : Fragment(), View.OnClickListener, FirebaseAuth.AuthStateListener {
    lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_profile, container, false)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.addAuthStateListener(this)
        //show progressbar
        updateData()
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
            R.id.b_logout -> FirebaseAuth.getInstance().signOut()
        }
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}