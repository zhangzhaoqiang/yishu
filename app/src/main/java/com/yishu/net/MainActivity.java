package com.yishu.net;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.yishu.net.scp.FileInstall;
import com.yishu.net.scp.Scp;
import com.yishu.net.utils.SilentInstallUtils;
import com.yishu.serial.YSSerial;
import com.yishu.server.utils.GsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private EditText etDeviceName;
    private EditText etDeviceID;
    private EditText etServerIP;
    private EditText etPort;
    private EditText etTeacherCode;
    private EditText etUser;
    private EditText etPwd;
    private EditText et_server_port;
    private EditText et_server_broadcast_port;
    private TextView tv_uitext;
    private TextView tv_ip;
    private Button butBaocun;
    private Button butReboot;
    private Button butApkTest;
    private Button butSystemui;
    private Button butUpdate;
    private Button but_test;
    private Button but_server;
    private Button but_release;
    private Button but_net_update;
    private Button but_bluetest;
    private EditText et_pd_id;
    private LinearLayout ll_download;
    private CheckBox cbIsstart;
    private CheckBox cb_zhongzhuan;
    private CheckBox cb_isupdate;
    private CheckBox cb_student;
    private CheckBox cb_teacher;
    private CheckBox cb_isbind;
    private Button but_lock_systemui;
    String serverIP;
    List<FileInstall.FileClass> dataList;
    List<Button> buttons = new ArrayList<>();
    Scp scp = null;

    private String apkPath = "/sdcard/Education_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtils.AppInfo appInfo = AppUtils.getAppInfo(getApplicationContext().getPackageName());
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(appInfo.getName() + " " + appInfo.getVersionName()+" "+appInfo.getVersionCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    INTERNET,
                    RECEIVE_BOOT_COMPLETED, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_PHONE_STATE, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE,
            }, 1000);
        }
        File f = new File(apkPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        but_lock_systemui = findViewById(R.id.but_lock_systemui);
        but_lock_systemui.setOnClickListener(new View.OnClickListener() {
            boolean isLook=false;
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(isLook?"android.intent.action.SHOW_SYSTEM_BAR":"android.intent.action.HIDE_SYSTEM_BAR");
                sendBroadcast(intent);
                but_lock_systemui.setText("锁死上下："+(isLook?"解除":"锁"));
                isLook=!isLook;
            }
        });

        but_bluetest = findViewById(R.id.but_bluetest);
        but_bluetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BlueCheckActivity.class));
            }
        });
        et_pd_id = findViewById(R.id.et_pd_id);

        cb_student = findViewById(R.id.cb_student);
        cb_isupdate = findViewById(R.id.cb_isupdate);
        cb_teacher = findViewById(R.id.cb_teacher);
        cb_isbind = findViewById(R.id.cb_isbind);

        but_server = findViewById(R.id.but_server);
        but_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });
        tv_ip = findViewById(R.id.tv_ip);



        et_server_port = findViewById(R.id.et_server_port);
        et_server_broadcast_port = findViewById(R.id.et_server_broadcast_port);
        ll_download = findViewById(R.id.ll_download);
        but_test = findViewById(R.id.but_test);
        but_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etServerIP.setText("192.168.0.5");
                etPort.setText("8886");
            }
        });
        but_release = findViewById(R.id.but_release);
        but_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etServerIP.setText("192.168.0.222");
                etPort.setText("8887");
            }
        });
        but_net_update = findViewById(R.id.but_net_update);
        but_net_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NetStateEducationActivity.class));
            }
        });
        tv_uitext = findViewById(R.id.tv_uitext);
        etDeviceID = findViewById(R.id.et_deviceID);
        etDeviceName = findViewById(R.id.et_deviceName);
        etServerIP = findViewById(R.id.et_serverIP);
        etTeacherCode = findViewById(R.id.et_teachercode);
        butBaocun = findViewById(R.id.but_baocun);
        butUpdate = findViewById(R.id.but_update);
        etPort = findViewById(R.id.et_port);
        butReboot = findViewById(R.id.but_reboot);
        butApkTest = findViewById(R.id.but_apktest);
        butSystemui = findViewById(R.id.but_systemui);
        etUser = findViewById(R.id.et_user);
        etPwd = findViewById(R.id.et_pwd);
        Context context = getApplicationContext();

