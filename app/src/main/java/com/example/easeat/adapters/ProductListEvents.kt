package com.example.easeat.adapters

import com.example.easeat.models.Product

interface ProductListEvents {
    fun addToOrder(product: Product, quantity: Int)
    fun removeFromOrder(product: Product)
}