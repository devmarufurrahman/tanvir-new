package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivitySplashScreenBinding
import com.adaptixinnovate.tanvirahmedrobin.services.FirebaseService
import com.adaptixinnovate.tanvirahmedrobin.services.GetData
import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService
import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService.getFromPreferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure the app follows the system's night mode at the start
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // Initialize binding
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        // Check system UI mode and set theme accordingly
//        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
//            Configuration.UI_MODE_NIGHT_YES -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Night Mode
//            }
//            Configuration.UI_MODE_NIGHT_NO -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Light Mode
//            }
//            else -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) // Follow system default
//            }
//        }

        setContentView(binding.root)

        FirebaseService.base_url_firebase(this)


        // Now use binding to reference your views
        binding.splashImage.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )
        binding.splashText.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )



    }

    override fun onStart() {
        super.onStart()
        retrieveBaseUrl()
    }


    private fun proceedToNextActivity() {
        // Handler to wait for 2 seconds and then move to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Finish SplashActivity so user cannot go back to it
        }, 2000)  // 2000 milliseconds = 2 seconds
    }


    private fun retrieveBaseUrl() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Call the suspend function to get BaseUrl
                val baseUrl = getFromPreferences(
                    context = this@SplashScreen,
                    key = "BaseUrl",
                    defaultValue = ""
                )
                if (baseUrl.isNotEmpty()){
                    AppConfig.setUrl(baseUrl)
                    proceedToNextActivity()
                    setupSplash()
                    GetData.fetchSiteInfo(this@SplashScreen)
                } else{
                    recreate()
                }

            } catch (e: Exception) {
                Log.e("BaseUrl", "Error retrieving Base URL: ${e.message}")
            }
        }
    }

    private fun setupSplash() {
        val settings = SharedPrefereneService.getSettingsFromPreferences(this)

        Log.d("settings_log", "setupSplash: $settings")

        Picasso.get()
            .load("${AppConfig.IMG_URL}uploads/${settings.logo}")
//            .placeholder(R.drawable.splash_image)
            .error(R.drawable.splash_image)
            .into(binding.splashImage)

        binding.splashText.text = resources.getString(R.string.app_name, settings.name)

    }
}