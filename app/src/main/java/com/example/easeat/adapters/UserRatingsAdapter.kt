package com.example.easeat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.easeat.databinding.RatingItemBinding
import com.example.easeat.databinding.UserRatingItemBinding
import com.example.easeat.models.Rating
import com.example.easeat.ui.AppDialogs
import com.squareup.picasso.Picasso

class UserRatingsAdapter(val ratings: MutableList<Rating>, events: UserRatingListEvents)
    : RecyclerView.Adapter<UserRatingsAdapter.UserRatingsViewHolder>(), UserRatingListEvents by events {

    inner class UserRatingsViewHolder(val binding: UserRatingItemBinding) : ViewHolder(binding.root) {
        fun bind(rating: Rating, position: Int) {

            Picasso.get().load(rating.image)
                .into(binding.ivPostImage)

            binding.tvReview.text = rating.content
            binding.tvReviewerName.text = rating.personName

            binding.btnDelete.setOnClickListener {
                AppDialogs.showReviewDeleteDialog(binding.root.context, rating) {
                    ratings.removeAt(position)
                    notifyItemRemoved(position)
                    deleteRating(rating)
                }
            }
            binding.btnEdit.setOnClickListener {
                editRating(rating)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRatingsViewHolder {
        return UserRatingsViewHolder(
            UserRatingItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
    }

    override fun getItemCount(): Int {
        return ratings.size
    }

    override fun onBindViewHolder(holder: UserRatingsViewHolder, position: Int) {
        val rating = ratings[position]
        holder.bind(rating, position)
    }




}