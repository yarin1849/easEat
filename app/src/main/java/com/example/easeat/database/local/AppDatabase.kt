package com.example.easeat.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easeat.models.Business
import com.example.easeat.models.Order
import com.example.easeat.models.Product
import com.example.easeat.models.User

@Database(entities = [User::class, Product::class, Business::class, Order::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun businessDao() : BusinessDao
    abstract fun productDao() : ProductDao
    abstract fun userDao() : UserDao
    abstract fun orderDao() : OrderDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java,"db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}