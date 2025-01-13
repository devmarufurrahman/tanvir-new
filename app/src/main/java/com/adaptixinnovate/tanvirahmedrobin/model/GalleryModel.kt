package com.adaptixinnovate.tanvirahmedrobin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GalleryModel(
    val id: Int,
    val title: String,
    val image: String
) : Parcelable
