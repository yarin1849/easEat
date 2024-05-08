package com.example.easeat.database.remote

import android.net.Uri
import com.example.easeat.models.Rating

interface AddRating {
    suspend fun addRating(businessId: String, rating: Rating, image: Uri) : Rating
}