package com.example.hitshub.viewmodels

import androidx.lifecycle.LiveData
import com.example.hitshub.models.User
import com.example.hitshub.repositories.SplashRepository

class SplashViewModel : BaseViewModel() {
    private val splashRepository by lazy { SplashRepository() }
    lateinit var userAuntificationInfoLiveData: LiveData<User>
    lateinit var userLiveData: LiveData<User>

    fun setUserAuntificationInfo() {
        splashRepository.checkUserFirebaseAuth()
        userAuntificationInfoLiveData = splashRepository.userAuntificationData
    }

    fun setUserByUid(id: String) {
        splashRepository.fetchUser(id)
        userLiveData = splashRepository.fetchedUserData
    }
}
