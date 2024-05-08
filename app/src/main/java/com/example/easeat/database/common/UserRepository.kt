package com.example.easeat.database.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.easeat.database.IUserAuth
import com.example.easeat.database.local.OrderDao
import com.example.easeat.database.local.RatingsDao
import com.example.easeat.database.local.UserDao
import com.example.easeat.database.remote.UserRemoteDatabase
import com.example.easeat.models.Order
import com.example.easeat.models.Rating
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
    private val ratingsDao: RatingsDao,
    private val coroutineScope: CoroutineScope
) : IUserAuth by userRemoteDb {

    private var auth = FirebaseAuth.getInstance()

    private var userDocumentListener: FirebaseListener<User>? = null
    private var userOrderDocumentListener: FirebaseListener<List<Order>>? = null
    private var userRatingsDocumentListener: FirebaseListener<List<Rating>>? = null


    private var userMediator = MediatorLiveData<User?>(null)
    private var userOrdersMediator = MediatorLiveData<List<Order>?>(null)
    private var userRatingsMediator = MediatorLiveData<List<Rating>?>(null)

    private val authListener : AuthStateListener = AuthStateListener { authState ->
        if(authState.currentUser == null) {
            userDocumentListener?.stopListening()
            userOrderDocumentListener?.stopListening()
            userRatingsDocumentListener?.stopListening()
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    //  remove stuff locally that are related to current user
                    userDao.delete()
                    ordersDao.delete()
                    ratingsDao.delete()
                }
            }
        }
        else {
            userDocumentListener = listenCurrentUser()
            userOrderDocumentListener = listenCurrentUserOrders()
            userRatingsDocumentListener = listenCurrentUserRatings()

            userOrdersMediator.addSource(userOrderDocumentListener!!.get()) {
                userOrdersMediator.postValue(it)
            }
            userMediator.addSource(userDocumentListener!!.get()) {
                userMediator.postValue(it)
            }

            userRatingsMediator.addSource(userRatingsDocumentListener!!.get()) {
                userRatingsMediator.postValue(it)
            }
        }
    }

    fun getCurrentUser() : LiveData<User?> {
        return userMediator
    }
    fun getCurrentUserOrders() : LiveData<List<Order>?> {
        return userOrdersMediator
    }
    fun getCurrentUserRatings() : LiveData<List<Rating>?> {
        return userRatingsMediator
    }

    init {
        auth.addAuthStateListener (authListener)
    }

    fun stopListening() {
        auth.removeAuthStateListener(authListener)
        userDocumentListener?.stopListening()
        userOrderDocumentListener?.stopListening()
        userRatingsDocumentListener?.stopListening()
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


    private fun listenCurrentUserRatings(): FirebaseListener<List<Rating>> {
        return FirebaseListener(
            userRemoteDb.listenCurrentUserRatings { ratings ->
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        ratingsDao.insert(ratings)
                    }
                }
            },
            ratingsDao.getCurrentUserRatings())
    }


    private fun listenCurrentUserOrders(): FirebaseListener<List<Order>> {

        return FirebaseListener(
            userRemoteDb.listenCurrentUserOrders { orders ->
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        ordersDao.insert(orders)
                    }
                }
            }, ordersDao.getOrders())
    }


}