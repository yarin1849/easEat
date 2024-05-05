package com.example.easeat.models.util

sealed class LoadingState {
    data object Loaded : LoadingState()
    data object Loading : LoadingState()
}