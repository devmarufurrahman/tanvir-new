package com.adaptixinnovate.tanvirahmedrobin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.adaptixinnovate.tanvirahmedrobin.model.GalleryModel
import com.adaptixinnovate.tanvirahmedrobin.network.repository.GalleryRepository
import kotlinx.coroutines.launch

class GalleryViewModel(private val repository: GalleryRepository) : ViewModel() {
    private val _galleryItems = MutableLiveData<List<GalleryModel>>()
    val galleryItems: LiveData<List<GalleryModel>> get() = _galleryItems

    fun fetchGalleryItems() {
        viewModelScope.launch {
            try {
                val items = repository.getGalleryItems()
                _galleryItems.postValue(items)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class GalleryViewModelFactory(private val repository: GalleryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
