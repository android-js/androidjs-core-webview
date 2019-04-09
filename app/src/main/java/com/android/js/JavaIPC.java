package com.android.js;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class JavaIPC {
    private MainActivity activity;
    private WebView myWebView;

    public JavaIPC(MainActivity activity, WebView myWebView){
        this.activity = activity;
        this.myWebView = myWebView;
    }

    @JavascriptInterface
    public String helloWorld(){
        System.out.println("Java IPC Works");
        return "Hello World";
    }
}
