package com.android.js.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Hotspot {
    private WifiManager wifi_manager;
    private Activity activity;
    private WifiManager.LocalOnlyHotspotReservation local_reservation;

    public Hotspot(Activity activity){
        this.wifi_manager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.activity = activity;
    }
    public void enableHotspot(String ssid) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wifi_manager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                    super.onStarted(reservation);
//                Timber.d("Wifi Hotspot is on now , reservation is : %s", reservation.toString());
                    local_reservation = reservation;
                    String key = local_reservation.getWifiConfiguration().preSharedKey;
                    String ussid = local_reservation.getWifiConfiguration().SSID;

                }

                @Override
                public void onStopped() {
                    super.onStopped();
//                Timber.d("onStopped: ");
                }

                @Override
                public void onFailed(int reason) {
                    super.onFailed(reason);
//                Timber.d("onFailed: ");
                }
            }, new Handler());
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            System.out.println("trying to start hotspot");
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = ssid;
            Method method = this.wifi_manager.getClass().getDeclaredMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            method.invoke(this.wifi_manager, conf, true);
        }
    }

    public void disableHotspot() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (this.local_reservation != null) {
                this.local_reservation.close();
            }
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
//            WifiConfiguration conf = new WifiConfiguration();
//            conf.SSID = ssid;
            Method method = this.wifi_manager.getClass().getDeclaredMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            method.invoke(this.wifi_manager, null, false);
        }
    }

    public boolean isHotspotEnabled(){
        try {
            Method method = this.wifi_manager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(this.wifi_manager);
        }
        catch (Throwable ignored) {}
        return false;
    }
}
