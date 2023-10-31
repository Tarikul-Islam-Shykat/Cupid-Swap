package com.example.cupidswap.Screens.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.cupidswap.MainActivity
import com.example.cupidswap.R
import com.example.cupidswap.Screens.Dashboard.Dashboard
import com.example.cupidswap.databinding.ActivityLoginBinding
import com.example.cupidswap.utils.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class Login : AppCompatActivity() {

    private  lateinit var  binding: ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()
    private  var verificationId: String? = null
    /*private lateinit var dialog : AlertDialog*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* dialog = AlertDialog
            .Builder(this@Login)
            .setView(R.layout.sample_loading_dialog_layout)
            .setCancelable(false)
            .create()*/
        
        binding.btnSendOTP.setOnClickListener {
            Utils.dismissKeyboard(this@Login)

            if(binding.edtUserNumber.text!!.isEmpty())
                binding.edtUserNumber.error = "Please Enter your Number"
            else{
                binding.btnSendOTP.startAnimation()
                sendOtp(binding.edtUserNumber.text.toString())
            }
        }

        binding.btnVerifyOTP.setOnClickListener {
            Utils.dismissKeyboard(this@Login)
            if(binding.edtUserOTP.text!!.isEmpty())
                binding.edtUserOTP.error = "Please Enter your Number"
            else {
                binding.btnVerifyOTP.startAnimation()
                verifyOTP(binding.edtUserOTP.text.toString())
            }
        }

    }


    private fun verifyOTP(userOTP: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, userOTP)
        signInWithPhoneAuthCredential(credential)
    }



    private fun sendOtp(userNumber: String) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken, ) {
                this@Login.verificationId = verificationId
                stopSendOTPAnimation()

                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+88$userNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    stopVerifyOTPAnimation()
                    checkUserExist(binding.edtUserNumber.text.toString())
                } else {
                    stopSendOTPAnimation()
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun checkUserExist(userNumber: String) {
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    stopVerifyOTPAnimation()
                    if(snapshot.exists()){
                        startActivity(Intent(this@Login, Dashboard::class.java))
                        finish()
                    }
                    else{
                        startActivity(Intent(this@Login, Registration::class.java))
                        finish()
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    stopVerifyOTPAnimation()
                    Toast.makeText(applicationContext, "Please Register First", Toast.LENGTH_SHORT).show()
                    finish()
                }

            })

    }



    private fun stopSendOTPAnimation(){
        val bitmap = getDrawable(R.drawable.baseline_done_24)?.toBitmap()
        val color = ContextCompat.getColor(applicationContext, R.color.primary )
        binding.btnSendOTP.doneLoadingAnimation(color, bitmap!!)

    }



    private fun stopVerifyOTPAnimation(){
        val bitmap = getDrawable(R.drawable.baseline_done_24)?.toBitmap()
        val color = ContextCompat.getColor(applicationContext, R.color.primary )
        binding.btnVerifyOTP.doneLoadingAnimation(color, bitmap!!)
    }






}