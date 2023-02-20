package com.obsidian.plugins.task_notifier.utils

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {
  companion object {
    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
  }

  override fun serialize(
    src: LocalDateTime?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(src?.format(FORMATTER))
  }
}
