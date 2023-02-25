package com.obsidian.plugins.task_notifier.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
  companion object {
    val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
  }

  override fun deserialize(
    json: JsonElement,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalDateTime {
    Logger.info("LocalDateTimeDeserializer.deserialize $json")
    var result = LocalDateTime.now()
    try {
      result = LocalDateTime.parse(json.asString, FORMATTER)

    } catch (exception: Exception) {
      Logger.error("LocalDateTimeDeserializer.deserialize Failed to parse ${json}")
      result = LocalDateTime.now()
    }
    return result
  }
}
