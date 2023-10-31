package com.example.cupidswap.Screens.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.cupidswap.MainActivity
import com.example.cupidswap.R
import com.example.cupidswap.Screens.Dashboard.Dashboard
import com.example.cupidswap.Screens.auth.Login
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        val user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            when (user){
                null -> startActivity(Intent(this, Login::class.java))
                else -> startActivity(Intent(this, Dashboard::class.java))
            }
            finish()
        }, 2000)
    }
}