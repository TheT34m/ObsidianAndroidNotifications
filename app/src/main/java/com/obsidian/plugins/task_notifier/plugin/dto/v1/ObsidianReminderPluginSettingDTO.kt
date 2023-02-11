package com.obsidian.plugins.task_notifier.plugin.dto.v1

import com.google.gson.annotations.SerializedName

data class ObsidianReminderPluginSettingDTO(
    @SerializedName("reminderTime") var reminderTime: String? = null,
    @SerializedName("laters") var laters: String? = null,
    @SerializedName("useSystemNotification") var useSystemNotification: Boolean? = null,
    @SerializedName("autoCompleteTrigger") var autoCompleteTrigger: String? = null,
    @SerializedName("primaryReminderFormat") var primaryReminderFormat: String? = null,
    @SerializedName("enableReminderPluginReminderFormat") var enableReminderPluginReminderFormat: Boolean? = null,
    @SerializedName("dateFormat") var dateFormat: String? = null,
    @SerializedName("dateTimeFormat") var dateTimeFormat: String? = null,
    @SerializedName("strictDateFormat") var strictDateFormat: Boolean? = null,
    @SerializedName("linkDatesToDailyNotes") var linkDatesToDailyNotes: Boolean? = null,
    @SerializedName("enableTasksPluginReminderFormat") var enableTasksPluginReminderFormat: Boolean? = null,
    @SerializedName("useCustomEmojiForTasksPlugin") var useCustomEmojiForTasksPlugin: Boolean? = null,
    @SerializedName("removeTagsForTasksPlugin") var removeTagsForTasksPlugin: Boolean? = null,
    @SerializedName("enableKanbanPluginReminderFormat") var enableKanbanPluginReminderFormat: Boolean? = null,
    @SerializedName("editDetectionSec") var editDetectionSec: Int? = null,
    @SerializedName("reminderCheckIntervalSec") var reminderCheckIntervalSec: Int? = null
)