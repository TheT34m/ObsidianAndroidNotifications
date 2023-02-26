package com.obsidian.plugins.task_notifier.core

import android.content.Context
import android.net.Uri
import com.obsidian.plugins.task_notifier.core.bo.ObsidianActiveReminderBO
import com.obsidian.plugins.task_notifier.os.AlarmManager
import com.obsidian.plugins.task_notifier.os.NotificationManager
import com.obsidian.plugins.task_notifier.os.PersistenceManager
import com.obsidian.plugins.task_notifier.os.ServiceManager
import com.obsidian.plugins.task_notifier.plugin.ObsidianPluginManager
import com.obsidian.plugins.task_notifier.utils.Constants
import com.obsidian.plugins.task_notifier.utils.Logger
import com.obsidian.plugins.task_notifier.utils.ScopeEnum
import java.util.*

class ObsidianTaskReminderCore {
  companion object {
    @JvmStatic
    fun init(context: Context) {
      Logger.info("ObsidianTaskReminderCore.init")
      PersistenceManager.init(context)
      val folders = PersistenceManager.getWatchedFolders(context)
      ServiceManager.ensureAllPathsAreWatched(context, folders)

      val reminders = PersistenceManager.getActiveReminders(context)
      AlarmManager().syncAlarmsWithReminders(context, reminders)
    }

    @JvmStatic
    fun onFileChanged(context: Context, uri: Uri, content: String): OnFileChangedResult {
      Logger.info("ObsidianTaskReminderCore.onFileChanged path: ${uri.path} content: $content")

      if (Constants.IS_DEBUG_MODE) {
        NotificationManager.notify(
          context,
          "file changed at",
          "path ${uri.path}",
          1,
          ScopeEnum.APPLICATION
        )
      }

      val folders = PersistenceManager.getWatchedFolders(context)
      if (!folders.contains(uri.toString())) return OnFileChangedResult.STOP_LISTENING
      try {
        val reminders = ObsidianPluginManager.processFile(content)
        AlarmManager().syncAlarmsWithReminders(context, reminders)
        PersistenceManager.setActiveReminders(context, reminders)
      } catch (e: Exception) {
        Logger.info("Failed to process reminders. Error: ${e.message} ${e.cause}")
      }
      return OnFileChangedResult.ACK
    }

    @JvmStatic
    fun onWatchedPathAdded(context: Context, uri: Uri) {
      Logger.info("ObsidianTaskReminderCore.onWatchedPathAdded")
      if (!ObsidianPluginManager.isInterestedFile(uri)) return

      if (Constants.IS_DEBUG_MODE) {
        NotificationManager.notify(
          context,
          "folder added",
          "path ${uri.path}",
          UUID.randomUUID().hashCode(),
          ScopeEnum.APPLICATION
        )
      }

      PersistenceManager.addWatchedFolder(uri, context)
      val folders = PersistenceManager.getWatchedFolders(context)
      ServiceManager.ensureAllPathsAreWatched(context, folders)
    }

    @JvmStatic
    fun onReminderBroadcast(
      context: Context,
      guid: Int,
    ) {
      val reminders = PersistenceManager.getActiveReminders(context).filter { it.guid == guid }
      if (reminders.isEmpty()) {
        Logger.error("No Active Reminder found with guid $guid")
        return
      }
      val reminder = reminders.first()
      NotificationManager.notify(
        context,
        reminder.title,
        reminder.dateTime.toString(),
        reminder.guid,
        ScopeEnum.REMINDER
      )
      PersistenceManager.removeActiveReminder(context, guid)
    }

    @JvmStatic
    fun removeFolder(context: Context, folderPath: String) {
      Logger.info("Removing watched folder $folderPath")
      PersistenceManager.removeWatchedFolder(folderPath, context)
      val folders = PersistenceManager.getWatchedFolders(context)
      ServiceManager.ensureAllPathsAreWatched(context, folders)
    }

    @JvmStatic
    fun removeActiveReminder(context: Context, reminder: ObsidianActiveReminderBO) {
      PersistenceManager.removeActiveReminder(context, reminder.guid)
      val activeReminders = PersistenceManager.getActiveReminders(context)
      AlarmManager().syncAlarmsWithReminders(context, activeReminders)
      PersistenceManager.setActiveReminders(context, activeReminders)
    }
  }
}

enum class OnFileChangedResult {
  ACK,
  STOP_LISTENING,
}
