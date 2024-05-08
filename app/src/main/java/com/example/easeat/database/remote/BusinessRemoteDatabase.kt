package com.example.easeat.database.remote

import android.content.SharedPreferences
import android.net.Uri
import com.example.easeat.DEFAULT_IMAGE
import com.example.easeat.LAST_UPDATE_KEY
import com.example.easeat.models.Business
import com.example.easeat.models.Order
import com.example.easeat.models.Product
import com.example.easeat.models.Rating
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusinessRemoteDatabase(
    private val sp: SharedPreferences,
    private val imageStorage: ImageStorage
)  : AddRating{

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

    fun listenToBusinessRatings(id: String, onBusinessProductsCallback: (List<Rating>) -> Unit): ListenerRegistration {
        return fireStore
            .collection(BusinessRemoteDatabase.COLLECTION_NAME)
            .document(id)
            .collection("ratings")
            .addSnapshotListener { value, error ->
                onBusinessProductsCallback.invoke(
                    value?.toObjects(Rating::class.java) ?: listOf()
                )
            }
    }
    override suspend fun addRating(businessId: String, rating: Rating, image: Uri) = withContext(Dispatchers.IO) {

        val continuation = CompletableDeferred<Rating>()

        val newDoc = fireStore
            .collection(BusinessRemoteDatabase.COLLECTION_NAME)
            .document(businessId)
            .collection("ratings")
            .document()
        rating.image  =  imageStorage.uploadImage("ratingImages/${newDoc.id}",image)

        newDoc.set(rating)
            .addOnSuccessListener {
                continuation.complete(rating)
            }.addOnFailureListener(continuation::completeExceptionally)

        continuation.await()
    }



}