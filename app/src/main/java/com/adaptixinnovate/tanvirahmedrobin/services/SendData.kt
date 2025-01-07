package com.adaptixinnovate.tanvirahmedrobin.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.adaptixinnovate.tanvirahmedrobin.model.ContactModel
import com.adaptixinnovate.tanvirahmedrobin.model.DataCollectionModel
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.ui.CongratulationsActivity
import com.adaptixinnovate.tanvirahmedrobin.ui.MainActivity
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

object SendData {
    
    fun dataCollection(name: String, fatherName: String, motherName: String, nid: String, dateOfBirth: String, mobile: String, gender: String, address: String, wardName: String, thanaName: String, context: Context, progressbar: ProgressBar){
        
        val userDetails = DataCollectionModel(
            name, fatherName, motherName, nid, dateOfBirth, mobile, gender, address, wardName, thanaName
        )

        RetrofitClient.instance.postUserData(userDetails).enqueue(object : retrofit2.Callback<DataCollectionModel> {
            override fun onResponse(
                call: retrofit2.Call<DataCollectionModel>,
                response: retrofit2.Response<DataCollectionModel>
            ) {
                if (response.isSuccessful) {
                    progressbar.visibility = View.GONE
                    Toast.makeText(context, "Submission successful!", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, CongratulationsActivity::class.java))
                    (context as? Activity)?.finish()
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