package com.obsidian.plugins.task_notifier.plugin

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.obsidian.plugins.task_notifier.core.bo.ObsidianReminderBO
import com.obsidian.plugins.task_notifier.plugin.dto.v1.ObsidianReminderPluginConfigDTO
import com.obsidian.plugins.task_notifier.plugin.dto.v1.ObsidianReminderPluginDate
import com.obsidian.plugins.task_notifier.utils.Logger

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
    fun processFile(json: String): ArrayList<ObsidianReminderBO> {
      val jsonObject = Gson().fromJson(json, com.google.gson.JsonObject::class.java)
      val remindersObject = jsonObject.getAsJsonObject("settings")
      val dateTimeFormat = "YYYY-MM-DD HH:mm"
      val reminderTime = remindersObject["reminderTime"].asString

      val gson = GsonBuilder()
        .registerTypeAdapter(
          ObsidianReminderPluginDate::class.java,
          JsonDeserializer<Any?> { json, typeOfT, context ->
            ObsidianReminderPluginDate(json.asString, dateTimeFormat, reminderTime)
          }).create()

      val obsidianConfig: ObsidianReminderPluginConfigDTO =
        gson.fromJson(json, ObsidianReminderPluginConfigDTO::class.java)
      val result: ArrayList<ObsidianReminderBO> = ArrayList();

      obsidianConfig.reminders?.entries?.forEach { it ->
        it.value?.forEach { reminder ->
          if (reminder?.title != null) {
            result.add(ObsidianReminderBO(reminder.title, reminder.time.dateTime, null))
          };
        }
      }
      return result;
    }
  }
}
