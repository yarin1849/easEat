package com.example.easeat.models.util

import kotlinx.serialization.Serializable


@Serializable
data class OrderItem(
    var quantity: Int = 0,
    var price: Double = 0.0,
    var productId: String = ""
)