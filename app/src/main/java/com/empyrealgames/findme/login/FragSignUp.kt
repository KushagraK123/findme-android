package com.empyrealgames.findme.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ActivityDash
import com.empyrealgames.findme.firebase.createAccount
import com.empyrealgames.findme.pref.PreferenceManager
import com.empyrealgames.findme.showLoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.frag_signup.*

class FragSignUp : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var dialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        b_signup.setOnClickListener { signupUser() }

    }

    fun signupUser() {
        createAccount(context!!, et_name.text.toString(), et_lastname.text.toString(), ::onSuccessCreateAccount, ::onFailedCreateAccount)
        dialog = showLoadingDialog(context!!)
        dialog.show()
    }

    fun onSuccessCreateAccount(){
        startActivity(Intent(activity, ActivityDash::class.java).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        dialog.dismiss()
    }

    fun onFailedCreateAccount(){
        dialog.dismiss()
        Toast.makeText(context!!, "An error encountered while creating account!", Toast.LENGTH_SHORT).show()
    }

}