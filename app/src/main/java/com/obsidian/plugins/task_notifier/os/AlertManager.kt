package com.obsidian.plugins.task_notifier.os

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.obsidian.plugins.task_notifier.plugin.ObsidianReminderBO
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


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
    val instant: Instant = reminderBO.dateTime.atZone(ZoneId.systemDefault()).toInstant()
    val date = Date.from(instant)
    val calendar = Calendar.getInstance()
    calendar.time = date

    val notificationIntent = Intent(context, AlertBroadcast::class.java)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_CHANNEL_ID, reqId)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_TEXT, reminderBO.title)
    notificationIntent.putExtra(AlertBroadcast.NOTIFICATION_TITLE,  reminderBO.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))) // TODO: fix notification message
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
