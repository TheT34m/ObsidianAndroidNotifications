package com.obsidian.plugins.task_notifier.os

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.GsonBuilder
import com.obsidian.plugins.task_notifier.plugin.ObsidianReminderBO
import com.obsidian.plugins.task_notifier.plugin.PersistentObsidianReminderBO
import com.obsidian.plugins.task_notifier.utils.LocalDateTimeDeserializer
import com.obsidian.plugins.task_notifier.utils.LocalDateTimeSerializer
import com.obsidian.plugins.task_notifier.utils.Logger
import java.time.LocalDateTime


class PersistenceManager {
  companion object {
    private val STORE_NAME = "Obsidian.Task.Reminder"
    private val KEY_WATCHED_FOLDERS = "WATCHED_FOLDERS"
    private val ACTIVE_REMINDERS = "ACTIVE_REMINDERS"
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
      val newPath: String = newFolder.toString()
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
      return context.getSharedPreferences(STORE_NAME, MODE_PRIVATE)
    }

    fun addActiveReminders(context: Context, reminders: List<ObsidianReminderBO>) {
      val editor: SharedPreferences.Editor = getStore(context).edit()
      val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .create()

      val json = gson.toJson(PersistentObsidianReminderBO(reminders))
      editor.putString(ACTIVE_REMINDERS, json)
      editor.commit()
    }

    fun getActiveReminders(context: Context): List<ObsidianReminderBO> {
      val value = getStore(context).getString(ACTIVE_REMINDERS, "")
      if (value == "" || value == null) return emptyList()
      val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .create()

      val reminders: PersistentObsidianReminderBO = gson.fromJson(value, PersistentObsidianReminderBO::class.java)
      Logger.info("PersistenceManager.getActiveAlerts ids: $value")
      return reminders.reminders;
    }

    fun removeActiveReminder(context: Context, reqId: Int) {
      val reminders = this.getActiveReminders(context)
      val filteredReminders = reminders.filter { it.reqId != reqId }
      this.addActiveReminders(context, filteredReminders)
    }
  }
}

