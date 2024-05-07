package com.example.easeat.database.remote

import android.content.SharedPreferences
import com.example.easeat.LAST_UPDATE_KEY
import com.example.easeat.models.Business
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class BusinessRemoteDatabase(private val sp: SharedPreferences) {

    var fireStore = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()

    companion object {
        const val COLLECTION_NAME = "businesses"
        const val COLLECTION_NAME_UPDATE_TIME = "businesses_update_time"

    }
    fun listenToBusinesses(onBusinessesCallback: (List<Business>) -> Unit): ListenerRegistration {
        val lastUpdateTime = sp.getLong(COLLECTION_NAME_UPDATE_TIME, 0)
        return fireStore.collection(COLLECTION_NAME) // 10000 ,
            .whereGreaterThanOrEqualTo(LAST_UPDATE_KEY, lastUpdateTime)
            .addSnapshotListener { value, error ->
                onBusinessesCallback.invoke(
                    value?.toObjects(Business::class.java) ?: listOf()
                )
                sp.edit()
                    .putLong(COLLECTION_NAME_UPDATE_TIME, System.currentTimeMillis())
                    .apply()
            }
    }


}