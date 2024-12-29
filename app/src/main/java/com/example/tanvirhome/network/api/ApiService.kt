package com.example.tanvirhome.network.api


import com.example.tanvirhome.model.AboutModel
import com.example.tanvirhome.model.BannerModel
import com.example.tanvirhome.model.ContactModel
import com.example.tanvirhome.model.DataCollectionModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("contact-me")
    fun contactMessage(@Body contactModel: ContactModel): Call<ContactModel>

    @POST("complain")
    fun complainMessage(@Body contactModel: ContactModel): Call<ContactModel>

    @POST("data-collection")
    fun postUserData(@Body userData: DataCollectionModel): Call<DataCollectionModel>

    @GET("about-me")
    fun getAboutMe(): Call<List<AboutModel>>

    @GET("banner")
    fun getBanner(): Call<List<BannerModel>>
}