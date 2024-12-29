package com.example.tanvirhome.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tanvirhome.databinding.ActivityAboutUsBinding
import com.example.tanvirhome.model.AboutModel
import com.example.tanvirhome.network.retrofit.RetrofitClient
import com.example.tanvirhome.services.GetData
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