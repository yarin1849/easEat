package com.example.easeat.database.common

import com.example.easeat.database.local.BusinessDao
import com.example.easeat.database.local.ProductDao
import com.example.easeat.database.remote.BusinessRemoteDatabase
import com.example.easeat.database.remote.ProductRemoteDatabase
import com.example.easeat.models.Business
import com.example.easeat.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRepository(
    private val remoteDb: ProductRemoteDatabase,
    private val localDb: ProductDao
) {
    fun listenToBusinessProducts(id: String, coroutineScope: CoroutineScope): FirebaseListener<List<Product>> {
        val listener = remoteDb.listenToBusinessProducts(id) { products ->
            coroutineScope.launch(Dispatchers.IO) {
                localDb.insert(products)
            }
        }
        return FirebaseListener(listener, localDb.getProductsForBusiness(id))
    }

}