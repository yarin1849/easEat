package com.example.easeat

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun <T> LiveData<T?>.observeNotNull( owner : LifecycleOwner, observer : Observer<T>) {
    observe(owner) {
        if(it !=null) {
            observer.onChanged(it)
        }
    }
}