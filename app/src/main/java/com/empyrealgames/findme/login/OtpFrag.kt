package com.empyrealgames.findme.login


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.empyrealgames.findme.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.frag_signup.*
import java.util.concurrent.TimeUnit


class OtpFrag : Fragment() {
    lateinit var navController: NavController
    val args: OtpFragArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return  inflater.inflate(R.layout.frag_enter_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        b_submit_otp.setOnClickListener { navController.navigate(R.id.action_otpFrag_to_fragSignUp) }
        verifyNumber("+91-" + args.number.toString())
    }

    fun verifyNumber(number: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS,
            activity!!,
            callbacks
        )
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("login", "onVerificationCompleted:$credential")
//            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("login", "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...1
            }

            // Show a message and update the UI
            // ...
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
           Toast.makeText(context, "Code is sent", Toast.LENGTH_LONG).show()
         //   storedVerificationId = verificationId
            // Save verification ID and resending token so we can use them later
            /*storedVerificationId = verificationId
            resendToken = token
            */
            // ...
        }
    }



}
