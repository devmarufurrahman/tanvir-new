package com.example.tanvirhome.network.retrofit

import com.example.tanvirhome.constants.AppConfig
import com.example.tanvirhome.network.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}