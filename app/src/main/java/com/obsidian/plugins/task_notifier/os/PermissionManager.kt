package com.obsidian.plugins.task_notifier.os

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.utils.Logger
import kotlin.concurrent.thread

class PermissionManager {
    companion object {
        const val STORAGE_PERMISSION_REQUEST_CODE = 1;

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
            data: Intent?
        ) {
            if (resultCode != RESULT_OK || requestCode != STORAGE_PERMISSION_REQUEST_CODE) {
                Logger.info("Permission manager not processing intent resultCode $resultCode requestCode: $requestCode")
                return;
            }
            if (data == null) {
                Logger.info("PermissionManager intent is empty")
                return
            }
            data.data?.let { treeUri ->
                context.contentResolver.takePersistableUriPermission(
                    treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                readSDK30(context, treeUri)
            }
        }

        private fun readSDK30(context: Context, treeUri: Uri) {
            val tree = DocumentFile.fromTreeUri(context, treeUri)!!

            thread {
                val uriList = arrayListOf<Uri>()
                listFiles(tree).forEach { uri ->
                    ObsidianTaskReminderCore.onWatchedPathAdded(context, uri)
                    // Collect all the Uri from here
                }

            }
        }

        private fun listFiles(folder: DocumentFile): List<Uri> {
            return if (folder.isDirectory) {
                folder.listFiles().mapNotNull { file ->
                    if (file.name != null) file.uri else null
                }
            } else {
                emptyList()
            }
        }
    }
}