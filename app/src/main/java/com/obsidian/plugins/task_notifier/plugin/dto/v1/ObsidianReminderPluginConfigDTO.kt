package com.obsidian.plugins.task_notifier.plugin.dto.v1

import com.google.gson.annotations.SerializedName

data class ObsidianReminderPluginConfigDTO(
    @SerializedName("scanned") var scanned: Boolean? = null,
    @SerializedName("reminders") var reminders: Object,
    @SerializedName("debug") var debug: Boolean? = null,
    @SerializedName("settings") var settings: ObsidianReminderPluginSettingDTO? = ObsidianReminderPluginSettingDTO()
)