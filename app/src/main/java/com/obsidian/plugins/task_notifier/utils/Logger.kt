package com.obsidian.plugins.task_notifier.utils

import android.util.Log

class Logger {
  companion object {
    private val TAG = "Obsidian.Task.Notifier"

    @JvmStatic
    fun info(msg: String) {
      Log.i(TAG, msg)
    }

    @JvmStatic
    fun error(msg: String) {
      Log.e(TAG, msg)
    }
  }

}
