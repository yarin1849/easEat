package com.example.easeat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easeat.databinding.RestaurantItemBinding
import com.example.easeat.models.Business
import com.squareup.picasso.Picasso

class BusinessAdapter(
    private val businesses: List<Business>,
    private val events: BusinessListEvents
) : RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder>(),
    BusinessListEvents by events {

    inner class BusinessViewHolder(private val binding: RestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(business: Business) {
            Picasso.get().load(business.image)
                .into(binding.ivRestaurant)

            binding.tvRestaurant.text = business.name
            binding.tvRestaurantRating.text = String.format("%.1f", business.rating)
            binding.rbRestaurant.rating = business.rating
            binding.tvRestaurantDeliveryTime.text = String.format("%d min", business.averageDeliveryTimeInMinutes)
            binding.ivRestaurant.setOnClickListener {
                onShowDetails(business)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        return BusinessViewHolder(RestaurantItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
       return businesses.size
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = businesses[position]
        holder.bind(business)
    }
}