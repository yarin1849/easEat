package com.example.easeat.database.remote

import android.content.SharedPreferences
import com.example.easeat.LAST_UPDATE_KEY
import com.example.easeat.models.Business
import com.example.easeat.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class ProductRemoteDatabase(val sp: SharedPreferences) {

    var fireStore = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()
    companion object {
        const val COLLECTION_NAME = "products"
        fun collectionName (id:String) = "businesses_products_update_time_$id"
        fun collectionNameRatings (id:String) = "businesses_ratings_update_time_$id"
    }
    fun listenToBusinessProducts(id: String, onBusinessProductsCallback: (List<Product>) -> Unit): ListenerRegistration {
        val productsCollection = collectionName(id)
        val lastUpdateTime = sp.getLong(productsCollection, 0)
        return fireStore
            .collection(BusinessRemoteDatabase.COLLECTION_NAME)
            .document(id)
            .collection(COLLECTION_NAME)
            .whereGreaterThanOrEqualTo(LAST_UPDATE_KEY, lastUpdateTime)
            .addSnapshotListener { value, error ->
                onBusinessProductsCallback.invoke(
                    value?.toObjects(Product::class.java) ?: listOf()
                )
                sp.edit()
                    .putLong(productsCollection, System.currentTimeMillis())
                    .apply()
            }
    }


}