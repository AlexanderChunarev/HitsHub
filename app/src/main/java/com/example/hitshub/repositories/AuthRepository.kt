package com.example.hitshub.repositories

import androidx.lifecycle.MutableLiveData
import com.example.hitshub.models.User
import com.google.firebase.auth.AuthCredential

class AuthRepository : BaseRepository() {
    val userData by lazy { MutableLiveData<User>() }

    fun createNewUser(authenticatedUser: User) {
        val uidRef = userReference.document(authenticatedUser.uId)
        uidRef.get().addOnSuccessListener {
            if (!it!!.exists()) {
                uidRef.set(authenticatedUser).addOnSuccessListener {
                    authenticatedUser.isCreated = true
                    userData.value = authenticatedUser
                }
            }
        }
    }

    fun singInWithGoogle(authCredential: AuthCredential) {
        val user = User()
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener {
            val isNewUser = it.additionalUserInfo!!.isNewUser
            firebaseAuth.currentUser.apply {
                if (this != null) {
                    user.apply {
                        uId = uid
                        name = displayName.toString()
                        email = getEmail().toString()
                        avatarUrl = photoUrl.toString()
                        isNew = isNewUser
                    }
                    userData.value = user
                }
            }
        }
    }

    fun signInAnonymously() {
        val user = User()
        firebaseAuth.signInAnonymously().addOnSuccessListener {
            firebaseAuth.currentUser.apply {
                if (this != null) {
                    user.apply {
                        uId = uid
                        isAnonymous = true
                    }
                    userData.value = user
                }
            }
        }
    }
}
