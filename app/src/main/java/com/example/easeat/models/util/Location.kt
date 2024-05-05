package com.example.easeat.models.util

import kotlinx.serialization.Serializable

@Serializable
data class Location(var latitude: Double,
                    var longtitude: Double) {
    constructor() : this(0.0,0.0)
}
