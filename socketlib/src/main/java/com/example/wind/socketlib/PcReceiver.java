package com.example.wind.socketlib;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;


public class PcReceiver {

    public ServerSocket ss;
    private TextArea ta;
    private Frame f;
    private Label start;
    private Panel p;
    private Logger log = Logger.getLogger("PcReceiver");

    public void InitializeUI() {
        f = new Frame("Receiver");
        ta = new TextArea();
        start = new Label("data receiver");
        p = new Panel();
        p.setLayout(new FlowLayout());
        p.add(start);
        f.add(ta, "Center");
        f.add(p, "South");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.setSize(400, 400);
        f.setVisible(true);

    }

    public Client connect() {
        log.info("zhuzhibin ======connect======");
        try {
            Client client = new Client(this);
            client.s = ss.accept();
            client.dis = new DataInputStream(client.s.getInputStream());
            client.dos = new DataOutputStream(client.s.getOutputStream());
            log.info("zhuzhibin client:"+client.toString());
            return client;
        } catch (IOException e) {
            log.info("zhuzhibin ======IOException======");
            e.printStackTrace();
            return null;
        }
    }


    public TextArea getTextArea() {
        return ta;
    }

    public void Clientclose(Client client) {
        try {
            client.dis.close();
            client.dos.close();
            client.s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
