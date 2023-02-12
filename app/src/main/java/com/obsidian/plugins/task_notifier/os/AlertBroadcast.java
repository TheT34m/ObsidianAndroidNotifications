package com.obsidian.plugins.task_notifier.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertBroadcast extends BroadcastReceiver {
  static String NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID";
  static String NOTIFICATION_TEXT = "NOTIFICATION_TEXT";
  static String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";

  @Override
  public void onReceive(Context context, Intent intent) {
    NotificationManager.notify(context, intent.getStringExtra(NOTIFICATION_TITLE), intent.getStringExtra(NOTIFICATION_TEXT), intent.getIntExtra(NOTIFICATION_CHANNEL_ID, 42));
  }
}
