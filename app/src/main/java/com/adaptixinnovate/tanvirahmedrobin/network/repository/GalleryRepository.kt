package com.adaptixinnovate.tanvirahmedrobin.network.repository

import com.adaptixinnovate.tanvirahmedrobin.model.GalleryModel
import com.adaptixinnovate.tanvirahmedrobin.network.api.ApiService

class GalleryRepository(private val apiService: ApiService) {
    suspend fun getGalleryItems(): List<GalleryModel> {
        return apiService.getGalleryItems()
    }

    suspend fun getGallery31Items(): List<GalleryModel> {
        return apiService.getGallery31Items()
    }
}
