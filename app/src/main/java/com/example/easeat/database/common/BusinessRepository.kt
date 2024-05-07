package com.example.easeat.database.common

import com.example.easeat.database.local.BusinessDao
import com.example.easeat.database.remote.BusinessRemoteDatabase
import com.example.easeat.models.Business
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BusinessRepository(
    private val remoteDb: BusinessRemoteDatabase,
    private val localDb: BusinessDao
) {

    fun listenToBusinesses(coroutineScope: CoroutineScope): FirebaseListener<List<Business>> {
        val listener = remoteDb.listenToBusinesses { businesses ->
            coroutineScope.launch(Dispatchers.IO) {
                localDb.insert(businesses)
            }
        }
        return FirebaseListener(listener, localDb.getBusinesses())
    }
}