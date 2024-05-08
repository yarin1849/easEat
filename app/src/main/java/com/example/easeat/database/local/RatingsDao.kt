package com.example.easeat.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easeat.models.Order
import com.example.easeat.models.Rating
import com.example.easeat.models.User

@Dao
interface RatingsDao {
    @Query("Select * from ratings where businessId = :id")
    fun getRatings(id: String) : LiveData<List<Rating>>

    @Query("Select * from ratings")
    fun getCurrentUserRatings() : LiveData<List<Rating>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rating: Rating) // upsert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ratings: List<Rating>) // upsert
    @Query("delete from ratings")
    suspend fun delete() : Int
    @Query("delete from ratings where id = :id")
    suspend fun delete(id: String) : Int
}