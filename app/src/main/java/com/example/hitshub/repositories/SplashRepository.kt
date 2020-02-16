package com.example.hitshub.repositories

import com.example.hitshub.models.User
import com.example.hitshub.viewmodels.SingleLiveEvent

class SplashRepository : BaseRepository() {
    val userAuntificationData = SingleLiveEvent<User>()
    val fetchedUserData = SingleLiveEvent<User>()

    fun checkUserFirebaseAuth() {
        val user = User()
        firebaseAuth.currentUser.apply {
            if (this != null) {
                userAuntificationData.value = user.apply {
                    isAuth = true
                    user.uId = uid
                }
            } else {
                userAuntificationData.value = user
            }
        }
    }

    fun fetchUser(id: String) {
        userReference.document(id).get().addOnSuccessListener { document ->
            if (document?.exists()!!) {
                fetchedUserData.value = document.toObject(User::class.java)!!
            }
        }
    }
}
