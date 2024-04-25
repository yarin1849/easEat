package com.example.easeat.models.util

import kotlinx.serialization.Serializable


@Serializable
data class OrderItem(
    var quantity: Int,
    var productId: String
)