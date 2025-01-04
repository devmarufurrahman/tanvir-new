package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize binding
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Now use binding to reference your views
        binding.splashImage.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )
        binding.splashText.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )

        // Handler to wait for 2 seconds and then move to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Finish SplashActivity so user cannot go back to it
        }, 2000)  // 2000 milliseconds = 2 seconds

    }
}