package com.adaptixinnovate.tanvirahmedrobin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.adaptixinnovate.tanvirahmedrobin.databinding.ItemGalleryBinding
import com.adaptixinnovate.tanvirahmedrobin.model.GalleryModel
import com.squareup.picasso.Picasso

class GalleryAdapter(
    private val galleryItems: List<GalleryModel>,
    private val onItemClick: (GalleryModel) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(private val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryModel, onItemClick: (GalleryModel) -> Unit) {
            binding.galleryTitle.text = item.title
            Picasso.get().load("${ AppConfig.IMG_URL}${item.image}")
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.load_error)
                .fit().centerCrop().into(binding.galleryImage)
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(galleryItems[position], onItemClick)
    }

    override fun getItemCount() = galleryItems.size
}

