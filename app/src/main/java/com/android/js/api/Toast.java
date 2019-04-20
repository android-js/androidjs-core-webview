package com.android.js.api;

import android.app.Activity;

public class Toast {
    public static void showToast(Activity activity, String text, int duration){
        if(duration > 1) duration = 1;
        if(duration < 0) duration = 0;
        android.widget.Toast toast = android.widget.Toast.makeText(activity, text, duration);
        toast.show();
    }
}
