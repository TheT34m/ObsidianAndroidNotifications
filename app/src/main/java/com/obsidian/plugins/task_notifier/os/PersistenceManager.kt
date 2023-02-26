package com.obsidian.plugins.task_notifier.os

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import com.obsidian.plugins.task_notifier.core.bo.ObsidianActiveReminderBO
import com.obsidian.plugins.task_notifier.core.bo.ObsidianActiveReminderBOFactory
import com.obsidian.plugins.task_notifier.core.bo.WatchedFoldersBO
import com.obsidian.plugins.task_notifier.utils.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class PersistenceManager {
  companion object {
    private const val STORE_NAME = "Obsidian.Task.Reminder"
    private const val KEY_WATCHED_FOLDERS = "WATCHED_FOLDERS"
    private const val ACTIVE_REMINDERS = "ACTIVE_REMINDERS"
    private const val SEPARATOR = "$&$"

    private lateinit var prefSubject: BehaviorSubject<SharedPreferences>

    private val prefChangeListener =
      SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
        prefSubject.onNext(sharedPreferences)
      }

    @JvmStatic
    fun init(context: Context) {
      val sharedPreferences = getStore(context)
      prefSubject = BehaviorSubject.createDefault(sharedPreferences)
      sharedPreferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    @JvmStatic
    fun getWatchedFolders(context: Context): List<String> {
      return getWatchedFoldersList(context).getPaths()
    }

    @JvmStatic
    fun getWatchedFolders(): Observable<List<String>> {
      return prefSubject.map { WatchedFoldersBO(it.getString(KEY_WATCHED_FOLDERS, "")).getPaths() }
    }

    @JvmStatic
    fun addWatchedFolder(newFolder: Uri, context: Context) {
      var folders = getWatchedFoldersList(context);
      folders.addFolder(newFolder)
      setWatchedFoldersList(folders, context)
    }

    @JvmStatic
    fun removeWatchedFolder(folderPathToRemove: String, context: Context) {
      var folders = getWatchedFoldersList(context);
      folders.remove(folderPathToRemove)
      setWatchedFoldersList(folders, context)
    }

    @JvmStatic
    private fun setWatchedFoldersList(watchedFolders: WatchedFoldersBO, context: Context) {
      val editor: SharedPreferences.Editor = getStore(context).edit()
      editor.putString(KEY_WATCHED_FOLDERS, watchedFolders.toString())
      editor.commit()
    }

    @JvmStatic
    private fun getWatchedFoldersList(context: Context): WatchedFoldersBO {
      val value = getStore(context).getString(KEY_WATCHED_FOLDERS, "")
      return WatchedFoldersBO(value!!)
    }

    @JvmStatic
    fun setActiveReminders(context: Context, reminders: List<ObsidianActiveReminderBO>) {
      val editor: SharedPreferences.Editor = getStore(context).edit()
      val json = ObsidianActiveReminderBOFactory().toJSON(reminders)
      Logger.info("PersistenceManager.setActiveReminders: $json")

      editor.putString(ACTIVE_REMINDERS, json)
      editor.commit()
    }

    @JvmStatic
    fun getActiveReminders(context: Context): List<ObsidianActiveReminderBO> {
      val value = getStore(context).getString(ACTIVE_REMINDERS, "")
      Logger.info("PersistenceManager.getActiveReminders: $value")
      return ObsidianActiveReminderBOFactory().fromJSON(value)
    }

    @JvmStatic
    fun getActiveReminders(): Observable<List<ObsidianActiveReminderBO>> {
      return prefSubject.map {
        ObsidianActiveReminderBOFactory().fromJSON(
          it.getString(
            ACTIVE_REMINDERS,
            ""
          )
        )
      }
    }

    fun removeActiveReminder(context: Context, guid: Int) {
      val reminders = this.getActiveReminders(context)
      val filteredReminders = reminders.filter { it.guid != guid }
      this.setActiveReminders(context, filteredReminders)
    }

    @JvmStatic
    private fun getStore(context: Context): SharedPreferences {
      return context.getSharedPreferences(STORE_NAME, MODE_PRIVATE)
    }
  }
}

