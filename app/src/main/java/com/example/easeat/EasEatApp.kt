package com.example.easeat

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.json.Json

const val DEFAULT_IMAGE = "https://i.ibb.co/kD19GbQ/nophoto.webp"
const val LAST_UPDATE_KEY =  "lastUpdated"
val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

@HiltAndroidApp
class EasEatApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(this, getString(R.string.googleApiKey))
    }
}