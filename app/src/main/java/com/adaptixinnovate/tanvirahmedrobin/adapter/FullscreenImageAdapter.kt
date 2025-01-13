package com.adaptixinnovate.tanvirahmedrobin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ItemFullscreenImageBinding
import com.adaptixinnovate.tanvirahmedrobin.model.GalleryModel
import com.squareup.picasso.Picasso

class FullscreenImageAdapter(private val galleryItems: List<GalleryModel>) :
    RecyclerView.Adapter<FullscreenImageAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemFullscreenImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String, text: String) {
            binding.imageFullViewText.text = text
            Picasso.get().load("${ AppConfig.IMG_URL}${image}")
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.load_error)
                .fit().centerInside().into(binding.fullscreenImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFullscreenImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(galleryItems[position].image, galleryItems[position].title)
    }

    override fun getItemCount() = galleryItems.size
}
