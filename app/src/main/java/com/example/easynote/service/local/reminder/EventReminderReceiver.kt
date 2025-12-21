package com.example.easynote.service.local.reminder


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.easynote.R

class EventReminderReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {

        val title = intent.getStringExtra("title") ?: "Recordatorio"
        val message = intent.getStringExtra("message") ?: ""

        val channelId = "event_channel"

        val channel = NotificationChannel(
            channelId,
            "Event Reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.outline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), notification)
    }
}