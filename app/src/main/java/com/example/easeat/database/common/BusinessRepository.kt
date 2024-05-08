package com.example.easeat.database.common

import com.example.easeat.database.local.BusinessDao
import com.example.easeat.database.local.RatingsDao
import com.example.easeat.database.remote.AddRating
import com.example.easeat.database.remote.BusinessRemoteDatabase
import com.example.easeat.models.Business
import com.example.easeat.models.Product
import com.example.easeat.models.Rating
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusinessRepository(
    private val remoteDb: BusinessRemoteDatabase,
    private val localDb: BusinessDao,
    private val ratingsDao: RatingsDao
) : AddRating by remoteDb {

    fun listenToBusinesses(coroutineScope: CoroutineScope): FirebaseListener<List<Business>> {
        val listener = remoteDb.listenToBusinesses { businesses ->
            coroutineScope.launch(Dispatchers.IO) {
                localDb.insert(businesses)
            }
        }
        return FirebaseListener(listener, localDb.getBusinesses())
    }

    override suspend fun deleteRating(businessId: String, ratingId: String) = withContext(Dispatchers.IO) {
        val res = remoteDb.deleteRating(businessId, ratingId)
        ratingsDao.delete(ratingId)
        res
    }


    fun listenToBusinessRatings(id:String, coroutineScope: CoroutineScope): FirebaseListener<List<Rating>> {
        val listener = remoteDb.listenToBusinessRatings(id) { ratings ->
            coroutineScope.launch(Dispatchers.IO) {
                ratingsDao.insert(ratings)
            }
        }
        return FirebaseListener(listener, ratingsDao.getRatings(id))
    }

}