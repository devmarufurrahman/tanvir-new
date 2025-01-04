package com.adaptixinnovate.tanvirahmedrobin.network.retrofit

import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.network.api.ApiService
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