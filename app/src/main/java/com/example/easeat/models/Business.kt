package com.example.easeat.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easeat.models.util.Location
import kotlinx.serialization.Serializable

@Entity(tableName = "businesses")
@Serializable
class Business(
    @PrimaryKey override var id: String,
    var name: String,
    var location: Location,
    var deliveryRadius: Double,
    var averageDeliveryTimeInMinutes: Int,
    var rating: Float,
    var category: String,
    var image: String,
    override var lastUpdated: Long
) : BaseModel() {
    constructor() :this("","",Location(), 0.0,0,0.0f,"","",0)
}
