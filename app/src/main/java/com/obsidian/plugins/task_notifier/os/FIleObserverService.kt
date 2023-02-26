package com.obsidian.plugins.task_notifier.os

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.FileObserver
import android.os.IBinder
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.core.OnFileChangedResult
import com.obsidian.plugins.task_notifier.utils.Constants
import com.obsidian.plugins.task_notifier.utils.FileUtils
import com.obsidian.plugins.task_notifier.utils.Logger
import com.obsidian.plugins.task_notifier.utils.ScopeEnum
import java.io.File

class FileObserverService : Service() {
  private var mFileObserver: FileObserver? = null
  private var filePath: String? = null
  private var fileUri: Uri? = null

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Logger.info("FileObserverService.onStartCommand")
    val context: Context = this
    val uriString = intent.getStringExtra(ServiceManager.FILE_PATH_INTENT_KEY)
    if (uriString == null) {
      Logger.info("FileObserverService missing path from FILE_PATH_INTENT_KEY intent key")
      return STOP_FOREGROUND_REMOVE
    }

    Logger.info("FileObserverService started for uri ${uriString}")
    val uri = Uri.parse(uriString)
    fileUri = uri
    mFileObserver = getFileObserver(context, uri)
    (mFileObserver as FileObserver).startWatching() // The FileObserver starts watching

    val changeContent = FileUtils().tryReadFileContent(context, fileUri!!)

    ObsidianTaskReminderCore.onFileChanged(
      context,
      fileUri!!,
      changeContent
    )
    val notification = NotificationManager.notify(
      context,
      Constants.APPLICATION_NAME,
      "👀 Watching: $filePath",
      hashCode(),
      ScopeEnum.APPLICATION
    )
    startForeground(1, notification)
    return START_STICKY
  }

  override fun onBind(p0: Intent?): IBinder? {
    Logger.info("FileObserverService.onBind")
    TODO("Not yet implemented")
  }

  override fun onCreate() {
    super.onCreate()
    Logger.info("FileObserverService.onCreate")
  }

  override fun onDestroy() {
    super.onDestroy()
    Logger.info("FileObserverService.onDestroy")
  }

  private fun getFileObserver(context: Context, uri: Uri): FileObserver {
    val realPath = FileUtils().getPath(context, uri)
    Logger.info("FileObserverService created for file: $realPath ")
    filePath = realPath
    return object : FileObserver(File(realPath), FileObserver.CLOSE_WRITE) {
      override fun onEvent(event: Int, path: String?) {
        Logger.info("FileObserverService.onEvent $event path: $realPath")

        var changeContent = FileUtils().tryReadFileContent(context, fileUri!!)

        val fileChangeACKResult = ObsidianTaskReminderCore.onFileChanged(
          context,
          fileUri!!,
          changeContent
        )
        if (fileChangeACKResult === OnFileChangedResult.STOP_LISTENING) {
          Logger.info("FileObserverService stopping listening!")
          mFileObserver!!.stopWatching()
        }
      }
    }
  }
}
