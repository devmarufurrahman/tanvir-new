package com.example.tanvirhome.services

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.tanvirhome.R
import com.example.tanvirhome.constants.AppConfig
import com.example.tanvirhome.model.AboutModel
import com.example.tanvirhome.model.BannerModel
import com.example.tanvirhome.model.LocationModel
import com.example.tanvirhome.network.retrofit.RetrofitClient
import com.example.tanvirhome.utils.SetupDropDown
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GetData {

    fun showBanner(context: Context, textView: TextView, imageView: ImageView) {

        RetrofitClient.instance.getBanner().enqueue(object : Callback<List<BannerModel>> {
            override fun onResponse(call: Call<List<BannerModel>>, response: Response<List<BannerModel>>) {
                if (response.isSuccessful) {
                    val bannerList = response.body() ?: emptyList()

                    // Find the first 'Active' item
                    val activeBanner = bannerList.find { it.status == "Active" }

                    if (activeBanner != null) {
                        textView.text = activeBanner.title
                        // Load image from a URL into imageView using Picasso
                        Picasso.get()
                            .load(AppConfig.IMG_URL + activeBanner.image)
//                            .placeholder(R.drawable.banner) // Optional: shown while the image is loading
//                            .error(R.drawable.error_image) // Optional: shown if there's an error loading the image
                            .into(imageView)
                    } else {
                        // Handle the case where no active items are found
                        Toast.makeText(context, "Banner not found.", Toast.LENGTH_SHORT).show()
                        textView.visibility = View.GONE
                        imageView.visibility = View.GONE
                    }
                } else {
                    // Handle API errors here
                    Toast.makeText(context, "Error: server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BannerModel>>, t: Throwable) {
                // Handle the error, e.g., network error or request failure
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ërror", "onFailure:  ${t.message}")
            }
        })

    }

   fun fetchWards(context: Context, wardView: AutoCompleteTextView, thanaView: AutoCompleteTextView) {
        RetrofitClient.instance.getWard().enqueue(object : retrofit2.Callback<List<LocationModel>> {
            override fun onResponse(call: retrofit2.Call<List<LocationModel>>, response: retrofit2.Response<List<LocationModel>>) {
                if (response.isSuccessful) {
                    val wardsList = response.body() ?: emptyList()
                    val dropDown = SetupDropDown()
                    dropDown.setupDropdown(wardView, wardsList, context) { wardId ->
                        // Handle the selected ward ID here
                        println("Selected Ward ID: $wardId")
                        loadThanas(wardId, context, thanaView)
                    }

                } else {
                    println(response)
                    Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<LocationModel>>, t: Throwable) {
                println("Error: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loadThanas(wardId: Int, context: Context, inputView: AutoCompleteTextView) {
        RetrofitClient.instance.getThanasByWardId(wardId).enqueue(object : Callback<List<LocationModel>> {
            override fun onResponse(call: Call<List<LocationModel>>, response: Response<List<LocationModel>>) {
                if (response.isSuccessful) {
                    val thanaList = response.body() ?: emptyList()
                    val dropDown = SetupDropDown()
                    dropDown.setupDropdown(inputView, thanaList, context) { thanaId ->
                        // Handle the selected ward ID here
                        println("Selected Thana ID: $thanaId")
                    }

                } else {
                    Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<LocationModel>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}