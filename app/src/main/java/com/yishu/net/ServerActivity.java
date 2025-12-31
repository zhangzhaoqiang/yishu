package com.yishu.net;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.yishu.net.scp.FileInstall;
import com.yishu.net.scp.Scp;
import com.yishu.net.utils.DownloadAPK;
import com.yishu.net.utils.SilentInstallUtils;
import com.yishu.server.BroadcastIP;
import com.yishu.server.EducationServer;
import com.yishu.server.SettingConfig;
import com.yishu.server.msgfuc.UpdateMsgFuc;
import com.yishu.server.utils.GsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class ServerActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private TextView tv_server;
    private ScrollView svResult;
    public AndroidServer androidServer=null;
    private ClientSetting clientSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        clientSetting=ClientSetting.loadSetting();
        Context context = getApplicationContext();
//        SharedPreferences sp = context.getSharedPreferences(ClientNet.ClientSP, context.MODE_PRIVATE);
//        String ServerPort = sp.getString("ServerPort", "8006");
//        String ServerBroadcastPort = sp.getString("ServerBroadcastPort", "40000");
        tv_server = findViewById(R.id.tv_server);
        svResult= (ScrollView) findViewById(R.id.sv_text);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = AndroidServer.getIP();
                updateString(ip);

                if(ip.length()==0){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ip = AndroidServer.getIP();
                }
                int i = ip.lastIndexOf(".");
                String broadIP=ip.substring(0,i)+".255";
                updateString(broadIP);
                updateString(clientSetting.port);
                updateString(clientSetting.bordcastPort);
               String  packageName = getApplicationContext().getPackageName();
                int code = AppUtils.getAppInfo(packageName).getVersionCode();
                androidServer=new AndroidServer(broadIP, Integer.parseInt(clientSetting.bordcastPort), Integer.parseInt(clientSetting.port),code, new EducationServer.LogListener() {
                    @Override
                    public void log(String s) {
                        updateString(s);
                    if(s.contains("基础服务更新")){
                        s.indexOf("基础服务更新");
                        String s1=s.substring(s.indexOf("基础服务更新")+6,s.length()-1);
                        DownloadAPK.downloadAPK(s1);
                    }
                    }
                });
            }
        }).start();
//        UpdateMsgFuc.setOnUpdateDataCallBack(new UpdateMsgFuc.OnUpdateDataCallBack() {
//            @Override
//            public void apkUrl(String s) {
//                DownloadAPK.downloadAPK(s);
//            }
//        });
    }
    private int count=0;
    public void updateString(String s){
        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_server.append(s+"\n");
                svResult.fullScroll(ScrollView.FOCUS_DOWN);
                count++;
                if(count==1000){
                    tv_server.setText("");
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }


}