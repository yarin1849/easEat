package com.example.easeat.models.util

import kotlinx.serialization.Serializable
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Serializable
data class Location(var latitude: Double,
                    var longtitude: Double) {
    constructor() : this(0.0,0.0)
}

fun Location.hasInRadius(radius: Double, other: Location): Boolean {
    val earthRadius = 6371.0

    val latDistance = Math.toRadians(other.latitude - latitude)
    val lngDistance = Math.toRadians(other.longtitude - longtitude)

    val a = sin(latDistance / 2).pow(2) +
            cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude)) * sin(lngDistance/2).pow(2)

    val c  = 2 * atan2(sqrt(a), sqrt(1-a))
    val distance = earthRadius * c
    return distance <= radius
}