package com.android.js;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class PermissionRequest {

    public static void checkAndAskForPermissions(Activity activity, Context context){
        String [] permissions = checkAndAskForPermissionsHelper(activity, context);
        if(permissions.length > 0)
            ActivityCompat.requestPermissions(activity, permissions, 1);
    }

    private static String[] checkAndAskForPermissionsHelper(Activity activity, Context context){

        String totalPermissions [] = retrievePermissions(activity);

        ArrayList<String> permissionsToBeGranted = new ArrayList<String>();
        for(String permission : totalPermissions) {

            switch (permission) {
                case "android.permission.CAMERA":
                    if (ContextCompat.checkSelfPermission(context, "Manigest") != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.CAMERA);
                    }
                    break;

                case "android.permission.ACCESS_NETWORK_STATE":
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.ACCESS_NETWORK_STATE);
                    }
                    break;
                case "android.permission.ACCESS_COARSE_LOCATION":
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    }
                    break;
                case "android.permission.ACCESS_FINE_LOCATION":
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    break;
                case "android.permission.WRITE_EXTERNAL_STORAGE":
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    break;
                case "android.permission.READ_EXTERNAL_STORAGE":
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    break;
                case "android.permission.RECORD_AUDIO":
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.RECORD_AUDIO);
                    }
                    break;
                case "android.permission.MODIFY_AUDIO_SETTINGS":
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToBeGranted.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
                    }
                    break;
            }
        }
        String[] permissions = new String[permissionsToBeGranted.size()];
        for(int i = 0; i < permissionsToBeGranted.size(); i++){
            permissions[i] = permissionsToBeGranted.get(i);
            System.out.println(permissions[i]);
        }

        return permissions;
    }

    private static String[] retrievePermissions(Context context) {
        try {
            return context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS)
                    .requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException ("This should have never happened.", e);
        }
    }
}
