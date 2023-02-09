package com.obsidian.plugins.task_notifier.core

import android.content.Context
import com.obsidian.plugins.task_notifier.os.NotificationManager
import com.obsidian.plugins.task_notifier.os.PersistenceManager
import com.obsidian.plugins.task_notifier.os.ServiceManager

class ObsidianTaskReminderCore {
    companion object {
        @JvmStatic
        fun init(context: Context) {
            val folders = PersistenceManager.getWatchedFolders(context);
            ServiceManager.ensureAllPathsAreWatched(context, folders);
        }

        @JvmStatic
        fun onFileChanged(context: Context, path: String) {
            NotificationManager.notify(context, "file changed at", "path")
        }
    }
}