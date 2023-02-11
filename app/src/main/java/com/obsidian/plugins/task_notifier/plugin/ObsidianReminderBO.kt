package com.obsidian.plugins.task_notifier.plugin

import java.time.LocalDateTime

data class ObsidianReminderBO(
  var title: String,
  var time: LocalDateTime?,
)
