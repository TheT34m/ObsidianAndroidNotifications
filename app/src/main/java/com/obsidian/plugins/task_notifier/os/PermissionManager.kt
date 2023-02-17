package com.obsidian.plugins.task_notifier.os

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.utils.Logger

class PermissionManager {
  companion object {
    const val STORAGE_PERMISSION_REQUEST_CODE = 1

    @JvmStatic
    fun askPermissionForFolder(context: Activity) {
      val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/json"
      }
      context.startActivityForResult(intent, 1)
    }

    @JvmStatic
    fun ensureStoragePermission(activity: Activity) {
      // Check if we have write permission
      val permission: Int =
        ActivityCompat.checkSelfPermission(
          activity,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
      val permissions = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      )
      if (permission != PackageManager.PERMISSION_GRANTED) {
        // We don't have permission so prompt the user
        ActivityCompat.requestPermissions(
          activity,
          permissions,
          STORAGE_PERMISSION_REQUEST_CODE
        )
      }
    }

    @JvmStatic()
    fun onActivityResult(
      context: Context,
      requestCode: Int,
      resultCode: Int,
      intent: Intent?
    ) {
      if (resultCode != RESULT_OK || requestCode != STORAGE_PERMISSION_REQUEST_CODE) {
        Logger.info("Permission manager not processing intent resultCode $resultCode requestCode: $requestCode")
        return
      }
      if (intent == null || intent.data == null) {
        Logger.info("PermissionManager intent is empty or intent.data is empty")
        return
      }
      val contentResolver = context.contentResolver
      val fileUri = intent.data
      val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
      contentResolver.takePersistableUriPermission(fileUri!!, takeFlags)

      ObsidianTaskReminderCore.onWatchedPathAdded(context, fileUri)

    }


  }
}
