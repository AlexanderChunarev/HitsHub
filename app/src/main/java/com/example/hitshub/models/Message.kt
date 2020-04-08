package com.example.hitshub.models

data class Message(
    var name: String,
    var avatarUrl: String,
    var content: String,
    var trackId: Long,
    var time: Int
) {
    constructor() : this("", "", "", 0, 0)
}
