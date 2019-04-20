package com.android.js.api;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.android.js.MainActivity;

public class Call {
    private Intent callIntent;
    private Activity activity;

    public Call(Activity activity){
        this.callIntent = new Intent(Intent.ACTION_CALL);
        this.activity = activity;
    }

    public void makeCall(String number){
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[] {Manifest.permission.CALL_PHONE}, 2);
        }
        this.activity.startActivity(this.callIntent);
    }
}
