package com.yourorg.sample;

import android.icu.text.SymbolTable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


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
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.loadUrl("file:///android_asset/myapp/views/index.html");




        // hardware acceleration

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // chromium, enable hardware acceleration
//            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            // older android version, disable hardware acceleration
//            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }


        // java ipc socket

        JavaIPC Socket = null;
        try {
            Socket = new JavaIPC(3001);
            Socket.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // java socket.io client


//
//        final Socket socket;
//        try{
//            socket = IO.socket("http://localhost:3000");
//            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    socket.emit("foo", "hi");
//                }
//
//            }).on("helloFromNode", new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    System.out.println("Hello from node");
////                    socket.emit("helloFromJava", "Hello Node this is java");
//                }
//
//            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {}
//
//            });
//            socket.connect();
//            socket.emit("helloFromJava", "Hello Node this is java");
//            socket.on("helloFromNode", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    System.out.println(args[0]);
//                }
//            });
//            socket.on("getClientPath", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    socket.emit("resClientPath", getApplicationContext().getFilesDir().getAbsolutePath());
//                }
//            });
//
////            socket.emit("helloFromJava", "Hello Java");
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native Integer startNodeWithArguments(String[] arguments);
}
