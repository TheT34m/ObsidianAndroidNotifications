package com.obsidian.plugins.task_notifier.os.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.os.NotificationManager
import com.obsidian.plugins.task_notifier.utils.Logger

class ReminderBroadcast : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val guid = intent.getIntExtra(NotificationManager.NOTIFICATION_INTENT_KEY_GUID,999)
    if( guid == 999) {
      Logger.error("ReminderBroadcast.onReceive: Failed to get notification guid from intent!")
    }
    ObsidianTaskReminderCore.onReminderBroadcast(context, guid)
  }
}

