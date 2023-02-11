package com.obsidian.plugins.task_notifier.plugin

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.obsidian.plugins.task_notifier.plugin.dto.v1.ObsidianReminderPluginConfigDTO
import com.obsidian.plugins.task_notifier.utils.Logger
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
      val result: List<ObsidianReminderBO> = emptyList();
      val gson = GsonBuilder().registerTypeAdapter(
        LocalDateTime::class.java,
        JsonDeserializer<Any?> { json, typeOfT, context ->
          LocalDateTime.parse(
            json.asString,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
          )
        }).create()
      val obsidianConfig: ObsidianReminderPluginConfigDTO = gson.fromJson(json, ObsidianReminderPluginConfigDTO::class.java)

      obsidianConfig.reminders?.entries?.forEach { it ->
          it.value?.forEach { reminder ->
            if (reminder?.title != null) {
              result.plus(ObsidianReminderBO(reminder.title!!, reminder.time))
            };
          }
      }
      return result;
    }
  }
}
