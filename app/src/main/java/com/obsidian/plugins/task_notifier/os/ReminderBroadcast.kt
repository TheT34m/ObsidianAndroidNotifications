package com.obsidian.plugins.task_notifier.os

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.service.notification.NotificationListenerService
import androidx.core.app.ServiceCompat
import android.content.Intent
import com.obsidian.plugins.task_notifier.os.NotificationManager.Companion.notify

class ReminderBroadcast : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val reqId = intent.getIntExtra(NOTIFICATION_CHANNEL_ID, 42)
    notify(
      context,
      intent.getStringExtra(NOTIFICATION_TITLE)!!,
      intent.getStringExtra(NOTIFICATION_TEXT)!!,
      reqId
    )
    PersistenceManager.removeActiveReminder(context, reqId)
  }

  companion object {
    var NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID"
    var NOTIFICATION_TEXT = "NOTIFICATION_TEXT"
    var NOTIFICATION_TITLE = "NOTIFICATION_TITLE"
  }
}
