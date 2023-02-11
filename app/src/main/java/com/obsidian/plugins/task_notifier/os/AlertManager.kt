package com.obsidian.plugins.task_notifier.os

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.obsidian.plugins.task_notifier.plugin.ObsidianReminderBO
import java.util.*
import kotlin.collections.ArrayList

class AlertManager {
  public fun syncNotifications(context: Context, reminders: List<ObsidianReminderBO>) {
    cancelAllNotifications(context);
    val reqIds = createNotifications(context, reminders);
    save(context, reqIds)
  }

  private fun createNotifications(context: Context, reminders: List<ObsidianReminderBO>): List<Int> {
    val reqIds: ArrayList<Int> = ArrayList();
    reminders.forEach { reminder ->
      val reqId = UUID.randomUUID().hashCode();
      createNotification(context, reminder, reqId)
      reqIds.add(reqId);
    }
    return reqIds;
  }

  private fun createNotification(context: Context, reminderBO: ObsidianReminderBO, reqId: Int) {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, reminderBO.time?.hour ?: 9) // TODO: get default day configuration
    calendar.set(Calendar.MINUTE, reminderBO.time?.minute ?: 0)  // TODO: get default day configuration
    val notificationIntent = Intent(context, AlertBroadcast::class.java)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_CHANNEL_ID, reqId)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_TEXT, reminderBO.title)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_TITLE, reminderBO.time.toString()) // TODO: fix notification message
    val pendingIntent = PendingIntent.getBroadcast(context, reqId, notificationIntent, PendingIntent.FLAG_MUTABLE);

    val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
    alarmManager.set(AlarmManager.RTC_WAKEUP , calendar.timeInMillis , pendingIntent) ;
  }

  private fun cancelAllNotifications(context: Context) {
    val reqIds = PersistenceManager.getActiveAlerts(context)
    reqIds.forEach {
      val notificationIntent = Intent(context, AlertBroadcast::class.java)
      notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_CHANNEL_ID, it)
      val pendingIntent = PendingIntent.getBroadcast(context, it, notificationIntent, PendingIntent.FLAG_MUTABLE);
      pendingIntent.cancel()
    }
    // reset the list
    PersistenceManager.addActiveAlerts(context, ArrayList())
  }

  private fun save(context: Context, reqIds: List<Int>) {
    PersistenceManager.addActiveAlerts(context, reqIds)
  }
}
