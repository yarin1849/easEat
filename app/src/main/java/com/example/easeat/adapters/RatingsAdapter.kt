package com.example.easeat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.easeat.databinding.RatingItemBinding
import com.example.easeat.models.Rating
import com.squareup.picasso.Picasso

class RatingsAdapter(val ratings: List<Rating>) : RecyclerView.Adapter<RatingsAdapter.RatingsViewHolder>() {

    inner class RatingsViewHolder(val binding: RatingItemBinding) : ViewHolder(binding.root) {
        fun bind(rating: Rating) {

            Picasso.get().load(rating.image)
                .into(binding.ivPostImage)

            binding.tvReview.text = rating.content
            binding.tvReviewerName.text = rating.personName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingsViewHolder {
        return RatingsViewHolder(
            RatingItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
    }

    override fun getItemCount(): Int {
        return ratings.size
    }

    override fun onBindViewHolder(holder: RatingsViewHolder, position: Int) {
        val rating = ratings[position]
        holder.bind(rating)
    }
}