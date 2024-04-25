package com.example.easeat.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "products")
@Serializable
class Product(
    @PrimaryKey override var id: String,
    override var lastUpdated: Long,
    var price: Double,
    var stock: Int,
    var rating: Double,
    var businessId: String,
    var image: String
): BaseModel()