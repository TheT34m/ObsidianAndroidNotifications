package com.obsidian.plugins.task_notifier.os

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.utils.ScopeEnum

class NotificationManager {
  companion object {
    var NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID"
    var NOTIFICATION_FOREGROUND_CHANNEL_ID = "OBSIDIAN_TASK_FOREGROUND_ID"

    @JvmStatic
    fun notify(context: Context, title: String, text: String, reqId: Int = 1, scope: ScopeEnum = ScopeEnum.REMINDER): Notification {
      val channel: String = this.getChannelFromScope(scope)
      this.ensureNotificationChannelExists(context, channel)
      val notification = this.createNotification(context, title, text, channel)
      val notificationManager = NotificationManagerCompat.from(context)
      notificationManager.notify(reqId, notification)
      return notification
    }

    fun createNotification(context: Context, title: String, text: String, channel: String): Notification {
      val builder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, channel)
          .setSmallIcon(R.drawable.ic_launcher_background)
          .setContentTitle(title)
          .setContentText(text)
          .setPriority(NotificationCompat.PRIORITY_DEFAULT)

      return builder.build()
    }

    private fun ensureNotificationChannelExists(context: Context, channel: String) {
      // Create the NotificationChannel, but only on API 26+ because
      // the NotificationChannel class is new and not in the support library
      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
        return
      }

      val name: CharSequence = "OBSIDIAN_TASK_NOTIFICATIONS_NAME"
      val description = "OBSIDIAN_TASK_NOTIFICATIONS_DESCRIPTION"
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(channel, name, importance)
      channel.description = description
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      val notificationManager: NotificationManager =
        context.getSystemService<NotificationManager>(
          NotificationManager::class.java
        )
      notificationManager.createNotificationChannel(channel)
    }

    fun getChannelFromScope(scope: ScopeEnum): String {
      if (scope.equals(ScopeEnum.REMINDER)) {
        return NOTIFICATION_CHANNEL_ID
      } else if (scope.equals(ScopeEnum.APPLICATION)) {
        return NOTIFICATION_FOREGROUND_CHANNEL_ID
      }
      return NOTIFICATION_CHANNEL_ID
    }
  }

}
