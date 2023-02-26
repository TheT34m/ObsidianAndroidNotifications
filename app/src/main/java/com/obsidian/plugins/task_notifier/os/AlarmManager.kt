package com.obsidian.plugins.task_notifier.os

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.obsidian.plugins.task_notifier.core.bo.ObsidianActiveReminderBO
import com.obsidian.plugins.task_notifier.os.broadcast.ReminderBroadcast
import com.obsidian.plugins.task_notifier.utils.Logger
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class AlarmManager {
  fun syncAlarmsWithReminders(
    context: Context,
    reminders: List<ObsidianActiveReminderBO>
  ) {
    cancelAllScheduledAlarms(context, reminders)
    createNotifications(context, reminders)
  }

  private fun createNotifications(
    context: Context,
    reminders: List<ObsidianActiveReminderBO>
  ): List<ObsidianActiveReminderBO> {
    reminders.forEach { reminder ->
      if (reminder.dateTime < LocalDateTime.now()) {
        Logger.info("AlarmManager.createNotifications not creating alarm for $reminder")
        return@forEach
      }
      scheduleAlarm(context, reminder)
    }
    return reminders
  }

  private fun scheduleAlarm(
    context: Context,
    reminderBO: ObsidianActiveReminderBO,
  ) {
    val instant: Instant = reminderBO.dateTime.atZone(ZoneId.systemDefault()).toInstant()
    val date = Date.from(instant)
    val calendar = Calendar.getInstance()
    calendar.time = date

    val notificationIntent = Intent(context, ReminderBroadcast::class.java)
    notificationIntent.putExtra(NotificationManager.NOTIFICATION_INTENT_KEY_GUID, reminderBO.guid)

    val pendingIntent =
      PendingIntent.getBroadcast(
        context,
        reminderBO.guid,
        notificationIntent,
        PendingIntent.FLAG_MUTABLE
      )
    Logger.info("AlarmManager.scheduleAlarm $reminderBO")
    val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
  }

  private fun cancelAllScheduledAlarms(
    context: Context,
    reminders: List<ObsidianActiveReminderBO>
  ) {
    reminders.forEach { it ->
      if (it.dateTime < LocalDateTime.now()) {
        Logger.info("AlarmManager.cancelAllScheduledAlarms not cancelling alarm for $it")
        return@forEach
      }
      try {
        val notificationIntent = Intent(context, ReminderBroadcast::class.java)
        notificationIntent.putExtra(NotificationManager.NOTIFICATION_INTENT_KEY_GUID, it.guid)
        val pendingIntent =
          PendingIntent.getBroadcast(
            context,
            it.guid,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
          )
        pendingIntent.cancel()
      } catch (e: Exception) {
        Logger.error("Cannot cancel alarm for reminder ${it.guid}. e: ${e.message}")
      }
    }
  }
}
