package com.obsidian.plugins.task_notifier.plugin.dto.v1


class ObsidianReminderPluginConfigDTO(
  var scanned: Boolean = false,
  var reminders: LinkedHashMap<String?, ArrayList<ObsidianReminderPluginActualReminderDTO?>?>? = null,
  var debug: Boolean = false
)
