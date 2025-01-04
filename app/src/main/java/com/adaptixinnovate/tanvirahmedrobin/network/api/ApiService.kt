package com.adaptixinnovate.tanvirahmedrobin.network.api


import com.adaptixinnovate.tanvirahmedrobin.model.AboutModel
import com.adaptixinnovate.tanvirahmedrobin.model.BannerModel
import com.adaptixinnovate.tanvirahmedrobin.model.ContactModel
import com.adaptixinnovate.tanvirahmedrobin.model.DataCollectionModel
import com.adaptixinnovate.tanvirahmedrobin.model.LocationModel
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