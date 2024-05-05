package com.example.easeat.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easeat.models.util.OrderItem
import kotlinx.serialization.Serializable


@Entity(tableName = "orders")
@Serializable
class Order(
    @PrimaryKey override var id: String,
    override var lastUpdated: Long,
    var businessId: String,
    var userId: String,
    var orderDate: Long,
    var orderAddress: String,
    var items: MutableList<OrderItem>
): BaseModel() {
    constructor() : this("", 0,"","",0,"", mutableListOf())

}