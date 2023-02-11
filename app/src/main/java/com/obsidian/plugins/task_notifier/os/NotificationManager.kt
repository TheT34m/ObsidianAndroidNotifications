package com.obsidian.plugins.task_notifier.os

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.obsidian.plugins.task_notifier.R

class NotificationManager {
    companion object {
        var NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID"

      @JvmStatic fun notify(context: Context, title: String, text: String, reqId: Int = 1) {
            this.ensureNotificationChannelExists(context);
            val notification = this.createNotification(context, title, text);
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(reqId, notification)
        }

      fun createNotification(context: Context, title: String, text: String, hashCode: Int): Notification {
        val builder: NotificationCompat.Builder =
          NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder.build();
      }

        private fun ensureNotificationChannelExists(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                return;
            }

            val name: CharSequence = "OBSIDIAN_TASK_NOTIFICATIONS_NAME"
            val description = "OBSIDIAN_TASK_NOTIFICATIONS_DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager: NotificationManager =
                context.getSystemService<NotificationManager>(
                    NotificationManager::class.java
                )
            notificationManager.createNotificationChannel(channel)
        }
    }
}
