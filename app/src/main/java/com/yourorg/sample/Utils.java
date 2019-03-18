package com.yourorg.sample;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.ACTIVITY_SERVICE;

public class Utils {

    public static void copyFolder(AssetManager am, String folderName, String dest){
        try{
            String files [] = am.list(folderName);
            if(files.length == 0){
                copyFile(am, folderName, dest);
            }else{
                File dir = new File(dest + "/" + folderName);
                if(! dir.exists()){
                    dir.mkdir();
                    for(String file :files){
                        copyFolder(am, folderName + "/" + file, dest);
                    }
                }
            }
        }catch (IOException e){
            Log.e("tag", "I/O Exception", e);
        }
    }

    public static void copyFile(AssetManager am, String filename, String dest){
        InputStream in = null;
        OutputStream out = null;
        try{
            in = am.open(filename);
            out = new FileOutputStream(dest + "/" + filename);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        }catch (IOException e){
            Log.e("tag","I/O Excetion", e);
        }
    }

    public static void copyAssetFile(AssetManager am, String src, String dest)
    {
        try {
            File destFile = new File(dest);
            if (!destFile.exists())
                destFile.createNewFile();

            InputStream in = am.open(src);
            FileOutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns the address of the first network interface that is running
    public static String getIP()
    {
        try
        {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            while (ni.hasMoreElements())
            {
                NetworkInterface i = ni.nextElement();
                if (!i.isLoopback() && i.isUp())
                {
                    for (InterfaceAddress ia: i.getInterfaceAddresses())
                    {
                        if (ia.getAddress().getAddress().length == 4) { // length = 16 IPv6
                            return ia.getAddress().toString();
                        }
                    }
                }

            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isServiceRunning(Context context, Class<?> clazz)
    {
        final ActivityManager am = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo i: am.getRunningServices(Integer.MAX_VALUE)) {
            if (i.service.getClassName().equals(clazz.getName()))
                return true;
        }

        return false;
    }

    public static int getPid(Context context, String processName) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo pi: am.getRunningAppProcesses()) {
            if(pi.processName.equals(processName))
                return pi.pid;
        }

        return -1;
    }
}
