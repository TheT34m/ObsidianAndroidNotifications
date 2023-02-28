package com.obsidian.plugins.task_notifier.os

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.obsidian.plugins.task_notifier.core.bo.WatchedFolderBO
import com.obsidian.plugins.task_notifier.utils.Logger


class ServiceManager {
  companion object {
    const val FILE_PATH_INTENT_KEY = "FILE_PATH_INTENT_KEY"

    @JvmStatic()
    fun ensureAllPathsAreWatched(context: Context, folders: List<WatchedFolderBO>) {
      folders.forEach { folder: WatchedFolderBO -> startService(context, folder.uri) }
    }

    private fun startService(context: Context, uri: String) {
      Logger.info("ServiceManager starting FileObserverService for path $uri")
      val myIntent = Intent(context, FileObserverService::class.java)
      myIntent.putExtra(FILE_PATH_INTENT_KEY, uri)
      ContextCompat.startForegroundService(context, myIntent)
    }
  }
}
