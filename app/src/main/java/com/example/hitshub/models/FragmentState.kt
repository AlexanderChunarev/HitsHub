package com.example.hitshub.models

data class FragmentState(
    var trackId: Long = 0,
    var trackTitle: String = "",
    var trackAuthor: String = "",
    var imageUrl: String = "",
    var tag: String = ""
)
