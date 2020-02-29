package com.example.hitshub.viewmodels

import androidx.lifecycle.LifecycleObserver
import com.example.hitshub.models.User
import com.example.hitshub.repositories.AuthRepository
import com.example.hitshub.utils.MessageUtils
import com.example.hitshub.utils.ValidationUtils
import com.google.firebase.auth.AuthCredential

class RegisterViewModel : BaseViewModel(), LifecycleObserver {
    private val authRepository by lazy { AuthRepository() }

    fun singInWithGoogle(authCredential: AuthCredential) {
        _showSpinner.value = true
        authRepository.singInWithGoogle(authCredential)
        _authenticatedUserLiveData = authRepository.userData
        _showSpinner.value = false
    }

    fun signUpWithEmailAndPassword(email: String, password: String) {
        if (ValidationUtils.isEmailValid(email) && ValidationUtils.isPasswordValid(password)) {
            _showSpinner.value = true
            authRepository.singUpWithEmailAndPassword(email, password)
            _authenticatedUserLiveData = authRepository.userData
            _showSpinner.value = false
        } else {
            _message.value = MessageUtils.ErrorMessage(INVALID_EMAIL_OR_PASS)
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (ValidationUtils.isEmailValid(email) && ValidationUtils.isPasswordValid(password)) {
            _showSpinner.value = true
            authRepository.singInWithEmailAndPassword(email, password)
            _authenticatedUserLiveData = authRepository.userData
            _showSpinner.value = false
        } else {
            _message.value = MessageUtils.ErrorMessage(INVALID_EMAIL_OR_PASS)
        }
    }

    fun signInAnonymously() {
        _showSpinner.value = true
        authRepository.signInAnonymously()
        _authenticatedUserLiveData = authRepository.userData
        _showSpinner.value = false
    }

    fun saveUserToFireStore(user: User) {
        _showSpinner.value = true
        authRepository.saveUserToFireStore(user)
        _userInfoLiveData = authRepository.userData
        _showSpinner.value = false
    }

    companion object {
        const val INVALID_EMAIL_OR_PASS = "Invalid email or password"
    }
}
