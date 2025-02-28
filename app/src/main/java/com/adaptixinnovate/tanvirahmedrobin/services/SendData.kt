package com.adaptixinnovate.tanvirahmedrobin.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.adaptixinnovate.tanvirahmedrobin.model.DataCollectionModel
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.ui.CongratulationsActivity

object SendData {
    
    fun dataCollection(name: String, fatherName: String, motherName: String, nid: String, dateOfBirth: String, mobile: String, gender: String, address: String, wardName: String, thanaName: String, context: Context, progressbar: ProgressBar){
        
        val userDetails = DataCollectionModel(
            name, fatherName, motherName, nid, dateOfBirth, mobile, gender, address, wardName, thanaName
        )

        Log.d("data collection", "dataCollection: $userDetails")

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
                    Toast.makeText(context, "Error database: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("error", "Error database: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<DataCollectionModel>, t: Throwable) {
                progressbar.visibility = View.GONE
                Log.e("error", "onFailure: ${t.message}")
                Toast.makeText(context, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    
    
}