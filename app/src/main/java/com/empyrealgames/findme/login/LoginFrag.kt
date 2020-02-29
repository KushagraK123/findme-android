package com.empyrealgames.findme.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.empyrealgames.findme.R
import kotlinx.android.synthetic.main.frag_login.*

class LoginFrag : Fragment(){
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.frag_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        b_continue.setOnClickListener {
            val num:String = et_number.editText!!.text.toString()
            val action = LoginFragDirections.actionLoginFragToOtpFrag(num)
            navController.navigate(action)
        }
        et_number.editText!!.addTextChangedListener {

            b_continue.isEnabled = !(it == null || it.length <10)

        }
    }

}