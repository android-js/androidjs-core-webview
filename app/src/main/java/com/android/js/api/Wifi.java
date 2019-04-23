package com.android.js.api;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkSpecifier;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Wifi {
    private WifiManager main_wifi;
    private Activity activity;

    public Wifi(Activity activity){
        this.activity = activity;
        main_wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void enableWifi(){
        if(! this.main_wifi.isWifiEnabled())
            main_wifi.setWifiEnabled(true);
    }

    public void disableWifi(){
        if(this.main_wifi.isWifiEnabled())
            main_wifi.setWifiEnabled(false);
    }

    public void disconnectWifi(){
        main_wifi.disconnect();
    }

    public int getWifiState(){
        return main_wifi.getWifiState();
    }

    public boolean isWifiEnabled(){
        return main_wifi.isWifiEnabled();
    }

    public String getWifiScanResults() throws JSONException {
//        System.out.println("wifi api called");
        List<ScanResult> res = main_wifi.getScanResults();
        JSONArray final_res = new JSONArray();
        for(int i = 0; i < res.size(); i++) {
            JSONObject item = new JSONObject();
            item.put("SSID", res.get(i).SSID);
            item.put("BSSID", res.get(i).BSSID);
            item.put("capabilities", res.get(i).capabilities);
            item.put("level", res.get(i).level);
            item.put("frequency", res.get(i).frequency);
            item.put("timestamp", res.get(i).timestamp);
//            final_res[i].put("passpoint", res.get(i).isPasspointNetwork());
//            final_res[i].put("ChannelBandwidth", res.get(i).channelWidth);
//            final_res[i].put("centerFreq0", res.get(i).centerFreq0);
//            final_res[i].put("centerFreq1", res.get(i).centerFreq1);
//            final_res[i].put("80211mcResponder", res.get(i).is80211mcResponder());
//            System.out.println(res.get(i));
            final_res.put(item);
        }
        return final_res.toString();
    }

    public void connectWifi(String ssid, String password){
        System.out.println("Connect Called " + ssid);
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", ssid);
        if(password.equals(""))
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        else
            conf.preSharedKey = String.format("\"%s\"", password);
        int netId = main_wifi.addNetwork(conf);
        main_wifi.disconnect();
        main_wifi.enableNetwork(netId, true);
        main_wifi.reconnect();
    }

}
