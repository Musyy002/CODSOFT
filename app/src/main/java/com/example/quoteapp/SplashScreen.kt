package com.example.quoteapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.quoteapp.MainActivity
import com.example.quoteapp.R

class SplashScreen : AppCompatActivity() {

    private lateinit var typingTextView: TextView
    private val textToType = "The Quote App"
    private var currentIndex = 0
    private val delayMillis: Long = 200

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)

        // Lottie animation logic
        Handler(Looper.getMainLooper()).postDelayed({
            val anim = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
            anim.visibility = View.GONE
        }, 5000)

        typingTextView = findViewById(R.id.typingTextView)

        startTypingAnimation()
    }

    private fun startTypingAnimation() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (currentIndex <= textToType.length) {
                    typingTextView.text = textToType.substring(0, currentIndex++)
                    handler.postDelayed(this, delayMillis)
                } else {
                    // Check if the user is new or returning and navigate accordingly
                    checkUserStatus()
                }
            }
        }
        handler.postDelayed(runnable, delayMillis)
    }

    // Check if the user is new or returning
    private fun checkUserStatus() {
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        if (isFirstTime) {
            // New user, open InfoActivity
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstTime", false)
            editor.apply()

            // Open InfoActivity
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        } else {
            // Existing user, open MainActivity
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        }

        // Finish SplashScreen activity
        finish()
    }
}
