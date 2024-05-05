package com.example.easeat.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.easeat.database.common.UserRepository
import com.example.easeat.database.local.AppDatabase
import com.example.easeat.database.local.BusinessDao
import com.example.easeat.database.local.OrderDao
import com.example.easeat.database.local.ProductDao
import com.example.easeat.database.local.UserDao
import com.example.easeat.database.remote.BusinessRemoteDatabase
import com.example.easeat.database.remote.ImageStorage
import com.example.easeat.database.remote.OrderRemoteDatabase
import com.example.easeat.database.remote.ProductRemoteDatabase
import com.example.easeat.database.remote.UserRemoteDatabase
import dagger.Component.Factory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class DbModule {
    @Provides
    @ViewModelScoped
    fun provideUserDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @ViewModelScoped
    fun provideProductDao(appDatabase: AppDatabase) : ProductDao {
        return appDatabase.productDao()
    }

    @Provides
    @ViewModelScoped
    fun provideBusinessDao(appDatabase: AppDatabase) : BusinessDao {
        return appDatabase.businessDao()
    }

    @Provides
    @ViewModelScoped
    fun provideOrderDao(appDatabase: AppDatabase) : OrderDao {
        return appDatabase.orderDao()
    }


    @Provides
    @ViewModelScoped
    fun provideImageStorage() : ImageStorage {
        return ImageStorage()
    }

    @Provides
    @ViewModelScoped
    fun provideUserRemoteDb(storage: ImageStorage) : UserRemoteDatabase {
        return UserRemoteDatabase(storage)
    }

    @Provides
    @ViewModelScoped
    fun provideBusinessRemoteDb() : BusinessRemoteDatabase {
        return BusinessRemoteDatabase()
    }

    @Provides
    @ViewModelScoped
    fun provideOrderRemoteDb() : OrderRemoteDatabase {
        return OrderRemoteDatabase()
    }

    @Provides
    @ViewModelScoped
    fun provideProductRemoteDb() : ProductRemoteDatabase {
        return ProductRemoteDatabase()
    }

    // repositories
    @Provides
    @ViewModelScoped
    fun provideUserRepository(
        userRemoteDatabase: UserRemoteDatabase,
        userDao: UserDao,
        orderDao: OrderDao,
        coroutineScope: CoroutineScope,
    ) : UserRepository {
        return UserRepository(userRemoteDatabase, userDao, orderDao, coroutineScope)
    }


}