package com.yishu.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.yishu.net.app.YsApp;
import com.yishu.net.utils.ContextUtils;
import com.yishu.server.utils.GsonUtil;

import java.util.List;


public class BootUpReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    Handler handler=new Handler();
    private SharedPreferences sp;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            String useMode=CacheDiskUtils.getInstance(context.getCacheDir()).getString(context.getPackageName() + YsApp.KEY_DEVICEUSEMODE);
            boolean empty = TextUtils.isEmpty(useMode);
            switch(empty?"":useMode){
                case YsApp.TYPE_CLASS:
                    String classType = CacheDiskUtils.getInstance(context.getCacheDir()).getString(YsApp.SHOP_PACKAGE + YsApp.CLASS_TYPE);
                    if(TextUtils.isEmpty(classType)){
                        goShop(context);
                    }else {
                        goClass(context);
                    }
                    return;
                case YsApp.TYPE_STAND:
                    String  bootPackageName=CacheDiskUtils.getInstance(context.getCacheDir()).getString(YsApp.KEY_BOOTAPPLICATION);
                    if(!TextUtils.isEmpty(bootPackageName)&& AppUtils.getAppInfo(bootPackageName)!=null){
                        CacheDiskUtils.getInstance(context.getCacheDir()).getString(context.getPackageName()+ YsApp.KEY_DEVICEUSEMODE);
                        ContextUtils.startActivity(context,bootPackageName);
                    }else {
                        goShop(context);
                    }
                    return;
            }
            goShop(context);
        }
    }

    public void goClass(Context context){
//        String isAutoStart = sp.getString("isAutoStart", "No");
//        if(isAutoStart.equals("No")){
//            goShop(context);
//            return;
//        }
        Intent serviceIntent=new Intent(context, NetStateEducationActivity.class);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(serviceIntent);
    }
    public void goShop(Context context){
        ContextUtils.startActivity(context,YsApp.SHOP_PACKAGE);
    }

}
