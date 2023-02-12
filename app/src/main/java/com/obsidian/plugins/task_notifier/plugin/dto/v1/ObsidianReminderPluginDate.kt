package com.obsidian.plugins.task_notifier.plugin.dto.v1

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ObsidianReminderPluginDate(dateString: String, dateTimeFormat: String, reminderTime: String) {
  var dateTime: LocalDateTime

  init {
    val parser = DateTimeFormatter.ofPattern(dateTimeFormat.replace('Y', 'y').replace('D', 'd'))
    try {
      dateTime = LocalDateTime.parse(dateString, parser)
    } catch (e: Exception) {
      dateTime = LocalDateTime.parse(dateString + " " + reminderTime, parser)
    }
  }
}
