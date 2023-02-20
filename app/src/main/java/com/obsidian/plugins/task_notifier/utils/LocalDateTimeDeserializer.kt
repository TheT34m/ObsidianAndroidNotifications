package com.obsidian.plugins.task_notifier.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
  companion object {
    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
  }

  override fun deserialize(
    json: JsonElement,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalDateTime {
    return LocalDateTime.parse(json.asString, FORMATTER)
  }
}
