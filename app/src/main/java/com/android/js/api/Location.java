package com.android.js.api;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;

public class Location implements LocationListener {
    private Activity activity;
    private LocationManager locationManager;
    private android.location.Location location;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    public Location(Activity activity) {
        this.activity = activity;
        this.locationManager = (LocationManager) this.activity.getSystemService(LOCATION_SERVICE);
    }

    private Boolean isGPSEnable() {
        return this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public String getLocation() {
        if (isGPSEnable()) {
            try {
                if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (this.location != null) {
                        return "{\"error\": false, \"latitude\": \"" + this.location.getLatitude() + "\", \"longitude\": \"" + this.location.getLongitude() + "\"}";
                    } else {
                        return "{\"error\": true, \"err\": \"Please try again..\"}";
                    }
                } else {
                    return "{\"error\": true, \"err\": \"Please try again..\"}";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\": true, \"err\": \""+ e.toString() + "\"}";
            }
        } else {
            return "{\"error\": true, \"msg\": \"GPS is disabled\"}";
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
