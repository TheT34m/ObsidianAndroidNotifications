package com.obsidian.plugins.task_notifier.plugin.dto.v1

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ObsidianReminderPluginActualReminderDTO(
  @SerializedName("title") var title: String? = null,
  @SerializedName("time") var time: LocalDateTime? = null,
  @SerializedName("rowNumber") var rowNumber: Int? = null
)
