package com.example.hitshub.viewmodels

import androidx.lifecycle.LifecycleObserver
import com.example.hitshub.models.User
import com.example.hitshub.repositories.AuthRepository
import com.google.firebase.auth.AuthCredential

class RegisterViewModel : BaseViewModel(), LifecycleObserver {
    private val authRepository by lazy { AuthRepository() }

    fun singInWithGoogle(authCredential: AuthCredential) {
        _showSpinner.value = true
        authRepository.singInWithGoogle(authCredential)
        _authenticatedUserLiveData = authRepository.userData
        _showSpinner.value = false
    }

    fun createAccount(user: User) {
        _showSpinner.value = true
        authRepository.createNewUser(user)
        _userInfoLiveData = authRepository.userData
        _showSpinner.value = false
    }
}
