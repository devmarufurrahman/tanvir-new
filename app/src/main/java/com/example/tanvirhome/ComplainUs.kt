package com.example.tanvirhome

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tanvirhome.databinding.ActivityComplainUsBinding
import com.example.tanvirhome.databinding.ActivityContactMeBinding
import com.example.tanvirhome.utils.FilePickerUtility
import kotlinx.coroutines.launch

class ComplainUs : AppCompatActivity() {
    private lateinit var binding: ActivityComplainUsBinding
    // File picker launcher
    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                lifecycleScope.launch {
                    val fileName = FilePickerUtility.getFileName(this@ComplainUs, it)
                    Toast.makeText(this@ComplainUs, "Selected File: $fileName", Toast.LENGTH_SHORT).show()
                    binding.fileName.text = fileName
                }
            } ?: Toast.makeText(this@ComplainUs, "No file selected", Toast.LENGTH_SHORT).show()
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
                    val fileName = FilePickerUtility.getFileName(this@ComplainUs, it)
                    Toast.makeText(this@ComplainUs, "Selected File: $fileName", Toast.LENGTH_SHORT).show()
                    binding.fileName.text = fileName
                }
            }
        }
    }
}