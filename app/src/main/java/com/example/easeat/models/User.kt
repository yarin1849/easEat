package com.example.easeat.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(tableName = "users")
@Serializable
class User(
    @PrimaryKey override var id: String,
    var name: String,
    var email: String,
    var address: String,
    var birthday: Long,
    var image: String,
    override var lastUpdated: Long
): BaseModel()