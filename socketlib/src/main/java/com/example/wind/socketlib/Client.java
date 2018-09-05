package com.example.wind.socketlib;

import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

class Client extends Thread {

    public PcReceiver server;
    public Socket s;
    public DataInputStream dis;
    public DataOutputStream dos;
    private String msgType;

    public Client(PcReceiver server) {

        this.server = server;
    }

    public void run() {
        try {
            msgType = dis.readUTF();
            System.out.println(msgType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(msgType.contains("HEAD_MSG")){
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
        if(msgType.contains("HEAD_FILE")){
            try{
                String fileName=dis.readUTF();
                long fileLength=dis.readLong();
                File directory=new File("D:\\fileCache");
                if(!directory.exists()){
                    directory.mkdir();
                }
                File file=new File(directory.getAbsolutePath()+File.separatorChar+fileName);
                FileOutputStream fos=new FileOutputStream(file);

                byte[] bytes=new byte[1024];
                int length=0;
                while((dis.read(bytes,0,bytes.length))!=-1){
                    fos.write(bytes,0,length);
                    fos.flush();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        server.Clientclose(this);

    }


}
