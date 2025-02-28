package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivitySplashScreenBinding
import com.adaptixinnovate.tanvirahmedrobin.model.GetSettings
import com.adaptixinnovate.tanvirahmedrobin.services.FirebaseService
import com.adaptixinnovate.tanvirahmedrobin.services.GetData
import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService
import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService.getFromPreferences
import com.google.firebase.messaging.FirebaseMessaging
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
        setContentView(binding.root)

        FirebaseService.base_url_firebase(this)
        retrieveBaseUrl()

        // Subscribe to a topic
        FirebaseMessaging.getInstance().subscribeToTopic("general")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Subscribed", "Subscribed to topic: general")
                } else {
                    Toast.makeText(this, "Subscription failed", Toast.LENGTH_SHORT).show()
                    Log.e("Subscribed", "Subscription failed")
                }
            }



        // Now use binding to reference your views
        binding.splashImage.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )
        binding.splashText.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )



    }



    private fun proceedToNextActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }


    private fun retrieveBaseUrl() {
        binding.progressBar.visibility = View.VISIBLE
        val settings = SharedPrefereneService.getSettingsFromPreferences(this)

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
                    GetData.fetchSiteInfo(this@SplashScreen)
                    setupSplash(settings)
                } else{
                    recreate()
                }

            } catch (e: Exception) {
                Log.e("BaseUrl", "Error retrieving Base URL: ${e.message}")
            }
        }
    }

    private fun setupSplash(settings : GetSettings) {

        if (settings.logo.isNotEmpty() && settings.name.isNotEmpty()) {
            binding.progressBar.visibility = View.GONE
            Picasso.get()
                .load("${AppConfig.IMG_URL}uploads/${settings.logo}")
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.load_error)
                .into(binding.splashImage)

            binding.splashText.text = resources.getString(R.string.app_name, settings.name)
            proceedToNextActivity()
        } else {
            binding.progressBar.visibility = View.GONE
            binding.splashText.text = resources.getString(R.string.app_name2)
            binding.splashImage.setImageResource(R.drawable.main_image)
            proceedToNextActivity()
        }



    }
}