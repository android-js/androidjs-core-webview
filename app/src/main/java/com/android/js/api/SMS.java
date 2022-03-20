package com.android.js.api;

import android.app.Activity;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class SMS {
    private SmsManager smsManager;
    private Activity activity;

    public SMS(Activity activity) {
        this.activity = activity;
        this.smsManager = SmsManager.getDefault();
    }

    public String sendSMS(String number, String message) {
        try {
            ArrayList<String> messageParts = this.smsManager.divideMessage(message);
            this.smsManager.sendMultipartTextMessage(number, null, messageParts, null, null);
            return "{\"error\": false, \"msg\": \"Message Sent\"}";
        } catch (Exception e){
            e.printStackTrace();
            return "{\"error\": true, \"msg\": \"" + e.getMessage().toString() + "\"}";
        }
    }
}