//
//        et_pd_id.setText(PDid);
//        String ServerPort = sp.getString("ServerPort", "8006");
//        String ServerBroadcastPort = sp.getString("ServerBroadcastPort", "40000");
//        cbIsstart = findViewById(R.id.cb_isstart);
//        cbIsstart.setChecked(isAutoStart.equals("Yes"));
//
//        cb_isbind.setChecked(isBindPd.equals("Yes"));
//        cb_isupdate.setChecked(isAutoUpdate.equals("Yes"));
//        cb_zhongzhuan = findViewById(R.id.cb_zhongzhuan);
//        cb_zhongzhuan.setChecked(isServer.equals("Yes"));
//        serverIP = sp.getString("serverIP", "192.168.0.222");
//        String port = sp.getString("port", "8887");
//        etServerIP.setText(serverIP);
//        etTeacherCode.setText(teachingHostCode);
//        cb_teacher.setChecked(teachingHostCode.equals("0004"));
//        cb_student.setChecked(teachingHostCode.equals(""));
//        etDeviceID.setText(deviceID);
//        etDeviceName.setText(deviceName);
//        etPort.setText(port);
//        etUser.setText(user);
//        etPwd.setText(pwd);
//        et_server_port.setText(ServerPort);
//        et_server_broadcast_port.setText(ServerBroadcastPort);
        AppUtils.AppInfo appInfoStudent = AppUtils.getAppInfo("com.yishu.student");
        AppUtils.AppInfo appInfoTeacher = AppUtils.getAppInfo("com.yishu.teaching");
        AppUtils.AppInfo appInfoLocal = AppUtils.getAppInfo(context.getPackageName());
        if (appInfoStudent != null)
            updateUIString(appInfoStudent.getVersionCode() + "  appInfoStudent");
        if (appInfoTeacher != null)
            updateUIString(appInfoTeacher.getVersionCode() + "  appInfoTeacher");
        updateUIString(appInfoLocal.getVersionCode() + "  localNet");
        butApkTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream is = getAssets().open("wifiadb.apk");
                            FileOutputStream fos = new FileOutputStream(apkPath + "/wifiadb.apk");
                            byte[] buff = new byte[1024];
                            while (true) {
                                int read = is.read(buff);
                                if (read > 0) {
                                    fos.write(buff, 0, read);
                                } else {
                                    break;
                                }
                            }
                            is.close();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SilentInstallUtils.installByShell(new File(apkPath + "/wifiadb.apk"), true);
                    }
                }).start();
//                String s=processCMD("pm install /sdcard/apk/wifiadb.apk\n");
//                Log.e("CHECK","E  "+s);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtils.isAvailable()) {
                    tv_ip.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_ip.setText("IP:未识别");
                        }
                    });
                    return;
                }
                String ip = AndroidServer.getIP();
                tv_ip.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_ip.setText("IP:" + ip);
                        int i = ip.lastIndexOf(".");
                        String broadIP=ip.substring(0,i)+".222";
                        etServerIP.setText(broadIP);
                    }
                });
            }
        }).start();
        butReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCMD("reboot");
            }
        });
        butBaocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUIString("保存成功");
//                ClientNetFuc.stopSever(MainActivity.this);
//                ClientNetFuc.startSever(MainActivity.this);
//                finish();y
            }
        });
        butSystemui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShellUtils.execCmd("settings put global policy_control immersive.full=*", true);
            }
        });
        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons.size() > 0) {
                    butUpdate.setEnabled(false);
                    buttons.get(0).callOnClick();
                }
            }
        });
    }
    private void downloadZip(FileInstall.FileClass file) {
        File zipDir=new File( apkPath+"/Zip");
        boolean exists = zipDir.exists();
        if(!exists)
            zipDir.mkdir();
        updateUIString("Start " + file.path + " " + file.aimPath);
        scp.getFile(file.path, zipDir.getPath());
        try {
            ZipUtils.unzipFile(zipDir.getPath()+"/"+file.getName(),file.aimPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateUIString("Stop " + file.path + " " + file.aimPath);

    }

    private void downloadDir(FileInstall.FileClass fileClass) {
        updateUIString("Start " + fileClass.path + " " + fileClass.aimPath);
        scp.getFile(fileClass.path, fileClass.aimPath);
        updateUIString("Stop " + fileClass.path + " " + fileClass.aimPath);
        ShellUtils.execCmd("tar " + fileClass.aimPath + "/" + fileClass.name + "", true);
    }

    private int indexP = 0;

    private void checkNext() {
        if (!butUpdate.isEnabled()) {
            indexP++;
            if (indexP == buttons.size()) {
                butUpdate.setEnabled(true);
                updateUIString(" done ");
                return;
            }
            buttons.get(indexP).callOnClick();
        }
    }

    private void downloadResource(FileInstall.FileClass fileClass) {
        updateUIString("Start " + fileClass.path + " " + fileClass.aimPath);
        scp.getFile(fileClass.path, fileClass.aimPath);
        updateUIString("Stop " + fileClass.path + " " + fileClass.aimPath);
    }

    private void checkAimPath(String aimPath) {
        File file = new File(aimPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void downloadAPK(FileInstall.FileClass fileClass) {
        updateUIString("Start " + fileClass.path + " " + fileClass.aimPath);
        scp.getFile(fileClass.path, fileClass.aimPath);
        updateUIString("Stop " + fileClass.path + " " + fileClass.aimPath);
        if (fileClass.isInstall()) {
            updateUIString("installByShell " + "/" + fileClass.name + ".apk start");
            SilentInstallUtils.installByShell(new File(fileClass.aimPath + "/" + fileClass.name + ".apk"), true);
            updateUIString("installByShell " + "/" + fileClass.name + ".apk stop");
            AppUtils.AppInfo apkInfo = AppUtils.getApkInfo(fileClass.aimPath + "/" + fileClass.name + ".apk");
            AppUtils.AppInfo appInfo = AppUtils.getAppInfo(apkInfo.getPackageName());
            updateUIString(appInfo.getVersionCode() + " " + fileClass.name);
        }
    }

    private void updateUIString(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_uitext.append(s + "\n");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static String processCMD(String cmd) {
        try {
            byte[] buffer = new byte[1024 * 4];
            Process process = Runtime.getRuntime().exec(cmd);
            int len = process.getInputStream().read(buffer);
            if (len > 0)
                return new String(buffer, 0, len);
            else
                return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}