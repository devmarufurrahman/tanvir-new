package com.example.tanvirhome.network.api


import com.example.tanvirhome.model.ContactModel
import com.example.tanvirhome.model.DataCollectionModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("your-endpoint")
    fun contactMessage(@Body contactModel: ContactModel): Call<ContactModel>

    @POST("new-endpoint")
    fun postUserData(@Body userData: DataCollectionModel): Call<DataCollectionModel>
}