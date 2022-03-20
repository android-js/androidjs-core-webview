package com.android.js.api;

import android.app.Activity;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class MobileData {
    private Activity activity;
    private TelephonyManager telephonyManager;

    public MobileData(Activity activity) {
        this.activity = activity;
        this.telephonyManager = (TelephonyManager) this.activity.getSystemService(this.activity.TELEPHONY_SERVICE);
    }

    public boolean isEnabled() {
        boolean flag = false;
        if (this.telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
                flag = Settings.Global.getInt(this.activity.getContentResolver(), "mobile_data", 1) == 1;
            else
                flag = Settings.Secure.getInt(this.activity.getContentResolver(), "mobile_data", 1) == 1;
        }
        return flag;
    }
}
