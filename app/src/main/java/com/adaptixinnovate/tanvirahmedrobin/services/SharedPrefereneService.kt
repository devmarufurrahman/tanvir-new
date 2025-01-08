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


    fun saveSettingsToPreferences(context: Context, name: String, logo: String, address: String, facebook: String, linkedin: String, youtube: String, copyright: String, email: String, phone: String ) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("logo", logo)
        editor.putString("address", address)
        editor.putString("facebook", facebook)
        editor.putString("linkedin", linkedin)
        editor.putString("youtube", youtube)
        editor.putString("copyright", copyright)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.apply() // Asynchronous save
    }


    fun getSettingsFromPreferences(context: Context): GetSettings {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "") ?: ""
        val logo = sharedPreferences.getString("logo", "") ?: ""
        val address = sharedPreferences.getString("address", "") ?: ""
        val facebook = sharedPreferences.getString("facebook", "") ?: ""
        val linkedin = sharedPreferences.getString("linkedin", "") ?: ""
        val youtube = sharedPreferences.getString("youtube", "") ?: ""
        val copyright = sharedPreferences.getString("copyright", "") ?: ""
        val email = sharedPreferences.getString("email", "") ?: ""
        val phone = sharedPreferences.getString("phone", "") ?: ""

        return GetSettings(
            name = name,
            logo = logo,
            address = address,
            facebook = facebook,
            linkedin = linkedin,
            youtube = youtube,
            copyright = copyright,
            email = email,
            phone = phone
        )

    }




}