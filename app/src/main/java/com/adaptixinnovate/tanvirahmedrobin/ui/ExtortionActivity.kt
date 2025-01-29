package com.adaptixinnovate.tanvirahmedrobin.ui

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.adaptixinnovate.tanvirahmedrobin.databinding.ActivityExtortionActivityBinding
import com.adaptixinnovate.tanvirahmedrobin.model.LocationModel
import com.adaptixinnovate.tanvirahmedrobin.network.retrofit.RetrofitClient
import com.adaptixinnovate.tanvirahmedrobin.utils.SetupDropDown
import okhttp3.Address
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ExtortionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtortionActivityBinding
    private var fileUri: Uri? = null
    var selectedWardId: String? = "0"
    var selectedThanaId: String? = "0"

    // File picker launcher
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            fileUri = it

            binding.imageView.visibility = View.VISIBLE
            binding.fileName.visibility = View.VISIBLE
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
        binding = ActivityExtortionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fetchWards(this, binding.wardNameInput, binding.thanaNameInput)

        binding.swipeRefreshLayout.setOnRefreshListener {
            recreate()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.browseFileButton.setOnClickListener {
            openImagePicker()
        }

        binding.sendMessageButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val message = binding.messageEditText.text.toString()

            if (validateInputs(name, phone, message, selectedWardId.toString(), selectedThanaId.toString())) {
                binding.progressBar.visibility = View.VISIBLE
                fileUri?.let {
                    val file = uriToFile(it)
                    uploadImageFile(name, phone, message, selectedWardId.toString(), selectedThanaId.toString(), file)
                } ?: run {
                    uploadImageFile(name, phone, message, selectedWardId.toString(), selectedThanaId.toString(), null)
                }
            }
        }

    }



    private fun validateInputs(name: String, phone: String, message: String, wardId: String, thanaId: String): Boolean {
        if (name.isEmpty()) {
            binding.nameEditText.error = "Name cannot be empty"
            binding.nameEditText.requestFocus()
            return false
        }
        if (phone.isEmpty() || !phone.matches("\\d{11}".toRegex())) {
            binding.phoneEditText.error = "Enter a valid 11-digit phone number"
            binding.phoneEditText.requestFocus()
            return false
        }
        if (wardId == "0") {
            binding.wardNameInput.error = "Ward cannot be empty"
            binding.wardNameInput.requestFocus()
            return false
        }
        if (thanaId == "0") {
            binding.thanaNameInput.error = "Thana cannot be empty"
            binding.thanaNameInput.requestFocus()
            return false
        }
        if (message.isEmpty()) {
            binding.messageEditText.error = "Message cannot be empty"
            binding.messageEditText.requestFocus()
            return false
        }
        return true
    }


    private fun fetchWards(context: Context, wardView: AutoCompleteTextView, thanaView: AutoCompleteTextView) {
        RetrofitClient.instance.getWard().enqueue(object : retrofit2.Callback<List<LocationModel>> {
            override fun onResponse(call: retrofit2.Call<List<LocationModel>>, response: retrofit2.Response<List<LocationModel>>) {
                if (response.isSuccessful) {
                    val wardsList = response.body() ?: emptyList()
                    val dropDown = SetupDropDown()

                    dropDown.setupDropdown(wardView, wardsList, context) { wardId ->
                        // Handle the selected ward ID here
                        println("Selected Ward ID: $wardId")
                        selectedWardId = wardId.toString()
                        loadThanas(wardId, context, thanaView)
                    }

                } else {
                    println(response)
                    Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<LocationModel>>, t: Throwable) {
                println("Error: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun loadThanas(wardId: Int, context: Context, inputView: AutoCompleteTextView) {
        RetrofitClient.instance.getThanasByWardId(wardId).enqueue(object :
            Callback<List<LocationModel>> {
            override fun onResponse(call: Call<List<LocationModel>>, response: Response<List<LocationModel>>) {
                if (response.isSuccessful) {
                    val thanaList = response.body() ?: emptyList()
                    val dropDown = SetupDropDown()
                    dropDown.setupDropdown(inputView, thanaList, context) { thanaId ->
                        // Handle the selected ward ID here
                        println("Selected Thana ID: $thanaId")
                        selectedThanaId = thanaId.toString()
                    }

                } else {
                    Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<LocationModel>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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

    private fun uploadImageFile(name: String, phone: String, message: String,wardId: String, thanaId: String, imageFile: File?) {
        val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val phonePart = RequestBody.create("text/plain".toMediaTypeOrNull(), phone)
        val messagePart = RequestBody.create("text/plain".toMediaTypeOrNull(), message)
        val ward_id = RequestBody.create("text/plain".toMediaTypeOrNull(), wardId)
        val thana_id = RequestBody.create("text/plain".toMediaTypeOrNull(), thanaId)

        val imageRequestBody = imageFile?.let {
            RequestBody.create("image/*".toMediaTypeOrNull(),
                it
            )
        }

        val imagePart =
            imageRequestBody?.let { MultipartBody.Part.createFormData("file", imageFile.name, it) }

        RetrofitClient.instance.extortionMessage(namePart, phonePart, messagePart, ward_id, thana_id, imagePart)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@ExtortionActivity, "Submit successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ExtortionActivity, CongratulationsActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@ExtortionActivity, "Upload failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@ExtortionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}