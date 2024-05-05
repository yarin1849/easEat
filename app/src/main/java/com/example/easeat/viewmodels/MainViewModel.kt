package com.example.easeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easeat.database.common.UserRepository
import com.example.easeat.models.dto.RegisterDto
import com.example.easeat.models.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository):  ViewModel() {

    val currentUser = userRepository.getCurrentUser()

    private val _loading = MutableLiveData<LoadingState>(LoadingState.Loaded)
    val loading: LiveData<LoadingState> get() =  _loading

    private val _exceptions = MutableLiveData<Exception?>(null)
    val exceptions: LiveData<Exception?> get() =  _exceptions


    fun signOut() {
        userRepository.signOut()
    }

    fun login(email: String, password: String, successCallback: () -> Unit, failCallback: () -> Unit) {
        _loading.postValue(LoadingState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.login(email,password)
                withContext(Dispatchers.Main) {
                    successCallback.invoke()
                }
            }
            catch(e: Exception) {
                _exceptions.postValue(e)
                withContext(Dispatchers.Main) {
                    failCallback.invoke()
                }
            }
            finally {
                _loading.postValue(LoadingState.Loaded)
            }
        }
    }

    fun register(registerDto: RegisterDto, successCallback: () -> Unit, failCallback: () -> Unit) {
        _loading.postValue(LoadingState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.register(viewModelScope, registerDto)
                withContext(Dispatchers.Main) {
                    successCallback.invoke()
                }
            }
            catch(e: Exception) {
                _exceptions.postValue(e)
                withContext(Dispatchers.Main) {
                    failCallback.invoke()
                }
            }
            finally {
                _loading.postValue(LoadingState.Loaded)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.stopListening()
    }
}