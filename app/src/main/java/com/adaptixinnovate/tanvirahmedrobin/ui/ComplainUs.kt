package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityComplainUsBinding
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.utils.FilePickerUtility
import com.adaptixinnovate.tanvirahmedrobin.services.SendData
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ComplainUs : AppCompatActivity() {
    private lateinit var binding: ActivityComplainUsBinding
    private var fileUri: Uri? = null

    // File picker launcher
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            fileUri = it

            binding.imageView.setImageURI(it)
            binding.imageView.imageTintList = null
            binding.fileName.text = "Selected File: ${uriToFile(it).name}"
        }
    }

    fun openImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout
        binding = ActivityComplainUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the toolbar
        setSupportActionBar(binding.customToolbar)
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.swipeRefreshLayout.setOnRefreshListener {
            recreate()
            // Code to refresh the content goes here

            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.browseFileButton.setOnClickListener {
            openImagePicker()
        }


        binding.sendMessageButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val message = binding.messageEditText.text.toString()

            if (validateInputs(name, phone, message)) {
                binding.progressBar.visibility = View.VISIBLE
                fileUri?.let {
                    val file = uriToFile(it)
                    uploadImageFile(name, phone, message, file)
                } ?: run {
                    Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }



    private fun validateInputs(name: String, phone: String, message: String): Boolean {
        if (name.isEmpty()) {
            binding.nameEditText.error = "Name cannot be empty"
            return false
        }
        if (phone.isEmpty() || !phone.matches("\\d{11}".toRegex())) {
            binding.phoneEditText.error = "Enter a valid 11-digit phone number"
            return false
        }
        if (message.isEmpty()) {
            binding.messageEditText.error = "Message cannot be empty"
            return false
        }
        return true
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver: ContentResolver = applicationContext.contentResolver


        // Extract the original file name
        val fileName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "default_image.jpg" // Default name if no name is found

        val inputStream = contentResolver.openInputStream(uri)
        val file = File(applicationContext.cacheDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun uploadImageFile(name: String, phone: String, message: String, imageFile: File) {
        val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val phonePart = RequestBody.create("text/plain".toMediaTypeOrNull(), phone)
        val messagePart = RequestBody.create("text/plain".toMediaTypeOrNull(), message)

        val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, imageRequestBody)

        RetrofitClient.instance.complainMessage(namePart, phonePart, messagePart, imagePart)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@ComplainUs, "Upload successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ComplainUs, CongratulationsActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@ComplainUs, "Upload failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@ComplainUs, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    
    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }

}