package com.obsidian.plugins.task_notifier.core.bo

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.obsidian.plugins.task_notifier.utils.FileUtils
import com.obsidian.plugins.task_notifier.utils.LocalDateTimeDeserializer
import com.obsidian.plugins.task_notifier.utils.LocalDateTimeSerializer
import com.obsidian.plugins.task_notifier.utils.Logger
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.util.*

data class WatchedFolderBO(
  var uri: String,
  var nicePath: String,
  var guid: String,
)

class WatchedFoldersBO(json: String?) {
  private val listType: Type = object : TypeToken<ArrayList<WatchedFolderBO?>?>() {}.type
  private var folders: ArrayList<WatchedFolderBO> = arrayListOf()
  private val gson: Gson = GsonBuilder().create()

  init {
    if (json.isNullOrBlank()) {
      Logger.info("Cannot initialize watched folders. Empty raw source")
    } else {
      folders = gson.fromJson(json, listType)
    }
  }

  fun getFolders(): List<WatchedFolderBO> {
    return folders
  }

  fun remove(guid: String) {
    val filtered = folders.filter { it.guid != guid }
    folders = filtered as ArrayList<WatchedFolderBO>
    Logger.info("REMOVE after $folders")
  }

  fun addFolder(context: Context, newUri: Uri) {
    if (newUri.path == null) {
      Logger.info("addWatchedFolder got newFolder.path as null!")
      return
    }
    val newPath: String = newUri.toString()
    val sameFolder = folders.find { it.uri.equals(newUri) }
    if (sameFolder != null) {
      Logger.info("Folder $newPath is already watched folder")
      return
    }
    folders.add(
      WatchedFolderBO(
        newUri.toString(),
        FileUtils().getPath(context, newUri)!!,
        UUID.randomUUID().toString()
      )
    )
  }

  fun toJSON(): String {
    return gson.toJson(folders, listType)
  }
}
