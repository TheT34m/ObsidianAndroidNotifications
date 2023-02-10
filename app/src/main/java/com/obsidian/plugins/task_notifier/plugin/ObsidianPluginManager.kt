package com.obsidian.plugins.task_notifier.plugin

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.obsidian.plugins.task_notifier.plugin.dto.v1.ObsidianReminderPluginActualReminderDTO
import com.obsidian.plugins.task_notifier.plugin.dto.v1.ObsidianReminderPluginConfigDTO
import com.obsidian.plugins.task_notifier.utils.Logger
import java.text.SimpleDateFormat
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

            val jsonObject = Gson().fromJson(json, com.google.gson.JsonObject::class.java)
            val remindersObject = jsonObject.getAsJsonObject("reminders")
            for ((key, value) in remindersObject.entrySet()) {
                val reminderArray = value.asJsonArray
                for (i in 0 until reminderArray.size()) {
                    val reminder = reminderArray[i].asJsonObject
                    val title = reminder.get("title").asString
                    val time = reminder.get("time").asString
                    val simpleDateFormat =
                        SimpleDateFormat(obsidianConfig.settings!!.dateTimeFormat)
                    var date: Date? = null
                    try {
                        date = simpleDateFormat.parse(time)
                    } catch (e: java.lang.Exception) {
                    }
                    if (date == null) continue
                    result.plus(ObsidianReminderBO(title, date))
                }
            }
            return result;
        }
    }
}