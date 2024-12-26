package com.example.tanvirhome.model

import android.net.Uri

data class ContactModel(
    val name: String,
    val phone: String,
    val message: String,
    val uri: Uri? = null
)