package com.adaptixinnovate.tanvirahmedrobin.services

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import android.widget.Toast
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.model.BannerModel
import com.adaptixinnovate.tanvirahmedrobin.model.LocationModel
import com.adaptixinnovate.tanvirahmedrobin.model.SiteInfo
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.utils.SetupDropDown
import com.denzcoskun.imageslider.models.SlideModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GetData {

    fun showBanner(context: Context, imageSlider: com.denzcoskun.imageslider.ImageSlider ) {

        val imageList = ArrayList<SlideModel>() // Create image list

        RetrofitClient.instance.getBanner().enqueue(object : Callback<List<BannerModel>> {
            override fun onResponse(call: Call<List<BannerModel>>, response: Response<List<BannerModel>>) {
                if (response.isSuccessful) {
                    val bannerList = response.body() ?: emptyList()

                    // Filter the first 'Active' item
                    val activeBanner = bannerList.filter { it.status == "Active" }

                    if (activeBanner.isNotEmpty()) {

                        for (banner in activeBanner) {
                            imageList.add(SlideModel("${AppConfig.IMG_URL}${banner.image}", banner.title))
                        }


                    } else {
                        // Handle the case where no active items are found
                        Toast.makeText(context, "Banner not found.", Toast.LENGTH_SHORT).show()

                    }
                    imageSlider.startSliding(2000)
                    imageSlider.setImageList(imageList)
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

    fun fetchSiteInfo(context: Context) {
        RetrofitClient.instance.getSettings().enqueue(object : Callback<List<SiteInfo>> {
            override fun onResponse(call: Call<List<SiteInfo>>, response: Response<List<SiteInfo>>) {
                if (response.isSuccessful) {
                    val siteInfoList = response.body()
                    siteInfoList?.let {
                        for (info in it) {
                            info.logo?.let { logo ->
                                SharedPrefereneService.saveSettingsToPreferences(
                                    context,
                                    info.site_name ?: "",
                                    logo,
                                    info.address ?: "",
                                    info.facebook ?: "",
                                    info.linkedin ?: "",
                                    info.youtube ?: "",
                                    info.footer_text ?: "",
                                    info.email ?: "",
                                    info.phone ?: ""
                                )
                            }
                            Log.d("SiteInfo", "Site : $info")
                            Log.d("SiteInfo", "Facebook: ${info.facebook}")
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SiteInfo>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}