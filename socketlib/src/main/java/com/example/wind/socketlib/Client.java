package com.example.wind.socketlib;

import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Client extends Thread {

    public PcReceiver server;
    public Socket s;
    public DataInputStream dis;
    public DataOutputStream dos;

    public Client(PcReceiver server) {

        this.server = server;
    }

    public void run() {
        String info = null;
        TextArea ta = server.getTextArea();
        try {
            info = dis.readUTF();
            System.out.println(info);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ta.append("Receiving data: " + info + "\n");

    }


}
