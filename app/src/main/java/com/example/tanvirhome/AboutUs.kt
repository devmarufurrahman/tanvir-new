package com.example.tanvirhome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tanvirhome.databinding.ActivityAboutUsBinding

class AboutUs : AppCompatActivity() {
    private lateinit var binding: ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the toolbar
        setSupportActionBar(binding.customToolbar)
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.contactMeButton.setOnClickListener {
            val intent = Intent(this, ContactMe::class.java)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }
}