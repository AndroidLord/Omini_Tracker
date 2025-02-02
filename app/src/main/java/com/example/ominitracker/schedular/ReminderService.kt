package com.example.ominitracker.schedular

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.ominitracker.R
import com.example.ominitracker.ui.navigation.Routes
import com.example.ominitracker.util.Util

class ReminderService : Service() {
    override fun onCreate() {
        super.onCreate()
        val notification = NotificationCompat.Builder(this, Util.notificationServiceChannelId)
            .setContentTitle("Reminder Service")
            .setContentText("This service is running in the foreground")
            .setSmallIcon(R.drawable.ic_reminder)
            .build()
        startForeground(2, notification) // Start foreground with a notification
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            println("Text10: ReminderService started")
            openReminderActivity()
        }
        catch (e: Exception) {
            println("Text10: ReminderService has error: ${e.message}")
            e.printStackTrace()
        }
        stopSelf()
        println("Text10: ReminderService stopped")
        return START_NOT_STICKY
    }
    private fun openReminderActivity() {
        val deepLinkUri = "${applicationContext.packageName}://${Routes.Reminder.route}"
        val reminderIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUri)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK // Required to start Activity from the background
        }
        applicationContext.startActivity(reminderIntent)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}


