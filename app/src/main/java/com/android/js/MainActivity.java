package com.android.js;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.LinkProperties;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.android.js.api.Notification;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("node");
    }

    public WebView myWebView ;

    // override back button to webview back button

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    //We just want one instance of node running in the background.
    public static boolean _startedNodeAlready=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check and request for required permission
        System.out.println(Environment.getRootDirectory());
        PermissionRequest.checkAndAskForPermissions(this, this);

        if( !_startedNodeAlready ) {
            _startedNodeAlready=true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //The path where we expect the node project to be at runtime.
                    String nodeDir=getApplicationContext().getFilesDir().getAbsolutePath()+"/myapp";
                    if (Utils.wasAPKUpdated(getApplicationContext())) {
                        //Recursively delete any existing nodejs-project.
                        File nodeDirReference=new File(nodeDir);
                        if (nodeDirReference.exists()) {
                            Utils.deleteFolderRecursively(new File(nodeDir));
                        }
                        //Copy the node project from assets into the application's data path.
                        Utils.copyAssetFolder(getApplicationContext().getAssets(), "myapp", nodeDir);

                        Utils.saveLastUpdateTime(getApplicationContext());
                    }
                    startNodeWithArguments(new String[]{"node",
                            nodeDir+"/main.js"
                    });
                }
            }).start();
        }



            // webview

        myWebView = (WebView) findViewById(R.id.webview);

        // adding javascript interface for creating javascript to java IPC

        myWebView.addJavascriptInterface(new JavaIPC(this, myWebView), "android");


        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.setWebContentsDebuggingEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        myWebView.loadUrl("file:///android_asset/myapp/views/index.html");



        // entertain webview camera request

        myWebView.setWebChromeClient(new WebChromeClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final android.webkit.PermissionRequest request) {
                request.grant(request.getResources());

            }

        });


        // hardware acceleration

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // chromium, enable hardware acceleration
//            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            // older android version, disable hardware acceleration
//            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native Integer startNodeWithArguments(String[] arguments);
}
