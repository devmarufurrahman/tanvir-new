package com.example.tanvirhome.network.api


import com.example.tanvirhome.model.AboutModel
import com.example.tanvirhome.model.BannerModel
import com.example.tanvirhome.model.ContactModel
import com.example.tanvirhome.model.DataCollectionModel
import com.example.tanvirhome.model.LocationModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("wards")
    fun getWard(): Call<List<LocationModel>>

    @GET("get-thanas/{wardId}")
    fun getThanasByWardId(@Path("wardId") wardId: Int): Call<List<LocationModel>>
}