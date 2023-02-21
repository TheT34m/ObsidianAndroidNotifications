package com.obsidian.plugins.task_notifier.core.bo

import android.net.Uri
import com.obsidian.plugins.task_notifier.utils.Logger

private val SEPARATOR = "$&$"

class WatchedFoldersBO(raw: String?) {
  private var folders: ArrayList<String> = arrayListOf()

  init {
    if (raw == null) {
      Logger.info("Cannot initialize watched folders. Empty raw source")
    } else {
      var splittedValues = raw.split(SEPARATOR)
      splittedValues = splittedValues.filter { s -> !s.isNullOrBlank() }
      Logger.info("Initializing WatchedFolders from: $splittedValues")
      folders.addAll(splittedValues)
    }
  }

  fun getPaths(): ArrayList<String> {
    return folders
  }

  fun remove(path: String) {
    Logger.info("Path to remove ${path}")
    Logger.info("REMOVE before ${folders}")
    folders.remove(path)
    Logger.info("REMOVE after ${folders}")

  }

  fun addFolder(newUri: Uri) {
    if (newUri.path == null) {
      Logger.info("addWatchedFolder got newFolder.path as null!")
      return
    }
    val newPath: String = newUri.toString()
    if (folders.contains(newPath)) {
      Logger.info("Folder ${newPath} is already watched folder")
      return
    }
    folders.add(newPath)
  }

  override fun toString(): String {
    return folders.joinToString(SEPARATOR)
  }
}
