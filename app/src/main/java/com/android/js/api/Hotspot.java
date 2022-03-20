package com.android.js.api;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;

import android.support.v4.app.ActivityCompat;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Hotspot {
    private WifiManager wifi_manager;
    private Activity activity;
    private WifiManager.LocalOnlyHotspotReservation local_reservation;

    public Hotspot(Activity activity) {
        this.activity = activity;
        this.wifi_manager = (WifiManager) (this.activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE));
    }

    public void enableHotspot(String ssid) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
