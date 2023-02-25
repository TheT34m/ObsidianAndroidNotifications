package com.obsidian.plugins.task_notifier.os

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.obsidian.plugins.task_notifier.core.bo.ObsidianActiveReminderBO
import com.obsidian.plugins.task_notifier.utils.Logger
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class AlertManager {
  fun syncNotificationsAndAssignReqIds(
    context: Context,
    reminders: List<ObsidianActiveReminderBO>
  ): List<ObsidianActiveReminderBO> {
    cancelAllNotifications(context, reminders)
    return createNotifications(context, reminders)
  }

  private fun createNotifications(
    context: Context,
    reminders: List<ObsidianActiveReminderBO>
  ): List<ObsidianActiveReminderBO> {
    val reqIds: ArrayList<Int> = ArrayList()
    reminders.forEach { reminder ->
      if (reminder.dateTime < LocalDateTime.now()) return@forEach
      val reqId = UUID.randomUUID().hashCode()
      createNotification(context, reminder, reqId)
      reminder.reqId = reqId
      reqIds.add(reqId)
    }
    return reminders
  }

  private fun createNotification(
    context: Context,
    reminderBO: ObsidianActiveReminderBO,
    reqId: Int
  ) {
    val instant: Instant = reminderBO.dateTime.atZone(ZoneId.systemDefault()).toInstant()
    val date = Date.from(instant)
    val calendar = Calendar.getInstance()
    calendar.time = date

    val notificationIntent = Intent(context, ReminderBroadcast::class.java)
    notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL_ID, reqId)
    notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_TEXT, reminderBO.title)
    notificationIntent.putExtra(
      ReminderBroadcast.NOTIFICATION_TITLE,
      reminderBO.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    ) // TODO: fix notification message
    val pendingIntent =
      PendingIntent.getBroadcast(context, reqId, notificationIntent, PendingIntent.FLAG_MUTABLE)

    val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
  }

  private fun cancelAllNotifications(context: Context, reminders: List<ObsidianActiveReminderBO>) {
    try {
      reminders.forEach {
        if(it.reqId == null){
          Logger.info("ActiveReminderBO not yet has notification req id: ${it.title}")
          return
        }
        val notificationIntent = Intent(context, ReminderBroadcast::class.java)
        notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL_ID, it.reqId)
        val pendingIntent =
          PendingIntent.getBroadcast(
            context,
            it.reqId!!,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
          )
        pendingIntent.cancel()
      }
    } catch (e: Exception) {
      Logger.error("Cannot cancel notifications")
    }
  }
}
