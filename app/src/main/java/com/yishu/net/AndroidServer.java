package com.yishu.net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.yishu.server.BroadcastIP;
import com.yishu.server.EducationServer;
import com.yishu.server.SettingConfig;

import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Optional;

public class AndroidServer  {
    EducationServer s=null;
    public AndroidServer(String broadcast,int broadcastPort,int port,int code ,EducationServer.LogListener logListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    SettingConfig.broadcast=broadcast;
                    SettingConfig.broadcastPort=broadcastPort;
                    SettingConfig.port=port;
                    EducationServer s = new EducationServer(SettingConfig.port,code);
                    s.setLogListener(logListener);
                    s.start();
                    BroadcastIP broadcastIP=new BroadcastIP(SettingConfig.broadcastPort,SettingConfig.broadcast);
                    broadcastIP.start();
                    System.out.println("ChatServer started on port: " + s.getPort());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static String getIP(){
        Optional<Inet4Address> localIp4Address = null;
        try {
            localIp4Address = BroadcastIP.getLocalIp4Address();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                String hostAddress = ((Inet4Address)localIp4Address.get()).getHostAddress();
                return hostAddress;
            }catch (Exception e){
                try {
                    Log.e("net_error","路由器没有启动");
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                getIP();
            }

        }
        return "";
    }
}
