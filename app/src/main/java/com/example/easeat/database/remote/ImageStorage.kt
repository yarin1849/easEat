package com.example.easeat.database.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred

class ImageStorage {

    private var storage = FirebaseStorage.getInstance()

    suspend fun uploadImage(path:String, image: Uri) : String {
        val continuation = CompletableDeferred<String>()
        val ref = storage.getReference(path)
        ref
            .putFile(image)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { imageAddress ->
                    continuation.complete(imageAddress.toString())
                }
                    .addOnFailureListener(continuation::completeExceptionally)
            }
            .addOnFailureListener(continuation::completeExceptionally)
        return continuation.await()
    }

}