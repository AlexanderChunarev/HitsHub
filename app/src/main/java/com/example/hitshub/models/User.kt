package com.example.hitshub.models

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    var isNew: Boolean = false,
    @Exclude
    var isAnonymous: Boolean = false
) : Parcelable {

    constructor() : this("", "", "", "")
}
