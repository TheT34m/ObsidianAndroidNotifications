package com.obsidian.plugins.task_notifier.os

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.utils.Logger
import com.obsidian.plugins.task_notifier.utils.ScopeEnum

class NotificationManager {
  companion object {
    var CHANNEL_ACTIVE_REMINDER = "OBSIDIAN_ACTIVE_REMINDERS_CHANNEL"
    var CHANNEL_FOREGROUND_SERVICE = "OBSIDIAN_TASK_FOREGROUND_ID"

    var NOTIFICATION_INTENT_KEY_GUID = "NOTIFICATION_INTENT_KEY_GUID"

    @JvmStatic
    fun notify(
      context: Context,
      title: String,
      text: String,
      reqId: Int,
      scope: ScopeEnum
    ): Notification {
      Logger.info("NotificationManager.notify: $title $text $reqId $scope")

      val channel: String = this.getChannelFromScope(scope)
      this.ensureNotificationChannelExists(context, channel)

      val intent = getPendingIntent(context, scope)
      val notification = this.createNotification(context, intent, title, text, channel)

      val androidNotificationManager = NotificationManagerCompat.from(context)

      if (scope !== ScopeEnum.APPLICATION) { // TODO hacky hacky hack
        androidNotificationManager.notify(reqId, notification)
      }

      return notification
    }

    private fun getPendingIntent(context: Context, scope: ScopeEnum): Intent {
      var intent =
        context.packageManager.getLaunchIntentForPackage("com.obsidian.plugins.task_notifier")
      if (scope == ScopeEnum.REMINDER) {
        val obsidianIntent = context.packageManager.getLaunchIntentForPackage("md.obsidian")
        if (obsidianIntent == null) {
          Logger.error("Cannot open obsidian app since md.obisian package is not available!")
        } else {
          intent = obsidianIntent
        }
      }
      return intent!!
    }

    private fun createNotification(
      context: Context,
      intent: Intent,
      title: String,
      text: String,
      channel: String
    ): Notification {
      val pendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
      val builder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, channel)
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentIntent(pendingIntent)
          .setBadgeIconType(R.mipmap.ic_launcher)
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
      val notificationChannel = NotificationChannel(channel, name, importance)
      notificationChannel.description = description
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      val notificationManager: NotificationManager =
        context.getSystemService<NotificationManager>(
          NotificationManager::class.java
        )
      notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getChannelFromScope(scope: ScopeEnum): String {
      if (scope == ScopeEnum.REMINDER) {
        return CHANNEL_ACTIVE_REMINDER
      } else if (scope == ScopeEnum.APPLICATION) {
        return CHANNEL_FOREGROUND_SERVICE
      }
      return CHANNEL_ACTIVE_REMINDER
    }
  }

}
