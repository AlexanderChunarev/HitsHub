package com.example.hitshub.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_1_ID,
                AUDIO_NOTIFICATION,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    companion object {
        const val CHANNEL_1_ID = "channel1"
        const val AUDIO_NOTIFICATION = "audio_notification"
    }
}
