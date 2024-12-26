package com.example.tanvirhome.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tanvirhome.databinding.ActivityContactMeBinding
import com.example.tanvirhome.utils.FilePickerUtility
import com.example.tanvirhome.utils.SendData
import kotlinx.coroutines.launch

class ContactMe : AppCompatActivity() {
    private lateinit var binding: ActivityContactMeBinding
    private var fileUri: Uri? = null
    // File picker launcher
    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                lifecycleScope.launch {
                    val fileName = FilePickerUtility.getFileName(this@ContactMe, it)
                    Toast.makeText(this@ContactMe, "Selected File: $fileName", Toast.LENGTH_SHORT).show()
                    binding.fileName.text = fileName
                    fileUri = uri
                }
            } ?: Toast.makeText(this@ContactMe, "No file selected", Toast.LENGTH_SHORT).show()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout
        binding = ActivityContactMeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the toolbar
        setSupportActionBar(binding.customToolbar)
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Request permissions for Android 7-10
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (FilePickerUtility.requestPermissions(this)) {
                FilePickerUtility.setupFilePicker(this)
            }
        } else {
            FilePickerUtility.openAppSettings(this)
        }

        binding.browseFileButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Use SAF for Android 11+
                FilePickerUtility.openFilePicker(filePickerLauncher)
            } else {
                // Legacy permission-based file picker for Android 7-10
                FilePickerUtility.openLegacyFilePicker(this)
            }
        }

        binding.sendMessageButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val message = binding.messageEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditText.error = "Name cannot be empty"
                    return@setOnClickListener
                }
                phone.isEmpty() || !phone.matches("\\d{11}".toRegex()) -> {
                    binding.phoneEditText.error = "Enter a valid 11-digit phone number"
                    return@setOnClickListener
                }
                message.isEmpty() -> {
                    binding.messageEditText.error = "Message cannot be empty"
                    return@setOnClickListener
                }
                else -> {
                    // All validations pass, proceed with further actions
                    fileUri?.let { it1 -> SendData.contactMessage(name, phone, message, it1,this) }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // This method is called when the up button is pressed. Just finish the activity
        finish()
        return true
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.all { it == android.content.pm.PackageManager.PERMISSION_GRANTED }) {
                FilePickerUtility.setupFilePicker(this)
            } else {
                Toast.makeText(this, "Permissions denied. Adjust in settings.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2001 && resultCode == RESULT_OK) {
            val uri = data?.data
            uri?.let {
                lifecycleScope.launch {
                    val fileName = FilePickerUtility.getFileName(this@ContactMe, it)
                    Toast.makeText(this@ContactMe, "Selected File: $fileName", Toast.LENGTH_SHORT).show()
                    binding.fileName.text = fileName
                    fileUri = uri
                }
            }
        }
    }
}