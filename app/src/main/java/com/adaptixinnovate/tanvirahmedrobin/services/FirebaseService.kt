package com.adaptixinnovate.tanvirahmedrobin.services

import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseService {
    private val database = FirebaseDatabase.getInstance()
    private val statusRef = database.getReference("status")

    // Function to check the status value
    fun checkStatus(activity: Activity) {
        statusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue(Int::class.java) ?: 0
                if (status != 1) {
                    Toast.makeText(activity, "App will close. Unexpected Error Occurred", Toast.LENGTH_SHORT).show()
                    activity.finish() // Close the app
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}