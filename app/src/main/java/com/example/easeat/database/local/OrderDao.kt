package com.example.easeat.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easeat.models.Order
import com.example.easeat.models.User

@Dao
interface OrderDao {
    @Query("Select * from orders")
    fun getOrders() : LiveData<List<Order>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order) // upsert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orders: List<Order>) // upsert
    @Query("delete from orders")
    suspend fun delete() : Int
}