package com.obsidian.plugins.task_notifier.os

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.FileObserver
import android.os.IBinder
import androidx.documentfile.provider.DocumentFile
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.core.OnFileChangedResult
import com.obsidian.plugins.task_notifier.utils.Logger

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class FileObserverService : Service() {
    private var mFileObserver: FileObserver? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Logger.info("FileObserverService.onStartCommand")
        val context: Context = this
        val uriString = intent.getStringExtra(ServiceManager.FILE_PATH_INTENT_KEY)
        if (uriString == null) {
            Logger.info("FileObserverService missing path from FILE_PATH_INTENT_KEY intent key")
            return STOP_FOREGROUND_REMOVE;
        }
        Logger.info("FileObserverService started for uri ${uriString}")
        val uri = Uri.parse(uriString);

        mFileObserver = getFileObserver(context, uriString)
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

    private fun getFileObserver(context: Context, uri: String): FileObserver {
        Logger.info("FileObserverService created for file: ${uri} ")
        return object : FileObserver(uri, ALL_EVENTS) {
            override fun onEvent(event: Int, path: String?) {
                Logger.info("FileObserverService.onEvent")
                if (path == null) return
                if (event != MODIFY) return
                Logger.info("FileObserverService on event $event path $path")

                var changeContent = tryReadFileContent(context, path)

                val fileChangeACKResult = ObsidianTaskReminderCore.onFileChanged(
                    context,
                    path,
                    changeContent
                )
                if (fileChangeACKResult === OnFileChangedResult.STOP_LISTENING) {
                    Logger.info("FileObserverService stopping listening!")
                    mFileObserver!!.stopWatching()
                }
            }
        }
    }

    private fun tryReadFileContent(context: Context, path: String): String {
        var result = ""
        try {
            val uri = Uri.parse(path)
            val document = DocumentFile.fromTreeUri(context, uri)
            for (file in document!!.listFiles()) {
                if (!file.isFile) continue
                if (!file.name!!.contains(path)) continue
                val fis = context.contentResolver.openInputStream(file.uri)
                val isr = InputStreamReader(fis)
                val bufferedReader = BufferedReader(isr)
                val sb = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                fis!!.close()
                result = sb.toString()
            }
        } catch (e: IOException) {
            Logger.info("FileObserverService cannot read file content $path e: $e")
            e.printStackTrace()
        }
        return result
    }
}