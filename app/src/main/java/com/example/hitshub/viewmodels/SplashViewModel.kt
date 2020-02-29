package com.example.hitshub.viewmodels

import com.example.hitshub.repositories.SplashRepository

class SplashViewModel : BaseViewModel() {
    private val splashRepository by lazy { SplashRepository() }

    fun setUserAuntificationInfo() {
        splashRepository.checkUserFirebaseAuth()
        _authenticatedUserLiveData = splashRepository.userAuntificationData
    }

    fun setUser() {
        splashRepository.fetchUser()
        _userInfoLiveData = splashRepository.fetchedUserData
    }
}
