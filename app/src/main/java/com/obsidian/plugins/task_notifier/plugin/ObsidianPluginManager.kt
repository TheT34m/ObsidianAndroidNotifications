package com.obsidian.plugins.task_notifier.plugin

import android.net.Uri
import com.google.gson.Gson
import com.obsidian.plugins.task_notifier.plugin.dto.v1.ObsidianReminderPluginConfigDTO
import com.obsidian.plugins.task_notifier.utils.Logger
import java.util.Date

class ObsidianPluginManager {
    companion object {
        const val OBSIDIAN_REMINDER_PLUGIN_CONFIG_FILE_NAME = "data.json"

        @JvmStatic
        fun isInterestedFile(uri: Uri): Boolean {
            Logger.info(
                "ObsidianPluginManager uri ${uri.path} contains ${
                    uri.path?.contains(
                        OBSIDIAN_REMINDER_PLUGIN_CONFIG_FILE_NAME
                    )
                }"
            )
            if (uri.path == null || uri.path.isNullOrEmpty()) return false;
            return uri.path!!.contains(OBSIDIAN_REMINDER_PLUGIN_CONFIG_FILE_NAME)
        }

        @JvmStatic
        fun processFile(json: String): List<ObsidianReminderBO> {
            var result: List<ObsidianReminderBO> = emptyList();
            val gson = Gson()
            var obsidianConfig = gson.fromJson(json, ObsidianReminderPluginConfigDTO::class.java)
            obsidianConfig.reminders.forEach { reminder ->
                var titleCandidate = "N/A"
                if (reminder.title != null) {
                    titleCandidate = reminder.title.toString()
                }
                var timeCandidate = Date(reminder.time)
                result.plus(ObsidianReminderBO(titleCandidate, timeCandidate))
            }
            return result;
        }
    }
}