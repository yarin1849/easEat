package com.example.easeat.database.remote

import android.net.Uri
import com.example.easeat.DEFAULT_IMAGE
import com.example.easeat.database.IUserAuth
import com.example.easeat.models.User
import com.example.easeat.models.dto.RegisterDto
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException

class UserRemoteDatabase(
    private val storage: ImageStorage
): IUserAuth {
    companion object {
        const val COLLECTION_NAME = "users"
    }
    private var fireStore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()


    fun listenCurrentUser(callback: (User) -> Unit): ListenerRegistration {
        val uid = auth.uid!!
        return fireStore.collection(COLLECTION_NAME)
            .document(uid)
            .addSnapshotListener { value, _ ->
                value?.toObject(User::class.java)?.let {
                    callback(it)
                }
            }
    }

    @Throws
    override suspend fun login(email: String, password: String) : AuthResult = withContext(Dispatchers.IO) {
        val continuation = CompletableDeferred<AuthResult>()

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(continuation::complete)
            .addOnFailureListener(continuation::completeExceptionally)

        continuation.await()
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    @Throws
   override suspend fun register(coroutineScope: CoroutineScope, registerDto: RegisterDto): User = withContext(Dispatchers.IO) {

        val continuation = CompletableDeferred<User>()
        with(registerDto) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        val id = it.user?.uid ?: run {
                            continuation.completeExceptionally(RuntimeException("Unknown error occurred"))
                            return@launch
                        }

                        // If user selected image, upload it
                        val image = registerDto.image?.let { userImage ->
                            return@let try {
                                storage.uploadImage("userImages/$id",userImage)
                            } catch (ignored:Exception) {DEFAULT_IMAGE}
                        } ?: DEFAULT_IMAGE

                        val user = User(
                            id = id,
                            name = registerDto.name,
                            email = registerDto.email,
                            address = registerDto.address,
                            birthday = registerDto.birthday,
                            image = image,
                            lastUpdated = System.currentTimeMillis()
                        )

                        fireStore.collection(COLLECTION_NAME)
                            .document(id)
                            .set(user)
                            .addOnSuccessListener {
                                continuation.complete(user)
                            }
                            .addOnFailureListener(continuation::completeExceptionally)
                    }
                }
                .addOnFailureListener(continuation::completeExceptionally)
            continuation.await()
        }
    }



}