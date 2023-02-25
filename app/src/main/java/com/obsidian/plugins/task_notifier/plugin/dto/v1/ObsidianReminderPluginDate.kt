package com.obsidian.plugins.task_notifier.plugin.dto.v1

import com.obsidian.plugins.task_notifier.utils.Logger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ObsidianReminderPluginDate(dateString: String, dateTimeFormat: String, reminderTime: String) {
  var dateTime: LocalDateTime

  init {
    Logger.info("ObsidianReminderPluginDate parsing datestring: $dateString")
    val parser = DateTimeFormatter.ofPattern(dateTimeFormat.replace('Y', 'y').replace('D', 'd'))
    try {
      dateTime = LocalDateTime.parse(dateString, parser)
    } catch (e: Exception) {
      dateTime = LocalDateTime.parse(dateString + " " + reminderTime, parser)
    }
    Logger.info("ObsidianReminderPluginDate result of pase $dateTime")
  }
}
