package com.obsidian.plugins.task_notifier.os;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.FileObserver;
import android.os.IBinder;

import androidx.documentfile.provider.DocumentFile;

import com.obsidian.plugins.task_notifier.core.ObsidianTaskReminderCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileObserverService extends Service {
    private FileObserver mFileObserver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = this;
        System.out.println("service called");

        if ((intent.hasExtra(ServiceManager.FILE_PATH_INTENT_KEY))) // we store the path of directory inside the intent that starts the service
            System.out.println("service called inside");
        String path = intent.getStringExtra(ServiceManager.FILE_PATH_INTENT_KEY);
        mFileObserver = new FileObserver(path) {
            @Override
            public void onEvent(int event, String path) {
                if (path == null) return;
                if (!path.endsWith(".md")) return;
                if (event != MODIFY) return;
                String changeContent = path;

                Uri myUri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3Atest%2Ftest");
                try {
                    DocumentFile document = DocumentFile.fromTreeUri(context, myUri);
                    for (DocumentFile file : document.listFiles()) {
                        if (!file.isFile()) continue;
                        if (!file.getName().contains(path)) continue;
                        InputStream fis = context.getContentResolver().openInputStream(file.getUri());
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
                ObsidianTaskReminderCore.onFileChanged(context, path);

            }
        };
        mFileObserver.startWatching(); // The FileObserver starts watching
        return Service.START_NOT_STICKY;
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