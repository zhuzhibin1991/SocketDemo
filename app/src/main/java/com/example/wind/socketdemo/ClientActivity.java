package com.example.wind.socketdemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClientActivity extends Activity implements OnClickListener {
    MyThread ClientThread;
    EditText result;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (EditText) findViewById(R.id.EditText1);
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(this);

        // TODO Auto-generated method stub

    }

    private class MyThread extends Thread {
        private String message;
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket clientSocket;

        public void setMessage(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            try {
                //clientSocket = new Socket("10.0.2.2", 7100);
                clientSocket = new Socket("172.16.111.36", 8080);
                dis = new DataInputStream(clientSocket.getInputStream());
                dos = new DataOutputStream(clientSocket.getOutputStream());

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dos.writeUTF(message);
                Log.d("zhuzhibin","send:"+message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.close();
        }

        public void close() {
            try {
                dos.close();
                dis.close();
                clientSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        ClientThread = new MyThread();
        ClientThread.setMessage(result.getText().toString());
        ClientThread.start();
        Toast.makeText(this,result.getText().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();
    }
}
