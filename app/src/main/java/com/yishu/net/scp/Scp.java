package com.yishu.net.scp;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

public class Scp {

    private volatile static Scp scpInstance;

    private String user;
    private String pass;
    private String host;
    private Connection connection;
    private SCPClient scpClient;
    private Boolean isAuthed;

    private Scp(String user, String pass, String host){
        this.user = user;
        this.pass = pass;
        this.host = host;
    }

    public static Scp getScpUtilsInstance(String user, String pass, String host){

        if(scpInstance == null) {
            synchronized(Scp.class) {
                if(scpInstance == null) {
                    scpInstance = new Scp(user,pass,host);
                }
            }
        }
        return scpInstance;
    }


    public void connect(){
        connection = new Connection(host);
        try {
            connection.connect();
            isAuthed = connection.authenticateWithPassword(user,pass);
            // scp 连接
            scpClient = connection.createSCPClient();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void close(){
        connection.close();
//        sftPv3Client.close();
    }

    public boolean getIsAuthed(){
        return isAuthed;
    }

    // 拷贝文件到服务器
    public void putFile(String filePath,String aimPath){
        try {
            if(scpClient != null){
                scpClient.put(filePath,aimPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 拷贝文件到服务器
    public void getFile(String filePath,String aimPath){
        try {
            if(scpClient != null){
                scpClient.get(filePath,aimPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}