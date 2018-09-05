package com.example.wind.socketdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ClientActivity extends Activity implements OnClickListener {
    MyThread ClientThread;
    FileTransThread fileTransThread;
    EditText result;
    Button msgBtn;
    Button fileBtn;
    private  final int UPDATE_PROGRESS =1000;

    private  final int DISMISS_PROGRESS =1001;
    ProgressBar pb = null;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case UPDATE_PROGRESS:
                int progress=msg.arg1;
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(progress);
                break;
                case DISMISS_PROGRESS:
                    pb.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (EditText) findViewById(R.id.EditText1);
        msgBtn = (Button) findViewById(R.id.msg_btn);
        msgBtn.setOnClickListener(this);
        fileBtn = (Button) findViewById(R.id.file_btn);
        fileBtn.setOnClickListener(this);
        pb=(ProgressBar)findViewById(R.id.fileTransBar);
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
                //clientSocket = new Socket("172.16.111.36", 8080);
                clientSocket = new Socket("192.168.16.1", 8080);
                Log.d("zhuzhibin","server ip:"+clientSocket.getInetAddress().toString()+":"+clientSocket.getPort());
                dis = new DataInputStream(clientSocket.getInputStream());
                dos = new DataOutputStream(clientSocket.getOutputStream());

            } catch (UnknownHostException e) {
                Log.d("zhuzhibin","UnknownHostException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("zhuzhibin","IOException");
                e.printStackTrace();
            }
            try {
                dos.writeUTF("HEAD_MSG");
                dos.flush();
                dos.writeUTF(message);
                dos.flush();
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

    private class FileTransThread extends Thread {
        private InputStream is;
        private DataOutputStream dos;
        private Socket clientSocket;

        public void sendFile() throws IOException{
            String fileNames[]=getResources().getAssets().list("");
            Log.d("zhuzhibin","fileNames:"+ Arrays.toString(fileNames));
            for(String fileName:fileNames){
                Log.d("zhuzhibin","fileName:"+ fileName);
                if(fileName.contains("Settings.txt")){
                    Log.d("zhuzhibin","fileName11:"+ fileName);
                    is=getResources().getAssets().open(fileName);
                    long fileSize=is.available();
                    //File file =new File(fileName.trim());
                    if(dos!=null){
                        dos.writeUTF("HEAD_FILE");
                        dos.flush();
                        dos.writeUTF(fileName);
                        dos.flush();
                        dos.writeLong(fileSize);
                        dos.flush();

                        Message msg=Message.obtain();
                        msg.what=UPDATE_PROGRESS;
                        mHandler.sendMessage(msg);
                        byte[] bytes=new byte[1024];
                        int length=0;
                        long progress=0;
                        while((length=is.read(bytes,0,bytes.length))!=-1){
                            dos.write(bytes,0,length);
                            dos.flush();
                            progress += length;
                            Message msg1=Message.obtain();
                            msg1.what=UPDATE_PROGRESS;
                            msg1.arg1=(int)(100*progress/fileSize);
                            mHandler.sendMessage(msg1);
                            Log.d("zhuzhibin","| "+(100*progress/fileSize)+"% |");
                        }
                        Message msg2=Message.obtain();
                        msg2.what=DISMISS_PROGRESS;
                        mHandler.sendMessage(msg2);
                    }
                }
            }
        }

        @Override
        public void run() {
            try {
                //clientSocket = new Socket("172.16.111.36", 8080);
                clientSocket = new Socket("192.168.16.1", 8080);
                Log.d("zhuzhibin","server ip:"+clientSocket.getInetAddress().toString()+":"+clientSocket.getPort());
                dos = new DataOutputStream(clientSocket.getOutputStream());

            } catch (UnknownHostException e) {
                Log.d("zhuzhibin","UnknownHostException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("zhuzhibin","IOException");
                e.printStackTrace();
            }
            try {
                sendFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.close();
        }

        public void close() {
            try {
                dos.close();
                is.close();
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

        switch(v.getId()) {
            case R.id.msg_btn:
                ClientThread = new MyThread();
                ClientThread.setMessage(result.getText().toString());
                ClientThread.start();
                Toast.makeText(this,result.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.file_btn:
                fileTransThread = new FileTransThread();
                fileTransThread.start();
                break;
        }


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();
    }
}
