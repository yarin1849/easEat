package com.example.easeat.database.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.easeat.database.IUserAuth
import com.example.easeat.database.local.OrderDao
import com.example.easeat.database.local.UserDao
import com.example.easeat.database.remote.UserRemoteDatabase
import com.example.easeat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepository(
    private val userRemoteDb: UserRemoteDatabase,
    private val userDao: UserDao,
    private val ordersDao: OrderDao,
    private val coroutineScope: CoroutineScope
) : IUserAuth by userRemoteDb {

    private var auth = FirebaseAuth.getInstance()

    private var userDocumentListener: FirebaseListener<User>? = null

    private var userMediator = MediatorLiveData<User?>(null)

    private val authListener : AuthStateListener = AuthStateListener { authState ->
        if(authState.currentUser == null) {
            userDocumentListener?.stopListening()
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    //  remove stuff locally that are related to current user
                    userDao.delete()
                    ordersDao.delete()
                }
            }
        }
        else {
            userDocumentListener = listenCurrentUser()
            userMediator.addSource(userDocumentListener!!.get()) {
                userMediator.postValue(it)
            }
        }
    }

    fun getCurrentUser() : LiveData<User?> {
        return userMediator
    }

    init {
        auth.addAuthStateListener (authListener)
    }

    fun stopListening() {
        auth.removeAuthStateListener(authListener)
        userDocumentListener?.stopListening()
    }

    private fun listenCurrentUser(): FirebaseListener<User> {

        return FirebaseListener(
            userRemoteDb.listenCurrentUser { user ->
             coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    userDao.insert(user)
                }
            }
           },
        userDao.getUser())
    }


}