package com.adaptixinnovate.tanvirahmedrobin.services

import android.content.Context
import com.adaptixinnovate.tanvirahmedrobin.model.GetSettings
import com.adaptixinnovate.tanvirahmedrobin.model.SiteInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SharedPrefereneService {

    fun saveToPreferences(context: Context, value: String, key: String) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("BaseUrl", value)
        editor.apply() // Asynchronous save
    }

    suspend fun getFromPreferences(
        context: Context,
        key: String,
        defaultValue: String = ""
    ): String {
        return withContext(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            sharedPreferences.getString(key, defaultValue) ?: defaultValue
        }
    }


    fun saveSettingsToPreferences(context: Context, name: String, logo: String, address: String, facebook: String, x: String, youtube: String, copyright: String, email: String, phone: String, instagram: String, website: String ) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("logo", logo)
        editor.putString("address", address)
        editor.putString("facebook", facebook)
        editor.putString("x", x)
        editor.putString("youtube", youtube)
        editor.putString("copyright", copyright)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.putString("instagram", instagram)
        editor.putString("website", website)
        editor.apply() // Asynchronous save
    }


    fun getSettingsFromPreferences(context: Context): GetSettings {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "") ?: ""
        val logo = sharedPreferences.getString("logo", "") ?: ""
        val address = sharedPreferences.getString("address", "") ?: ""
        val facebook = sharedPreferences.getString("facebook", "") ?: ""
        val x = sharedPreferences.getString("x", "") ?: ""
        val youtube = sharedPreferences.getString("youtube", "") ?: ""
        val copyright = sharedPreferences.getString("copyright", "") ?: ""
        val email = sharedPreferences.getString("email", "") ?: ""
        val phone = sharedPreferences.getString("phone", "") ?: ""
        val instagram = sharedPreferences.getString("instagram", "") ?: ""
        val website = sharedPreferences.getString("website", "") ?: ""

        return GetSettings(
            name = name,
            logo = logo,
            address = address,
            facebook = facebook,
            x = x,
            youtube = youtube,
            copyright = copyright,
            email = email,
            phone = phone,
            instagram = instagram,
            website = website,
        )

    }




}