package com.obsidian.plugins.task_notifier.os

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.FileObserver
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.core.OnFileChangedResult
import com.obsidian.plugins.task_notifier.utils.FileUtils
import com.obsidian.plugins.task_notifier.utils.Logger
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class FileObserverService : Service() {
  val CHANNEL_ID = "ForegroundServiceChannel"
  var NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID"
    private var mFileObserver: FileObserver? = null
    private var filePath: String? = null;
    private var fileUri: Uri? = null;

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Logger.info("FileObserverService.onStartCommand")
        val context: Context = this
        val uriString = intent.getStringExtra(ServiceManager.FILE_PATH_INTENT_KEY)
        if (uriString == null) {
            Logger.info("FileObserverService missing path from FILE_PATH_INTENT_KEY intent key")
            return STOP_FOREGROUND_REMOVE;
        }
        createForeGroundNotification(context);

        Logger.info("FileObserverService started for uri ${uriString}")
        val uri = Uri.parse(uriString);
        fileUri = uri;
        mFileObserver = getFileObserver(context, uri)
        (mFileObserver as FileObserver).startWatching() // The FileObserver starts watching
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
        Logger.info("FileObserverService created for file: ${realPath} ")
        filePath = realPath;
        return object : FileObserver(File(realPath), FileObserver.CLOSE_WRITE) {
            override fun onEvent(event: Int, path: String?) {
                Logger.info("FileObserverService.onEvent ${event} path: ${realPath}")

                var changeContent = tryReadFileContent(context, fileUri!!)

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

    private fun tryReadFileContent(context: Context, uri: Uri): String {
        var result = "";
        val fis = context.contentResolver.openInputStream(uri)
        val isr = InputStreamReader(fis)
        val bufferedReader = BufferedReader(isr)
        val sb = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        fis!!.close()
        result = sb.toString()
        return result;
    }


  private fun createForeGroundNotification(context: Context) {
    ensureNotificationChannelExists()
    val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_background)
      .setContentTitle("Obsidian Reminder running")
      .setContentText("background")
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    val notification = builder.build()
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1, notification)
    startForeground(1, notification)
  }

  private fun ensureNotificationChannelExists() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name: CharSequence = "OBSIDIAN_TASK_NOTIFICATIONS_NAME"
      val description = "OBSIDIAN_TASK_NOTIFICATIONS_DESCRIPTION"
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
      channel.description = description
      val notificationManager = getSystemService(
        NotificationManager::class.java
      )
      notificationManager.createNotificationChannel(channel)
    }
  }
}
