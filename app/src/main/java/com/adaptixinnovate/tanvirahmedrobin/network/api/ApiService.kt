package com.adaptixinnovate.tanvirahmedrobin.network.api


import com.adaptixinnovate.tanvirahmedrobin.model.AboutModel
import com.adaptixinnovate.tanvirahmedrobin.model.BannerModel
import com.adaptixinnovate.tanvirahmedrobin.model.ContactModel
import com.adaptixinnovate.tanvirahmedrobin.model.DataCollectionModel
import com.adaptixinnovate.tanvirahmedrobin.model.LocationModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("contact-me")
    fun contactMessage(
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("message") message: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<ResponseBody>


    @Multipart
    @POST("complain")
    fun complainMessage(
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("message") message: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<ResponseBody>


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