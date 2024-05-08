package com.example.easeat.adapters

import com.example.easeat.models.Rating

interface UserRatingListEvents {
    fun editRating(rating: Rating)
    fun deleteRating(rating: Rating)
}