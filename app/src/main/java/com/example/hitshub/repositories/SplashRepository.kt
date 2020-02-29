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

    fun fetchUser() {
        val user = User()
        firebaseAuth.currentUser.apply {
            if (this != null && this.isAnonymous) {
                fetchedUserData.value = user.apply {
                    uId = uid
                    isAnonymous = isAnonymous()
                }
            } else {
                userReference.document(this!!.uid).get().addOnSuccessListener { document ->
                    if (document?.exists()!!) {
                        fetchedUserData.value = document.toObject(User::class.java)!!
                    }
                }
            }
        }
    }
}
