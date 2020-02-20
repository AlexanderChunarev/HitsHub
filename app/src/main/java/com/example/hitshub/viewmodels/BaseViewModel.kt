package com.example.hitshub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.User

abstract class BaseViewModel : ViewModel() {
    protected val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean> get() = _showSpinner

    protected var _authenticatedUserLiveData = MutableLiveData<User>()
    val authenticatedUserLiveData: LiveData<User> get() = _authenticatedUserLiveData

    protected var _userInfoLiveData = MutableLiveData<User>()
    val userInfoLiveData: LiveData<User> get() = _userInfoLiveData
}
