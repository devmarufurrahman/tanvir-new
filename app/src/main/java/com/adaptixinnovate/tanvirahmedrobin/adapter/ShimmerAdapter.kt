package com.adaptixinnovate.tanvirahmedrobin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adaptixinnovate.tanvirahmedrobin.R

class ShimmerAdapter : RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shimmer_item_gallery, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        // No binding needed for shimmer
    }

    override fun getItemCount(): Int = 6 // Show 6 shimmer items

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
