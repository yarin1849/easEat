package com.example.easeat.database

import com.example.easeat.models.Order
import com.example.easeat.models.User
import com.example.easeat.models.dto.RegisterDto
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CoroutineScope

interface IUserAuth {
    suspend fun register(coroutineScope: CoroutineScope, registerDto: RegisterDto) : User
    suspend fun login(email: String, password: String) : AuthResult
    suspend fun addOrder(order: Order)
    fun signOut()

}