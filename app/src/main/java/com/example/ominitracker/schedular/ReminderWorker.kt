package com.example.ominitracker.schedular

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.ominitracker.R
import com.example.ominitracker.ui.navigation.Routes
import com.example.ominitracker.util.Util

class ReminderWorker(val context: Context, param: WorkerParameters) : Worker(context, param) {

    override fun doWork(): Result {
        try {
            println("Text10: ReminderWorker started")
            showReminderNotification()
            startReminderService()
        } catch (e: Exception) {
            println("Text10: ReminderWorker has error: ${e.message}")
            return Result.failure()
        }
        return Result.success()
    }

    private fun startReminderService() {
        val serviceIntent = Intent(applicationContext, ReminderService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(serviceIntent)
        } else {
            applicationContext.startService(serviceIntent)
        }
    }

    private fun showReminderNotification() {

        val deepLinkUri = "${context.packageName}://${Routes.Reminder.route}"
        val deepLinkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUri))

        println("Text10: application package name: ${context.packageName}, deeplinkUri: ${deepLinkUri}")

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, Util.notificationChannelId)
            .setContentTitle("Reminder")
            .setContentText("Time to check your task!")
            .setSmallIcon(R.drawable.ic_reminder)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true) // Use this for fullscreen effect
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}