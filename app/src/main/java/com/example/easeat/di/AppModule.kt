package com.example.easeat.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.location.Geocoder
import com.example.easeat.database.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {
    @Provides
    @ViewModelScoped
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("com.example.easeat", MODE_PRIVATE)
    }

    @Provides
    @ViewModelScoped
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return AppDatabase.getInstance(context)
    }


    @Provides
    @ViewModelScoped
    fun provideGeoCoder(@ApplicationContext context: Context) : Geocoder {
        return Geocoder(context)
    }

    @Provides
    @ViewModelScoped
    fun provideCoroutineScope() : CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }

}