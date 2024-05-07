package com.example.easeat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easeat.database.common.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BusinessViewModel @Inject constructor(private val businessRepository: BusinessRepository): ViewModel() {
    private val businessListener by lazy {
        businessRepository.listenToBusinesses(viewModelScope)
    }
    val businesses by lazy {
        businessListener.get()
    }
    override fun onCleared() {
        super.onCleared()
        businessListener.stopListening()
    }
}