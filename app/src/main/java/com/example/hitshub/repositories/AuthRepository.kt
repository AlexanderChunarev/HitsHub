package com.example.hitshub.repositories

import androidx.lifecycle.MutableLiveData
import com.example.hitshub.models.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class AuthRepository : BaseRepository() {
    val userData by lazy { MutableLiveData<User>() }

    fun saveUserToFireStore(authenticatedUser: User) {
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
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener {
            it.init()
        }
    }

    fun signInAnonymously() {
        firebaseAuth.signInAnonymously().addOnSuccessListener {
            it.init()
        }
    }

    fun singUpWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            it.init()
        }
    }

    fun singInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            it.init()
        }
    }

    private fun FirebaseUser.setUser(isNewUser: Boolean): User {
        return User().apply {
            uId = uid
            name = displayName.toString()
            email = getEmail().toString()
            avatarUrl = photoUrl.toString()
            isNew = isNewUser
            isAnonymous = isAnonymous()
        }
    }

    private fun AuthResult.init() {
        val isNewUser = this.additionalUserInfo!!.isNewUser
        firebaseAuth.currentUser.apply {
            if (this != null) {
                userData.value = setUser(isNewUser)
            }
        }
    }
}
