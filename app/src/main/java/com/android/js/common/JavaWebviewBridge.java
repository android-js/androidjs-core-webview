package com.android.js.common;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;

import com.android.js.api.IO;
import com.android.js.api.App;
import com.android.js.api.SMS;
import com.android.js.api.Wifi;
import com.android.js.api.Call;
import com.android.js.api.Toast;
import com.android.js.api.Contact;
import com.android.js.api.Hotspot;
import com.android.js.api.DeepLink;
import com.android.js.api.Location;
import com.android.js.api.MobileData;
import com.android.js.api.Notification;

import android.content.pm.ActivityInfo;
import android.telephony.TelephonyManager;

import android.view.View;
import android.view.Window;
import android.content.Context;
import android.view.WindowManager;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

public class JavaWebviewBridge {
    private int iconId;
    private Activity activity;
    private WebView myWebView;
    
    private IO io;
    private App app;
    private SMS sms;
    private Call call;
    private Wifi wifi;
    private Toast toast;
    private Hotspot hotspot;
    private Contact contact;
    private String className;
    private DeepLink deepLink;
    private Location location;
    private MobileData mobileData;
    private Notification notification;
    
    public JavaWebviewBridge(Activity activity, WebView myWebView, int iconId, String className){
        this.iconId = iconId;
        this.activity = activity;
        this.myWebView = myWebView;
        this.className = className;
        
        this.io = new IO(activity);
        this.app = new App(activity);
        this.sms = new SMS(activity);
        this.call = new Call(activity);
        this.wifi = new Wifi(activity);
        this.toast = new Toast(activity);
        this.contact = new Contact(activity);
        this.hotspot = new Hotspot(activity);
        this.deepLink = new DeepLink(activity);
        this.location = new Location(activity);
        this.mobileData = new MobileData(activity);
        this.notification = new Notification(activity, iconId, className);
    }
        
    @JavascriptInterface
    public void setVolume( int volume ) {
    	AudioManager audioManager = (AudioManager) this.activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume( AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI );
    }
    
    @JavascriptInterface
    public void increaseVolume() {
    	AudioManager audioManager = (AudioManager) this.activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.adjustVolume( AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI );
    }
    
    @JavascriptInterface
    public void decreaseVolume() {
    	AudioManager audioManager = (AudioManager) this.activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.adjustVolume( AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI );   
    }
    
    @JavascriptInterface
    public void webviewClearCache( boolean disk ){
        this.myWebView.clearCache( disk );
    }
    
    @JavascriptInterface
    public void webviewClearFormatData(){
        this.myWebView.clearFormData();
    }
    
    @JavascriptInterface
    public void webviewClearHistory(){
        this.myWebView.clearHistory();
    }
      
    @JavascriptInterface
    public String getPath(String name) {
        return app.getPath(name);
    }
    
    @JavascriptInterface
    public void closeApp(){
    	this.activity.finish();
		System.exit(0);
    }

    @JavascriptInterface
    public void initNotification(String title, String msg){
        notification.initNotification(title, msg);
    }

    @JavascriptInterface
    public void showNotification(int id){
        notification.showNotification(id);
    }

    @JavascriptInterface
    public void initBigNotification(String title, String [] msg){
        notification.initBigNotification(title,  msg);
    }

    @JavascriptInterface
    public void showToast(String text, int duration){
        toast.showToast(text, duration);
    }

    @JavascriptInterface
    public void makeCall(String number){
        call.makeCall(number);
    }

    @JavascriptInterface
    public void enableWifi(){
        wifi.enableWifi();
    }

    @JavascriptInterface
    public void disableWifi(){
        wifi.disableWifi();
    }

    @JavascriptInterface
    public void disconnectWifi(){
        wifi.disconnectWifi();
    }

    @JavascriptInterface
    public int getWifiState(){
        return wifi.getWifiState();
    }

    @JavascriptInterface
    public boolean isWifiEnabled(){
        return wifi.isWifiEnabled();
    }

    @JavascriptInterface
    public String getWifiScanResults() throws JSONException {
        return wifi.getWifiScanResults();
    }

    @JavascriptInterface
    public void connectWifi(String ssid, String password){
        wifi.connectWifi(ssid, password);
    }

    @JavascriptInterface
    public void enableHotspot(String ssid){
        try {
            hotspot.enableHotspot(ssid);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void disableHotspot(){
        try{
            hotspot.disableHotspot();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public boolean isHotspotEnabled(){
        return hotspot.isHotspotEnabled();
    }

    @JavascriptInterface
    public String getAllContacts() throws JSONException {
        return this.contact.getAllContacts(false);
    }
    
    @JavascriptInterface
    public String getContactByName(String name) throws JSONException {
        return this.contact.getContactByName(name);
    }
    
    @JavascriptInterface
    public int getContactsCount() throws JSONException {
        return this.contact.getContactsCount();
    }
    
    @JavascriptInterface
    public String addContact(String name, String number, String email) {
        return this.contact.addContact(name, number, email);
    }

    @JavascriptInterface
    public String getDeepLink(){
        return this.deepLink.getLink();
    }

    @JavascriptInterface
    public String sendSMS(String number, String message) {
        return this.sms.sendSMS(number, message);
    }

    @JavascriptInterface
    public String getLocation(){
        return this.location.getLocation();
    }

    @JavascriptInterface
    public boolean isMobileDataEnabled() {
        return this.mobileData.isEnabled();
    }
              
    // TODO: IO Input Events XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX //
    
    @JavascriptInterface
    public void setOrientation( String orientation ){ this.io.setOrientation( orientation ); }
    
    @JavascriptInterface
    public void AndroidIntent( String url ){ this.io.AndroidIntent( this.myWebView, url ); }
   
    @JavascriptInterface
    public void IntentRaw( String path, String type, String action, String Url, String extras ){ 
		String url = "S.browser_fallback_url="+Url;
		String intent = "intent://"+path+"#Intent;type="+type+";action="+action+";"+url+";"+extras+"end";
    	this.io.AndroidIntent( this.myWebView, intent );
    }
    
    @JavascriptInterface
    public void IntentOpenUrl( String Url ){ 
    	String type = "type=text/html";
    	String url = "S.browser_fallback_url="+Url;
    	String intent = "intent://url#Intent;"+type+";"+url+";end";
    	this.io.AndroidIntent( this.myWebView, intent );
    }
   
    @JavascriptInterface
    public void IntentOpenFile( String path, String type ){ 
		String action = "action=android.intent.action.VIEW";
    	String intent = "intent://"+path+"#Intent;"+action+";type="+type+";end";
    	this.io.AndroidIntent( this.myWebView, intent );
    }
   
    @JavascriptInterface
    public void IntentOpenApp( String AppName, String Url ){ 			
    	String app = "package="+AppName;
		String url = "S.browser_fallback_url="+Url;
    	String intent = "intent://app#Intent;"+app+";"+url+";end";
    	this.io.AndroidIntent( this.myWebView, intent );
    }
   
    @JavascriptInterface
    public void IntentShare( String Message ){ 
    	String type = "type=text/plain";
		String action = "action=android.intent.action.SEND";
		String extra = "S.android.intent.extra.TEXT="+Message;
    	String intent = "intent://send/#Intent;"+action+";"+type+";"+extra+";end";
    	this.io.AndroidIntent( this.myWebView, intent );
    }
     
    // TODO: Contribution XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX //
    
    
}
