package com.example.easeat.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easeat.models.Business
import com.example.easeat.models.Product
import com.example.easeat.models.User

@Dao
interface ProductDao {
    @Query("Select * from products where businessId = :businessId")
    fun getProductsForBusiness(businessId: String) : LiveData<List<Product>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product) // upsert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(products: List<Product>) // upsert
    @Delete
    suspend fun delete(product: Product)

    @Query("delete from products")
    suspend fun delete()
}