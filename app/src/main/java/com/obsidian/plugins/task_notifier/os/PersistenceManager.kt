package com.obsidian.plugins.task_notifier.os

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import com.obsidian.plugins.task_notifier.utils.Logger

class PersistenceManager {
    companion object {
        private val STORE_NAME = "Obsidian.Task.Reminder"
        private val KEY_WATCHED_FOLDERS = "WATCHED_FOLDERS"
        private val SEPARATOR = "$&$"

        fun getWatchedFolders(context: Context): List<String> {
            return getWatchedFoldersList(context)
        }

        fun addWatchedFolder(newFolder: Uri, context: Context) {
            val folders = getWatchedFoldersList(context)
            if (newFolder.path == null) {
                Logger.info("addWatchedFolder got newFolder.path as null!")
                return
            }
            val newPath: String = newFolder.path!!.trim()
            if (folders.contains(newPath)) {
                Logger.info("Folder ${newPath} is already watched folder")
                return
            }
            var newFoldersArray: List<String> = folders.plus(newPath)
            setWatchedFoldersList(newFoldersArray, context)
        }

        private fun setWatchedFoldersList(list: List<String>, context: Context) {
            val editor: SharedPreferences.Editor = getStore(context).edit()
            editor.putString(KEY_WATCHED_FOLDERS, list.joinToString(SEPARATOR))
            editor.commit()
        }

        private fun getWatchedFoldersList(context: Context): List<String> {
            val value = getStore(context).getString(KEY_WATCHED_FOLDERS, "")
            Logger.info("PersistenceManager.getWatchedFoldersList folders: $value")
            return value!!.split(SEPARATOR)
        }

        private fun getStore(context: Context): SharedPreferences {
            return context.getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        }
    }
}