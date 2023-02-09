package com.obsidian.plugins.task_notifier;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.FileObserver;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileObserverService extends Service
{
    private static final String INTENT_EXTRA_FILEPATH = "" ;
    String NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID";
    private FileObserver mFileObserver;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = this;
        System.out.println("service called");
        if((intent.hasExtra(INTENT_EXTRA_FILEPATH))) // we store the path of directory inside the intent that starts the service
            System.out.println("service called inside");
        mFileObserver = new FileObserver(intent.getStringExtra(INTENT_EXTRA_FILEPATH)) {
            @Override
            public void onEvent(int event, String path) {
                if(path == null) return;
                if(!path.endsWith(".md")) return;
                if(event != MODIFY) return;
                String changeContent = path;

                Uri myUri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3Atest%2Ftest");
                try {
                DocumentFile document = DocumentFile.fromTreeUri(context,myUri);
                for(DocumentFile file : document.listFiles()){
                    if(!file.isFile()) continue;
                    if(!file.getName().contains(path)) continue;
                    InputStream  fis = context.getContentResolver().openInputStream(file.getUri());
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    fis.close();
                    changeContent = sb.toString();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ensureNotificationChannelExists();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("File changed!")
                        .setContentText(changeContent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1, builder.build());
            }
        };
        mFileObserver.startWatching(); // The FileObserver starts watching
        return Service.START_NOT_STICKY;
    }

    private void ensureNotificationChannelExists() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "OBSIDIAN_TASK_NOTIFICATIONS_NAME";
            String description ="OBSIDIAN_TASK_NOTIFICATIONS_DESCRIPTION";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}