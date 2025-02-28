package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityAboutUsBinding
import com.adaptixinnovate.tanvirahmedrobin.model.AboutModel
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.services.SharedPrefereneService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val settings = SharedPrefereneService.getSettingsFromPreferences(this)
        Picasso.get()
            .load("${AppConfig.IMG_URL}uploads/${settings.logo}")
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.load_error)
            .into(binding.personImg)



        binding.swipeRefreshLayout.setOnRefreshListener {
            recreate()
            // Code to refresh the content goes here

            binding.swipeRefreshLayout.isRefreshing = false
        }

        RetrofitClient.instance.getAboutMe().enqueue(object : Callback<List<AboutModel>> {
            override fun onResponse(call: Call<List<AboutModel>>, response: Response<List<AboutModel>>) {
                if (response.isSuccessful) {
                    val aboutList = response.body() ?: emptyList()

                    // Find the first 'Active' item
                    val activeAbout = aboutList.find { it.status == "Active" }

                    if (activeAbout != null) {
                        showData(activeAbout)
                    } else {
                        // Handle the case where no active items are found
                        Toast.makeText(this@AboutUs, "No active data found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle API errors here
                }
            }

            override fun onFailure(call: Call<List<AboutModel>>, t: Throwable) {
                // Handle the error, e.g., network error or request failure
                Toast.makeText(this@AboutUs, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ërror", "onFailure:  ${t.message}")
            }
        })


        binding.contactMeButton.setOnClickListener {
            val intent = Intent(this, ContactMe::class.java)
            startActivity(intent)
        }

    }

    private fun showData(data: AboutModel) {
        // Display or use the data in your app
        binding.name.text = data.title
        binding.role.text = data.sub_title
        binding.about.text = data.description
    }

    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }
}