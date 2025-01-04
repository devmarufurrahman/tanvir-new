package com.adaptixinnovate.tanvirahmedrobin.services

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.adaptixinnovate.tanvirahmedrobin.model.ContactModel
import com.adaptixinnovate.tanvirahmedrobin.model.DataCollectionModel
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient

object SendData {
    // Function to send the message
    fun contactMessage(name: String, phone: String, message: String, uri: Uri?, context: Context, progressbar: ProgressBar) {

        // Create the model
        val userMessage = ContactModel(name, phone, message, uri)

        // Post data using Retrofit
        val call = RetrofitClient.instance.contactMessage(userMessage)
        call.enqueue(object : retrofit2.Callback<ContactModel> {
            override fun onResponse(
                call: retrofit2.Call<ContactModel>,
                response: retrofit2.Response<ContactModel>
            ) {
                if (response.isSuccessful) {
                    progressbar.visibility = View.GONE
                    // Handle success
                    Toast.makeText(context, "Message sent successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    progressbar.visibility = View.GONE
                    // Handle API error
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ContactModel>, t: Throwable) {
                progressbar.visibility = View.GONE
                // Handle failure
                Toast.makeText(context, "Failed to send message: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    // Function to send the message
    fun complainMessage(name: String, phone: String, message: String, uri: Uri?, context: Context, progressbar: ProgressBar) {

        // Create the model
        val userMessage = ContactModel(name, phone, message, uri)

        // Post data using Retrofit
        val call = RetrofitClient.instance.complainMessage(userMessage)
        call.enqueue(object : retrofit2.Callback<ContactModel> {
            override fun onResponse(
                call: retrofit2.Call<ContactModel>,
                response: retrofit2.Response<ContactModel>
            ) {
                if (response.isSuccessful) {
                    progressbar.visibility = View.GONE
                    // Handle success
                    Toast.makeText(context, "Message sent successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    progressbar.visibility = View.GONE
                    // Handle API error
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ContactModel>, t: Throwable) {
                progressbar.visibility = View.GONE
                // Handle failure
                Toast.makeText(context, "Failed to send message: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    
    fun dataCollection(name: String, fatherName: String, motherName: String, nid: String, dateOfBirth: String, address: String, wardName: String, thanaName: String, mobile: String, email: String, context: Context, progressbar: ProgressBar){
        
        val userDetails = DataCollectionModel(
            name, fatherName, motherName, nid, dateOfBirth, address, wardName, thanaName, mobile, email
        )

        RetrofitClient.instance.postUserData(userDetails).enqueue(object : retrofit2.Callback<DataCollectionModel> {
            override fun onResponse(
                call: retrofit2.Call<DataCollectionModel>,
                response: retrofit2.Response<DataCollectionModel>
            ) {
                if (response.isSuccessful) {
                    progressbar.visibility = View.GONE
                    Toast.makeText(context, "Submission successful!", Toast.LENGTH_SHORT).show()
                } else {
                    progressbar.visibility = View.GONE
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<DataCollectionModel>, t: Throwable) {
                progressbar.visibility = View.GONE
                Toast.makeText(context, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    
}