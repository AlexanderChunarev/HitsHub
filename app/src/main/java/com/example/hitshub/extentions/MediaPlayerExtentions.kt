package com.example.hitshub.extentions

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer

fun MediaPlayer.setStreamingAttributes() {
    this.setAudioAttributes(
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .build()
    )
}

fun <E> MutableList<E>.next(index: Int, track: E): E? {
    val value = this.indexOf(track) + index
    return if (value >= 0 && value < this.size) this[value] else null
}
