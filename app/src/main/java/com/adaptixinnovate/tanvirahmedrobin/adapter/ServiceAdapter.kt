package com.adaptixinnovate.tanvirahmedrobin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adaptixinnovate.tanvirahmedrobin.databinding.ItemServiceBinding

class ServiceAdapter(
    private val services: List<Service>,
    private val onServiceClick: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(private val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(service: Service) {
            binding.serviceIcon.setImageResource(service.iconResId)
            binding.serviceName.text = service.name

            // Set up click listener
            binding.root.setOnClickListener {
                onServiceClick(service)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount() = services.size
}

// Data class for services
data class Service(val name: String, val iconResId: Int)