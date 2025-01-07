package com.adaptixinnovate.tanvirahmedrobin.model

import android.net.Uri

data class ContactModel(
    val name: String,
    val phone: String,
    val message: String,
    val file: Uri? = null
)