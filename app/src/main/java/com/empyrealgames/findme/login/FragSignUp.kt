package com.empyrealgames.findme.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ActivityDash
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.frag_signup.*

class FragSignUp : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
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
        if (mAuth.currentUser != null) {
            val update = UserProfileChangeRequest.Builder()
                .setDisplayName(et_name.text.toString() + " " + et_lastname.text.toString()).build()
            mAuth.currentUser!!.updateProfile(update).addOnCompleteListener {
                if (it.isSuccessful) {
                  val user = hashMapOf("uid" to mAuth.currentUser!!.uid,
                      "username" to mAuth.currentUser!!.displayName)
                    db.collection("users").document(mAuth.currentUser!!.phoneNumber!!).set(user, SetOptions.merge())
                        .addOnSuccessListener { documentReference ->
                            setUpPrefs()
                            startActivity(Intent(activity, ActivityDash::class.java).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))

                        }
                        .addOnFailureListener { e ->
                            println("Error adding document"  +  e)
                            mAuth.signOut()

                        }
                }
            }
        }
    }
    fun setUpPrefs(){
        val preferenceManager = PreferenceManager()
        preferenceManager.setUserName(mAuth.currentUser!!.displayName!!, context!!)
        preferenceManager.setPhone(mAuth.currentUser!!.phoneNumber!!, context!!)
    }
}