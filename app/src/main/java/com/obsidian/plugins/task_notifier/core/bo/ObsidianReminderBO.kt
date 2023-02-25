package com.obsidian.plugins.task_notifier.core.bo

import java.time.LocalDateTime

data class ObsidianReminderBO(
  var title: String,
  var dateTime: LocalDateTime,
  var reqId: Int?,
)
