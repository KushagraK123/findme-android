package com.empyrealgames.findme.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ActivityDash
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.frag_login.*

class LoginFrag : Fragment(){
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(FirebaseAuth.getInstance().currentUser!=null) {
            startActivity(Intent(activity, ActivityDash::class.java))
        }
        return inflater.inflate(R.layout.frag_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        b_continue.setOnClickListener {
            val num:String = et_number.text.toString()
            val action = LoginFragDirections.actionLoginFragToOtpFrag(num)
            navController.navigate(action)
        }
    }
}