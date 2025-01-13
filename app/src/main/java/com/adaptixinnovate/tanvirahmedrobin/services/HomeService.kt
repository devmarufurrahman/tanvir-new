package com.adaptixinnovate.tanvirahmedrobin.services

import android.content.Context
import android.content.Intent
import android.util.Log
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.adapter.Service
import com.adaptixinnovate.tanvirahmedrobin.ui.AboutUs
import com.adaptixinnovate.tanvirahmedrobin.ui.ComplainUs
import com.adaptixinnovate.tanvirahmedrobin.ui.ContactMe
import com.adaptixinnovate.tanvirahmedrobin.ui.DataCollectionActivity
import com.adaptixinnovate.tanvirahmedrobin.ui.ExtortionActivity
import com.adaptixinnovate.tanvirahmedrobin.ui.GalleryActivity

object HomeService {

    fun handleServiceClick(service: Service, context: Context) {
        when (service.name) {
            context.getString(R.string.data_collection_form) -> {
                val intent = Intent(context, DataCollectionActivity::class.java)
                context.startActivity(intent)
            }
            context.getString(R.string.contact_me) -> {
                val intent = Intent(context, ContactMe::class.java)
                context.startActivity(intent)
            }
            context.getString(R.string.complain_us) -> {
                val intent = Intent(context, ComplainUs::class.java)
                context.startActivity(intent)
            }
            context.getString(R.string.about_me) -> {
                val intent = Intent(context, AboutUs::class.java)
                context.startActivity(intent)
            }
            context.getString(R.string.extortion) -> {
                val intent = Intent(context, ExtortionActivity::class.java)
                context.startActivity(intent)
            }
            context.getString(R.string.gallery) -> {
                val intent = Intent(context, GalleryActivity::class.java)
                context.startActivity(intent)
            }
            else -> {
                // Default action or a message
                Log.d("ServiceClick", "Unknown service: ${service.name}")
            }
        }
    }
}