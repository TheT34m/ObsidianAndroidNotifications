package com.obsidian.plugins.task_notifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, FileObserverService.class);
        String INTENT_EXTRA_FILEPATH = "";
        myIntent.putExtra(INTENT_EXTRA_FILEPATH, "/storage/emulated/0/test/test");
        context.startService(myIntent);
    }
}