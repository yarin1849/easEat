package com.example.easeat.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "ratings")
data class Rating(@PrimaryKey val id:String = "",
                  val businessId: String = "",
                  var image: String = "",
             val personName: String = "",
             val content: String = "")