package com.android.js;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class JavaIPC extends Thread {
    private ServerSocket serverSocket;

    public JavaIPC(int port) throws IOException{
        serverSocket = new ServerSocket(port);
//        serverSocket.setSoTimeout(10000);
    }

    public void run(){
        while(true) try {
            java.net.Socket server = serverSocket.accept();
            System.out.println("Java: a user connected");
            DataInputStream in = new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());
            if(in.readUTF().equals("hello")){
                on("hello", "bello");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void on(String s1, String s2){
        System.out.println("java: " + s1 + s2);
    }
}
