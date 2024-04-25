package com.example.easeat.database.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.easeat.json
import com.example.easeat.models.util.Location
import com.example.easeat.models.util.OrderItem
import kotlinx.serialization.encodeToString

class Converters {

    @TypeConverter
    fun fromOrderItemListToString(list: List<OrderItem>) : String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToOrderItemList(str: String) : List<OrderItem> {
        return json.decodeFromString(str)
    }

    @TypeConverter
    fun fromLocationToString(location: Location) : String {
        return json.encodeToString(location)
    }

    @TypeConverter
    fun fromStringToLocation(str: String) : Location {
        return json.decodeFromString(str)
    }
}