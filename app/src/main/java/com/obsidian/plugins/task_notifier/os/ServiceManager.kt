package com.obsidian.plugins.task_notifier.os

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.obsidian.plugins.task_notifier.utils.Logger


class ServiceManager {
    companion object {
        const val FILE_PATH_INTENT_KEY = "FILE_PATH_INTENT_KEY";

        @JvmStatic()
        fun ensureAllPathsAreWatched(context: Context, folders: List<String>) {
            folders.forEach { folder: String -> startService(context, folder) }
        }

        private fun startService(context: Context, path: String) {
            if (path.isNullOrEmpty()) {
                return
            }
            Logger.info("ServiceManager starting FileObserverService for path $path");
            val myIntent = Intent(context, FileObserverService::class.java)
            myIntent.putExtra(FILE_PATH_INTENT_KEY, path)
            ContextCompat.startForegroundService(context, myIntent)
        }
    }
}
