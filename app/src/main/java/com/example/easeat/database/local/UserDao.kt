package com.example.easeat.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easeat.models.User

@Dao
interface UserDao {

    @Query("Select * from users LIMIT 1")
    fun getUser() : LiveData<User>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User) // upsert
    @Query("delete from users")
    suspend fun delete() : Int
}