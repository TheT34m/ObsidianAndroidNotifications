package com.obsidian.plugins.task_notifier.os.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore;

public class BootUpReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    ObsidianTaskReminderCore.init(context);
  }
}
