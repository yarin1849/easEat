package com.example.easeat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easeat.databinding.RestaurantItemBinding
import com.example.easeat.databinding.SearchItemBinding
import com.example.easeat.models.Business
import com.squareup.picasso.Picasso

class BusinessSearchAdapter(
    private val businesses: List<Business>,
    private val events: BusinessListEvents
) : RecyclerView.Adapter<BusinessSearchAdapter.BusinessViewHolder>(),
    BusinessListEvents by events {
    inner class BusinessViewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(business: Business) {
            Picasso.get().load(business.image)
                .into(binding.ivRestaurantSearch)

            binding.tvRatingSearch.text = business.name
            binding.tvRatingSearch.text = String.format("%.1f", business.rating)
            binding.tvRestaurantCategorySearch.text = business.category
            binding.root.setOnClickListener {
                onShowDetails(business)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        return BusinessViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
       return businesses.size
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = businesses[position]
        holder.bind(business)
    }
}