package com.obsidian.plugins.task_notifier.plugin.dto.v1

import com.google.gson.annotations.SerializedName

data class ObsidianReminderPluginActualReminderDTO(
  @SerializedName("title") var title: String,
  @SerializedName("time") var time: ObsidianReminderPluginDate,
  @SerializedName("rowNumber") var rowNumber: Int? = null
)
