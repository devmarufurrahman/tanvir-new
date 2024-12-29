package com.example.tanvirhome.services

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.tanvirhome.constants.AppConfig
import com.example.tanvirhome.model.AboutModel
import com.example.tanvirhome.model.BannerModel
import com.example.tanvirhome.network.retrofit.RetrofitClient
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
                        Toast.makeText(context, "No active data found.", Toast.LENGTH_SHORT).show()
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




}