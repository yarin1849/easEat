package com.example.easeat.viewmodels

import androidx.lifecycle.ViewModel
import com.example.easeat.database.common.UserRepository
import com.example.easeat.models.dto.RegisterDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository):  ViewModel() {

    val currentUser = userRepository.getCurrentUser()
    fun login(email: String, password: String) {
      //  @TODO: Login
    }

    fun register(registerDto: RegisterDto) {
        // @TODO: Register
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.stopListening()
    }
}