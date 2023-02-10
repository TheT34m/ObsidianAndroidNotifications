package com.obsidian.plugins.task_notifier.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.view.View
import androidx.annotation.RequiresApi
import com.obsidian.plugins.task_notifier.R
import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore
import com.obsidian.plugins.task_notifier.os.PermissionManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionManager.ensureStoragePermission(this);

        ObsidianTaskReminderCore.init(this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PermissionManager.onActivityResult(this, requestCode, resultCode, data)
    }

    fun addObsidianReminderConfig(view: View) {
        selectFolder()
    }

    fun selectFolder() {
         val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        startActivityForResult(intent, 1)
    }
}