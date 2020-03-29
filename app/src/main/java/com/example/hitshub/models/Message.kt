package com.example.hitshub.models

data class Message(
    val name: String,
    val avatarUrl: String,
    val content: String,
    val trackId: Long,
    val time: Int
) {
    constructor() : this("", "", "", 0, 0)
}
