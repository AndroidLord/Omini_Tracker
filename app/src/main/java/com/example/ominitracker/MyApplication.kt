package com.example.ominitracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.ominitracker.util.Util
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    Util.notificationChannelId,
                    "Reminder Channel",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for Reminder Notifications"
                },
                NotificationChannel(
                    Util.notificationServiceChannelId,
                    "Reminder Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Channel for Reminder Service Notifications"
                }
            )
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channels.forEach { channel ->
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

}