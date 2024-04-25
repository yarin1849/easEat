package com.example.easeat.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easeat.models.Business
import com.example.easeat.models.User

@Dao
interface ProductDao {
    @Query("Select * from products where businessId = :businessId")
    fun getProductsForBusiness(businessId: String) : LiveData<List<Business>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(business:Business) // upsert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(businesses: List<Business>) // upsert
    @Delete
    suspend fun delete(business: Business)

    @Query("delete from products")
    suspend fun delete()
}