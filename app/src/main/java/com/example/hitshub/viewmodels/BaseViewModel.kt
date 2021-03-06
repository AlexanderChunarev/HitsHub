package com.example.hitshub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.User
import com.example.hitshub.utils.MessageUtils

abstract class BaseViewModel : ViewModel() {
    protected val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean> get() = _showSpinner

    protected lateinit var _authenticatedUserLiveData: MutableLiveData<User>
    val authenticatedUserLiveData: LiveData<User> get() = _authenticatedUserLiveData

    protected lateinit var _userInfoLiveData: MutableLiveData<User>
    val userInfoLiveData: LiveData<User> get() = _userInfoLiveData

    protected val _message = SingleLiveEvent<MessageUtils>()
    val message: LiveData<MessageUtils> get() = _message
}
