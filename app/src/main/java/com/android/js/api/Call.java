package com.android.js.api;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class Call {
    private Intent callIntent;
    private Activity activity;

    public Call(Activity activity){
        this.activity = activity;
        this.callIntent = new Intent(Intent.ACTION_CALL);

    }

    public void makeCall(String number){
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[] {Manifest.permission.CALL_PHONE}, 2);
        }else {
            this.activity.startActivity(this.callIntent);
        }
    }
}
