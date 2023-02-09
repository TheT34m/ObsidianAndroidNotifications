package com.obsidian.plugins.task_notifier

import android.util.Log

class Logger {
    companion object {
        private val TAG = "Obsidian.Task.Notifier"

        @JvmStatic
        fun info(msg: String) {
            Log.i(TAG, msg)
        }
    }

}