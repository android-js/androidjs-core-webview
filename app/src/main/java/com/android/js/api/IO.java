package com.android.js.api;

import android.net.Uri;
import android.app.Activity;
import android.webkit.WebView;
import android.content.Intent;
import android.content.Context;

import java.net.URI;
import android.util.Log;
import java.net.URISyntaxException;
import android.content.pm.ResolveInfo;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

public class IO {
    private Activity activity;

    public IO(Activity activity){ this.activity = activity; }
    
    public boolean AndroidIntent( WebView view, String url ){
    	Context context = view.getContext();
    	
    	if( url.startsWith("intent://") ){
			try { Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
				if (intent != null) {
				    PackageManager packageManager = context.getPackageManager();
				    ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
				    if (info != null){ context.startActivity(intent); } 
				    else {
				        String Url = intent.getStringExtra("browser_fallback_url");
		                intent = new Intent( Intent.ACTION_VIEW,Uri.parse(Url) );
		                context.startActivity(intent);
				    }	return true; }
			} catch (URISyntaxException e) {  Log.e("error", "Can't resolve intent: ", e); }	
		} else if( url.startsWith("file://") ){ view.loadUrl( url ); } return false; 
		
    }								
    
    public void setOrientation( String orientation ){
    	switch( orientation ){
    		case "landscape": this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); break;
    		case "portrait": this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); break;
    		default: this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); break;
    	}
    }
    
    
}