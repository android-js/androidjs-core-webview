package com.android.js.webview;

import android.net.Uri;
import android.os.Build;
import android.app.Activity;
import android.webkit.WebView;
import android.content.Intent;
import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.annotation.TargetApi;
import android.webkit.WebChromeClient;
import android.support.v7.app.AppCompatActivity;

import java.net.URI;
import android.util.Log;
import java.net.URISyntaxException;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager;

import android.view.KeyEvent;
import com.android.js.other.Utils;
import com.android.js.common.JavaWebviewBridge;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

public class AndroidJSActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("node");
    }

    public WebView myWebView ;
    // public VideoView myVideoView ;

    // override back button to webview back button
	
//	TODO: Input Override XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX//
   
    /* 	TODO: Input Events
    	I've created this code to get phone's buttons states like pressed or releaded in the webview
    	this include Volume Up and Down, And Back
    	
    	** android.onVolumeBackPressed
    	** android.onVolumeDownPressed
    	** android.onVolumeUpPressed
    	
    	** android.onVolumeBackReleased
    	** android.onVolumeDownReleased
    	** android.onVolumeUpReleased
    	
    */
        
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
    		myWebView.evaluateJavascript( "try{ android.onVolumeDownPressed() } catch(e) { android.decreaseVolume() }", null );
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
    		myWebView.evaluateJavascript( "try{ android.onVolumeUpPressed() } catch(e) { android.increaseVolume() }", null );		
		} else if (keyCode == KeyEvent.KEYCODE_BACK){
    		myWebView.evaluateJavascript( "try{ android.onVolumeBackPressed() } catch(e) { window.history.back(); }", null );	
		}	return true;
	}
    
    @Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
    		myWebView.evaluateJavascript( "try{ android.onVolumeDownReleased() } catch(e) { }", null );
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
    		myWebView.evaluateJavascript( "try{ android.onVolumeUpReleased() } catch(e) { }", null );		
		} else if (keyCode == KeyEvent.KEYCODE_BACK){
    		myWebView.evaluateJavascript( "try{ android.onVolumeBackReleased() } catch(e) { }", null );	
		} 	return true;
	}
	
//	TODO: Input Override XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX//
    
    //We just want one instance of node running in the background.
    public static boolean _startedNodeAlready=false;

    public void start_node(final Activity activity){
    
        if( !_startedNodeAlready ) {
            _startedNodeAlready=true;
            new Thread(new Runnable() {
                           
                @Override
                public void run() {                              
                    String nodeDir=activity.getApplicationContext().getFilesDir().getAbsolutePath()+"/myapp";
                    if (Utils.wasAPKUpdated(activity.getApplicationContext())) {
                    
                        File nodeDirReference=new File(nodeDir);
                        if (nodeDirReference.exists()) {
                            Utils.deleteFolderRecursively(new File(nodeDir));
                        }
                        
                        Utils.copyAssetFolder(activity.getApplicationContext().getAssets(), "myapp", nodeDir);
                        Utils.saveLastUpdateTime(activity.getApplicationContext());
                    }	startNodeWithArguments(new String[]{"node", nodeDir+"/main.js" });
                }
            }).start();
        }
    }

    public void configureWebview(int iconId){
        this.myWebView.addJavascriptInterface(new JavaWebviewBridge(this ,this.myWebView, iconId, "com.android.js.webview.MainActivity"), "android");
		
        this.myWebView.getSettings().setJavaScriptEnabled(true);
        this.myWebView.getSettings().setDomStorageEnabled(true);
        this.myWebView.getSettings().setAllowFileAccess(true);
        this.myWebView.setWebContentsDebuggingEnabled(true);
        
        this.myWebView.setWebViewClient(new WebViewClient(){
			
            @Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				Context context = view.getContext();
				
//	TODO: URL Intent Support XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX//
   
    /* 	TODO: Intent URL Support
    	I've created this code to support Intents based on URL to open a video, share content or somthing like that dude.
    */
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
					} catch (URISyntaxException e) {  Log.e("error", "Can't resolve intent://", e); }	
				} else if( url.startsWith("file://") ){ view.loadUrl( url ); } return false; 
								
//	TODO: URL Intent Support XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX//

			}
			
        });
        
        this.myWebView.getSettings().setSupportMultipleWindows(true);
        this.myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        this.myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        this.myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        this.myWebView.loadUrl("file:///android_asset/myapp/views/index.html");
        
        // entertain webview camera request

        this.myWebView.setWebChromeClient(new WebChromeClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final android.webkit.PermissionRequest request) {
                request.grant(request.getResources());
            }
            
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg){
                WebView.HitTestResult result = view.getHitTestResult();
                Context context = view.getContext();
                String url = result.getExtra();
                
              	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				context.startActivity(intent);
				
                return false;
            }
        });
    }
    
    public native Integer startNodeWithArguments(String[] arguments);
}
