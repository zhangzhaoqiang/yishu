package com.yishu.net;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.yishu.net.scp.FileInstall;
import com.yishu.net.scp.Scp;
import com.yishu.net.utils.SilentInstallUtils;
import com.yishu.server.json.Msg;
import com.yishu.server.json.Update;
import com.yishu.server.utils.GsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ServerSettingActivity extends AppCompatActivity {

    private String apkPath = "/sdcard/Education_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_setting);
        ClientNetFuc.initFucConnection(this, new OnServiceListener.Stub() {
            @Override
            public void OnConnect() throws RemoteException {

            }

            @Override
            public void OnDisConnect() throws RemoteException {

            }

            @Override
            public void OnRecived(String json) throws RemoteException {
                Msg msg = GsonUtil.convert(json, Msg.class);
                switch (msg.msg_code) {
                    case Update.UPDATE_SOURCE:


                        break;
                }
            }
        });
    }

    private void downloadDir(FileInstall.FileClass fileClass) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientNetFuc.destory(this);
    }


}