package com.empyrealgames.findme.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ActivityDash
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.frag_enter_otp.*
import java.util.concurrent.TimeUnit


class OtpFrag : Fragment() {
    lateinit var navController: NavController
    val args: OtpFragArgs by navArgs()
    lateinit var mAuth: FirebaseAuth
    lateinit var storeVerificationId:String
    lateinit var credential:PhoneAuthCredential

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return  inflater.inflate(R.layout.frag_enter_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()


        et_otp.addTextChangedListener {
            b_submit_otp.isEnabled = !(it == null || it.length <6)

        }
        verifyNumber("+91-" + args.number)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        println("signing  in user")
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                    println("signed in user")
                if (mAuth.currentUser != null) {
                    val user = mAuth.currentUser
                    if (user!!.displayName == "" || user.displayName.isNullOrEmpty()) {
                        println("user is not prsent")
                        findNavController().navigate(R.id.action_otpFrag_to_fragSignUp)
                    } else {
                        setUpPrefs()
                    }
                }

            }.addOnFailureListener {
                println("failed otp ${it.cause} ${it.message} $it")
            }

    }
    fun verifyNumber(number: String) {
        println("inside verify number")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS,
            activity!!,
            callbacks
        )
        println("called verifyNumber" + number)
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            mAuth.signInWithCredential(credential).addOnSuccessListener {
                signInWithPhoneAuthCredential(credential)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(context, "failed $e", Toast.LENGTH_LONG).show()
            println("${e.message} ${e.cause}")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            println("onCodeSent")
           Toast.makeText(context, "Code is sent", Toast.LENGTH_LONG).show()
            storeVerificationId = verificationId
            b_submit_otp.setOnClickListener {
                credential =
                    PhoneAuthProvider.getCredential(storeVerificationId, et_otp.text.toString())
                signInWithPhoneAuthCredential(credential)
                //navController.navigate(R.id.action_otpFrag_to_fragSignUp)
            }
        }

    }

    fun setUpPrefs(){
        if (context != null) {
            startActivity(
                Intent(
                    context,
                    ActivityDash::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }
}
