package com.yourorg.sample;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("node");
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
                    //Recursively delete any existing nodejs-project.
                    File nodeDirReference=new File(nodeDir);
                    if (nodeDirReference.exists()) {
                        deleteFolderRecursively(new File(nodeDir));
                    }
                    //Copy the node project from assets into the application's data path.
                    copyAssetFolder(getApplicationContext().getAssets(), "myapp", nodeDir);
                    startNodeWithArguments(new String[]{"node",
                            nodeDir+"/main.js"
                    });
                }
            }).start();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    String jsPath = getCacheDir().getAbsolutePath() + "/main.js";
////                    String nodeDir=getApplicationContext().getFilesDir().getAbsolutePath()+"/myapp";
//                    String hPath = getCacheDir().getAbsolutePath();
////                    String mpath = "android_asset/main.js";
////                    Utils.copyAssetFile(getAssets(), "main.js", jsPath);
//                    Utils.copyFolder(getAssets(), "myapp", hPath);
////                    Utils.copyFolder(getAssets(), "node_modules", hPath);
////                    Utils.copyAssetFile(getAssets(), "hello.js", hPath);
//                    startNodeWithArguments(new String[]{"node", hPath + "/main.js"});
//                }
//            }).start();
        }

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("file:///android_asset/myapp/views/index.html");

//        final Button buttonVersions = (Button) findViewById(R.id.btVersions);
//        final TextView textViewVersions = (TextView) findViewById(R.id.tvVersions);

//        buttonVersions.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                //Network operations should be done in the background.
//                new AsyncTask<Void,Void,String>() {
//                    @Override
//                    protected String doInBackground(Void... params) {
//                        String nodeResponse="";
//                        try {
//                            URL localNodeServer = new URL("http://localhost:3000/");
//                            BufferedReader in = new BufferedReader(
//                                    new InputStreamReader(localNodeServer.openStream()));
//                            String inputLine;
//                            while ((inputLine = in.readLine()) != null)
//                                nodeResponse=nodeResponse+inputLine;
//                            in.close();
//                        } catch (Exception ex) {
//                            nodeResponse=ex.toString();
//                        }
//                        return nodeResponse;
//                    }
//                    @Override
//                    protected void onPostExecute(String result) {
//                        textViewVersions.setText(result);
//                    }
//                }.execute();
//
//            }
//        });

    }
    private static boolean deleteFolderRecursively(File file) {
        try {
            boolean res=true;
            for (File childFile : file.listFiles()) {
                if (childFile.isDirectory()) {
                    res &= deleteFolderRecursively(childFile);
                } else {
                    res &= childFile.delete();
                }
            }
            res &= file.delete();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            boolean res = true;

            if (files.length==0) {
                //If it's a file, it won't have any assets "inside" it.
                res &= copyAsset(assetManager,
                        fromAssetPath,
                        toPath);
            } else {
                new File(toPath).mkdirs();
                for (String file : files)
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native Integer startNodeWithArguments(String[] arguments);
}
