package com.adaptixinnovate.tanvirahmedrobin.services

import android.content.Context
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



}