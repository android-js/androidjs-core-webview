package com.android.js.api;

import android.app.Activity;
import android.os.Environment;

import static android.os.Environment.DIRECTORY_ALARMS;
import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.DIRECTORY_NOTIFICATIONS;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.DIRECTORY_PODCASTS;
import static android.os.Environment.DIRECTORY_RINGTONES;

public class App {
    private Activity activity;

    public App(Activity activity){
        this.activity = activity;
    }

    public String getPath(String name) {
        if (name.equals("root")) {
            return Environment.getRootDirectory().getPath();
        } else if (name.equals("data")) {
            return Environment.getDataDirectory().getPath();
        } else if (name.equals("cache")) {
            return Environment.getDownloadCacheDirectory().getPath();
        } else if (name.equals("storage")) {
            return Environment.getExternalStorageDirectory().getPath();
        } else if (name.equals("alarms")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_ALARMS).getPath();
        } else if (name.equals("dcim")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getPath();
        } else if (name.equals("downloads")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        } else if (name.equals("movies")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES).getPath();
        } else if (name.equals("music")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC).getPath();
        } else if (name.equals("notifications")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_NOTIFICATIONS).getPath();
        } else if (name.equals("pictures")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getPath();
        } else if (name.equals("podcasts")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS).getPath();
        } else if (name.equals("ringtones")) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_RINGTONES).getPath();
        } else if (name.equals("appData")) {
            return this.activity.getFilesDir().getPath();
        } else if (name.equals("userData")) {
            return this.activity.getExternalFilesDir(null).getPath();
        } else {
            return "-1";
        }
    }
}
