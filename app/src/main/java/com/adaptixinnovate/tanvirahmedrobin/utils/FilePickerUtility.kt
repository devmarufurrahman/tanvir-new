package com.adaptixinnovate.tanvirahmedrobin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FilePickerUtility {

    // Check and request permissions for Android 7-10
    fun requestPermissions(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val requiredPermissions = arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val notGranted = requiredPermissions.any {
                ContextCompat.checkSelfPermission(activity, it) != android.content.pm.PackageManager.PERMISSION_GRANTED
            }
            if (notGranted) {
                ActivityCompat.requestPermissions(activity, requiredPermissions, 1001)
                return false
            }
        }
        return true
    }

    // Open file picker for Android 11+ using SAF
    fun openFilePicker(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(arrayOf("application/pdf", "image/*")) // Allow PDFs and images only
    }

    // Retrieve file name from URI
    suspend fun getFileName(context: Context, uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) it.getString(columnIndex) else null
                } else null
            }
        }
    }

    // Show permissions settings if denied
    fun openAppSettings(activity: Activity) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }

    fun setupFilePicker(context: Context) {
//        Toast.makeText(context, "Ready to pick files", Toast.LENGTH_SHORT).show()
    }

    fun openLegacyFilePicker(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf" // Allow only PDFs
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "image/*")) // Allow PDFs and images
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(activity, intent, 2001, null)
    }
}