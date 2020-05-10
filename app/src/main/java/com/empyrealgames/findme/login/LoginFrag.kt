package com.empyrealgames.findme.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ActivityDash
import com.empyrealgames.findme.databinding.FragLoginBinding
import com.empyrealgames.findme.utils.showLoadingDialog
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFrag : Fragment(){

    lateinit var binding: FragLoginBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var storeVerificationId: String
    lateinit var credential: PhoneAuthCredential
    private val loadingDialog: AlertDialog by lazy {
        showLoadingDialog(context!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragLoginBinding.bind(view)
        showLoginView()
        mAuth = FirebaseAuth.getInstance()


        binding.apply {

            loginView.bContinue.setOnClickListener {
                loadingDialog.show()
                sendCode(binding.loginView.etNumber.editText!!.text.toString())
            }

            otpView.etOtp.addTextChangedListener {
                binding.otpView.bSubmitOtp.isEnabled = !(it == null || it.length < 6)
            }

            loginView.etNumber.editText!!.addTextChangedListener {
                binding.loginView.bContinue.isEnabled = !(it == null || it.length < 10)
            }

            otpView.bSubmitOtp.setOnClickListener {
                loadingDialog.show()
                credential =
                    PhoneAuthProvider.getCredential(
                        storeVerificationId,
                        otpView.etOtp.text.toString()
                    )
                signInWithPhoneAuthCredential(credential)
            }

        }
    }


    fun showLoginView() {
        binding.apply {
            loginView.root.visibility = View.VISIBLE
            otpView.root.visibility = View.GONE
        }
    }

    fun showOtpView() {
        binding.apply {
            loginView.root.visibility = View.GONE
            otpView.root.visibility = View.VISIBLE
            otpView.etOtp.requestFocus()
        }
    }

    fun sendCode(phone: String) {
        verifyNumber("+91-$phone")
    }

    fun verifyNumber(number: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            activity!!,
            callbacks
        )
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            mAuth.signInWithCredential(credential).addOnSuccessListener {
                signInWithPhoneAuthCredential(credential)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            //when otp is not send due to some error
            if(loadingDialog.isShowing)
                loadingDialog.dismiss()
            println("${e.message} ${e.cause}")
            when (e) {
                is FirebaseTooManyRequestsException -> {
                    Toast.makeText(
                        context,
                        "We have detected unusual activity from your device, please try logging in after sometime!",
                        Toast.LENGTH_LONG
                    ).show()
                    TODO() //show dialog instread of toast
                }
                else -> {
                    Toast.makeText(context, "something wrong", Toast.LENGTH_LONG).show()
                }

            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            if(loadingDialog.isShowing)
                loadingDialog.dismiss()
            Toast.makeText(
                context,
                "Otp has been sent successfully to your device",
                Toast.LENGTH_LONG
            ).show()
            storeVerificationId = verificationId
            showOtpView()
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                if (mAuth.currentUser != null) {
                    val user = mAuth.currentUser
                    if (user!!.displayName == "" || user.displayName.isNullOrEmpty()) {
                        println("user is not prsent")
                        findNavController().navigate(R.id.action_loginFrag_to_fragSignUp)
                    } else {
                        setUpPrefs()
                    }
                }

            }.addOnFailureListener {
                if(loadingDialog.isShowing)
                    loadingDialog.dismiss()
                when (it) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(context, "Wrong Otp", Toast.LENGTH_LONG).show()
                    }
                    is FirebaseTooManyRequestsException -> {
                        Toast.makeText(context, "Device banned", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(context, "something wrong", Toast.LENGTH_LONG).show()
                    }

                }
            }

    }

    fun setUpPrefs() {
        if (context != null) {
            startActivity(
                Intent(
                    context,
                    ActivityDash::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }


    fun resendOtp() {
        TODO() //implement resend
    }


}