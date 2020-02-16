package com.example.hitshub.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

abstract class BaseRepository {
    private val settings = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build()
    private val db = FirebaseFirestore.getInstance().apply {
        this.firestoreSettings = settings
    }

    protected val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    protected val userReference = db.collection(USERS)

    companion object {
        const val USERS = "users"
    }
}
