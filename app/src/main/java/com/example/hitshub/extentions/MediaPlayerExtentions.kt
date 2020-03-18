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
