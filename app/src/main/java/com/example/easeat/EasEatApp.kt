package com.example.easeat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.json.Json


var DEFAULT_IMAGE = "https://i.ibb.co/kD19GbQ/nophoto.webp"
var json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

@HiltAndroidApp
class EasEatApp: Application()