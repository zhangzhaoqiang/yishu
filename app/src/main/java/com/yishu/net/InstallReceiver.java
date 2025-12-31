package com.yishu.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.yishu.net.app.YsApp;
import com.yishu.net.utils.DownloadAPK;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class InstallReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.i(TAG, "--------安装成功" + packageName);

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (packageName.equals(context.getPackageName())) {
                String string = CacheDiskUtils.getInstance(context.getCacheDir()).getString(YsApp.SHOP_PACKAGE + YsApp.CLASS_TYPE);
                switch (string) {
                    case "中转服务":
                        ShellUtils.execCmd("reboot", true);
                        break;
                }
//                String s = FileIOUtils.readFile2String(new File(DownloadAPK.path, "update.txt"));
//                if(s.contains("reboot")){
//                    FileIOUtils.writeFileFromString(new File(DownloadAPK.path,"update.txt"),"");
//                    ShellUtils.execCmd("reboot", true);
//                }
            }

            Log.i(TAG, "--------替换成功" + packageName);
        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.i(TAG, "--------卸载成功" + packageName);
        }
    }

}
