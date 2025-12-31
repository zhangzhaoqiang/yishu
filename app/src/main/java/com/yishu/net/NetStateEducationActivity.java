package com.yishu.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ShellUtils;

import com.yishu.net.app.YsApp;
import com.yishu.net.utils.SilentInstallUtils;

import java.io.File;


public class NetStateEducationActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    private TextView tv_content;
    private TextView tv_hcc;
    private ImageView iv_loading;
    private boolean isServer;
    private boolean isTeacher;
    private ClientSetting clientSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_netstate);
        Context context = getApplicationContext();
        clientSetting=ClientSetting.loadSetting();
        String string = CacheDiskUtils.getInstance(getCacheDir()).getString(YsApp.SHOP_PACKAGE + YsApp.CLASS_TYPE);
        switch (string) {
            case "学员机":
                isServer = false;
                isTeacher = false;
                break;
            case "教师机":
                isServer = false;
                isTeacher = true;
                break;
            case "中转服务":
                isServer = true;
                isTeacher = false;
                break;
        }
        initView();
        checkVersion();
//        checkNet();
    }

    private String ip = "";

    private void checkNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!NetworkUtils.isAvailable()) {
                        updateString("等待网络连接中");
                    } else {
                        ip = NetworkUtils.getIPAddress(true);
                        break;
                    }
                }
                checkVersion();
            }
        }).start();
    }

    private AppUtils.AppInfo appInfoStudent = null;
    private AppUtils.AppInfo appInfoTeacher = null;
    private AppUtils.AppInfo xinfeifusuE = null;
    private AppUtils.AppInfo haimulikeE = null;
    private AppUtils.AppInfo appshop = null;
    private String packageName;
    private AppUtils.AppInfo appInfoLocal;
    private String stringPdNumber = "";
    private String isAutoUpdate = "";
    boolean isHttpRun = false;

    private void checkVersion() {
        appInfoStudent = AppUtils.getAppInfo("com.yishu.student");
        appInfoTeacher = AppUtils.getAppInfo("com.yishu.teaching");
        xinfeifusuE = AppUtils.getAppInfo("com.yishu.shebei.xin.xinfeifusuE");
        haimulikeE = AppUtils.getAppInfo("com.zggc.sb002.e.qdywg");
        appshop = AppUtils.getAppInfo(YsApp.SHOP_PACKAGE);
        packageName = getApplicationContext().getPackageName();
        appInfoLocal = AppUtils.getAppInfo(packageName);

        StartRun();
    }

    private void hccUpdateString(String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_hcc.setText(s);
            }
        });
    }

    private void startADB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShellUtils.execCmd("setprop service.adb.tcp.port 5555", true);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ShellUtils.execCmd("stop adbd", true);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ShellUtils.execCmd("start adbd", true);
            }
        }).start();
    }

    private void StartRun() {
        startADB();
        Context context = getApplicationContext();
        if (isServer) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent serviceIntent = new Intent(context, ServerActivity.class);
                    serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(serviceIntent);
                }
            }, 200);
            return;
        }
        Intent serviceIntent = new Intent(context, ClientService.class);
        context.startService(serviceIntent);
        String packageName1 = "com.yishu.student";
        String activity1 = ".CheckActivity";
        String packageName2 = "com.yishu.teaching";
        String activity2 = ".ui.CheckActivity";
        Intent activityIntent = new Intent();
        if (isTeacher) {
            //打开教师机APP
            activityIntent.setClassName(packageName2, packageName2 + activity2);
            //intent.setComponent(cn);
        } else {
            //打开学院机APP
            activityIntent.setClassName(packageName1, packageName1 + activity1);
        }
        context.startActivity(activityIntent);
        finish();
    }


    private void installApk(String p1) {
        updateString("安装中");
        SilentInstallUtils.installByShell(new File(p1), true);
        updateString("安装完成");
    }

    private String apkPath = "/sdcard/Education_file";


    private void updateString(String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_content.setText(s);
            }
        });
    }

    private void initView() {
        tv_content = findViewById(R.id.tv_content);
        tv_hcc = findViewById(R.id.tv_hcc);
        iv_loading = findViewById(R.id.iv_loading);
        RotateAnimation animation;
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(1000);
        iv_loading.clearAnimation();
        iv_loading.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}