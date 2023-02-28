package com.obsidian.plugins.task_notifier.core.bo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.obsidian.plugins.task_notifier.utils.LocalDateTimeDeserializer
import com.obsidian.plugins.task_notifier.utils.LocalDateTimeSerializer
import com.obsidian.plugins.task_notifier.utils.Logger
import java.lang.reflect.Type
import java.time.LocalDateTime

data class ObsidianActiveReminderBO(
  var guid: Int,
  var title: String,
  var dateTime: LocalDateTime,
  var filePath: String,
  var fileRowNumber: Int,
)

class ObsidianActiveReminderBOFactory {
  private val listType: Type = object : TypeToken<ArrayList<ObsidianActiveReminderBO?>?>() {}.type
  private val gson: Gson = GsonBuilder()
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
    .create()

  fun toJSON(list: List<ObsidianActiveReminderBO>): String {
    return gson.toJson(list, listType)
  }

  fun fromJSON(json: String?): List<ObsidianActiveReminderBO> {
    Logger.info("ObsidianActiveReminderBOFactory.fromJSON: $json")
    if (json.isNullOrBlank()) return emptyList()
    return gson.fromJson(json, listType)
  }
}
