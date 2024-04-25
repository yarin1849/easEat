package com.example.easeat.models.dto

import android.net.Uri

data class RegisterDto(
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val birthday: Long,
    val image: Uri? = null,
)