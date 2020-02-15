package com.example.hitshub.models

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class User(
    var uId: String,
    var name: String,
    var email: String,
    var avatarUrl: String,
    @Exclude
    var isAuth: Boolean = false,
    @Exclude
    var isCreated: Boolean = false,
    @Exclude
    var isNew: Boolean = false
) : Serializable {

    constructor() : this("", "", "", "")
}
