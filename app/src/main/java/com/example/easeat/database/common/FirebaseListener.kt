package com.example.easeat.database.common

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ListenerRegistration

class FirebaseListener<T>(
    private val listener: ListenerRegistration,
    private val liveData: LiveData<T>
) {

    fun stopListening() {
        listener.remove()
    }

    fun get() : LiveData<T> {
        return liveData
    }
}