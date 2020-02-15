package com.example.hitshub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    protected val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean> get() = _showSpinner
}
