package com.obsidian.plugins.task_notifier.os

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.obsidian.plugins.task_notifier.plugin.ObsidianPluginManager
import com.obsidian.plugins.task_notifier.plugin.ObsidianReminderBO
import java.util.*
import kotlin.collections.ArrayList

class AlertManager {
  public fun syncNotifications(context: Context, reminders: ArrayList<ObsidianReminderBO>) {
    reminders.forEach { reminder ->
      this.createNotification(context, reminder);
    }
  }

  private fun createNotification(context: Context, reminderBO: ObsidianReminderBO) {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, reminderBO.time?.hour ?: 9) // TODO: get default day configuration
    calendar.set(Calendar.MINUTE, reminderBO.time?.minute ?: 0)  // TODO: get default day configuration
    val reqId = UUID.randomUUID().hashCode();
    val notificationIntent = Intent(context, AlertBroadcast::class.java)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_CHANNEL_ID, reqId)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_TEXT, reminderBO.title)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_TITLE, reminderBO.time.toString()) // TODO: fix notification message
    val pendingIntent = PendingIntent.getBroadcast(context, reqId, notificationIntent, PendingIntent.FLAG_MUTABLE);

    val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
    alarmManager.set(AlarmManager.RTC_WAKEUP , calendar.timeInMillis , pendingIntent) ;
  }

  private fun cancelAllNotifications(activeAlarmIds: java.util.ArrayList<Int>) {
    // TODO: Cancel all
  }

  private fun save() {
    // TODO: shared preferences
  }
}
