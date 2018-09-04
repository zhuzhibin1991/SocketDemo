package com.example.wind.socketlib;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketDemo {
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        PcReceiver receiver = new PcReceiver();
        receiver.InitializeUI();

        receiver.ss = new ServerSocket(8080);
        while(true) {
            Client client = receiver.connect();
            if(client!=null) {
                client.start();
            }
        }
    }
}
