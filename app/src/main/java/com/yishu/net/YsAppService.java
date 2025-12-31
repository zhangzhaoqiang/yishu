package com.yishu.net;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.google.gson.reflect.TypeToken;
import com.yishu.net.app.Source;
import com.yishu.net.app.YsApp;
import com.yishu.net.ui.LoadingDialog;
import com.yishu.net.utils.SilentInstallUtils;

import java.io.File;
import java.util.List;

public class YsAppService extends Service implements NetworkUtils.OnNetworkStatusChangedListener {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkUtils.registerNetworkStatusChangedListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return appBinder;
    }

    public void showLoading() {
        Handler handler = new Handler(getApplicationContext().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog == null)
                    loadingDialog = new LoadingDialog(getApplicationContext());
            }
        });
    }

    public void dissmissLoading() {
        Handler handler = new Handler(getApplicationContext().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dissmiss();
                loadingDialog=null;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkUtils.unregisterNetworkStatusChangedListener(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private static RemoteCallbackList<YsAppServiceListener> mListenerList = new RemoteCallbackList<>();
    private Binder appBinder = new YsAppServiceInterface.Stub() {
        @Override
        public boolean installApk(String apkfile) throws RemoteException {
            return SilentInstallUtils.installByShell(new File(apkfile), true);
        }

        @Override
        public String shellCMD(String cmd) throws RemoteException {
            ShellUtils.CommandResult commandResult = ShellUtils.execCmd(cmd, true);
            if (commandResult.result == 0)
                return commandResult.successMsg;
            return commandResult.errorMsg;
        }

        @Override
        public String getDeviceName() throws RemoteException {
            return CacheDiskUtils.getInstance(getCacheDir()).getString(YsApp.KEY_DEVICENAME);
        }

        @Override
        public boolean setDevice(String rsa, String deviceName) throws RemoteException {
            if (TextUtils.isEmpty(CacheDiskUtils.getInstance(getCacheDir()).getString("device"))) {
                CacheDiskUtils.getInstance(getCacheDir()).put("device", rsa);
                CacheDiskUtils.getInstance(getCacheDir()).put(YsApp.KEY_DEVICENAME, deviceName);
                return true;
            }
            return false;
        }

        @Override
        public void setBootApplication(String packageName) throws RemoteException {
            CacheDiskUtils.getInstance(getCacheDir()).put(YsApp.KEY_BOOTAPPLICATION, packageName);
        }

        @Override
        public String getBootApplication() throws RemoteException {
            return CacheDiskUtils.getInstance(getCacheDir()).getString(YsApp.KEY_BOOTAPPLICATION);
        }

        @Override
        public String readSetting(String packageName, String key) throws RemoteException {
            return CacheDiskUtils.getInstance(getCacheDir()).getString(packageName + key);
        }

        @Override
        public void wirteSetting(String packageName, String key, String value) throws RemoteException {
            if(key.startsWith("cmd_")){
                YsAppCMD.deal(key,value);
                return;
            }
            CacheDiskUtils.getInstance(getCacheDir()).put(packageName + key, value);
        }

        @Override
        public void registerListener(YsAppServiceListener listener) throws RemoteException {
            mListenerList.register(listener);
            int num = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
        }

        @Override
        public void unregisterListener(YsAppServiceListener listener) throws RemoteException {
            mListenerList.unregister(listener);
            int num = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
        }

        @Override
        public String getIP() throws RemoteException {
            return NetworkUtils.getIPAddress(true);
        }

        @Override
        public String getLocal() throws RemoteException {
            return null;
        }

        @Override
        public void hideTopBottom() throws RemoteException {
            ShellUtils.execCmd("settings put global policy_control immersive.full=*", true);
            CacheDiskUtils.getInstance(getCacheDir()).put(YsApp.KEY_TOPBOTTOMSHOW_STATUS, "false");
        }

        @Override
        public void showTopBottom() throws RemoteException {
            ShellUtils.execCmd("adb shell settings put global policy_control null", true);
            CacheDiskUtils.getInstance(getCacheDir()).put(YsApp.KEY_TOPBOTTOMSHOW_STATUS, "true");
        }

        @Override
        public void lockTopBottom() throws RemoteException {
            Intent intent = new Intent("android.intent.action.HIDE_SYSTEM_BAR");
            sendBroadcast(intent);
            CacheDiskUtils.getInstance(getCacheDir()).put(YsApp.KEY_TOPBOTTOMLOCK_STATUS, "true");
        }

        @Override
        public void unLockTopBottom() throws RemoteException {
            Intent intent = new Intent("android.intent.action.SHOW_SYSTEM_BAR");
            sendBroadcast(intent);
            CacheDiskUtils.getInstance(getCacheDir()).put(YsApp.KEY_TOPBOTTOMLOCK_STATUS, "false");
        }

        @Override
        public boolean isLockTopBottom() throws RemoteException {
            boolean flag = false;
            String flagS = CacheDiskUtils.getInstance(getCacheDir()).getString(YsApp.KEY_TOPBOTTOMLOCK_STATUS);
            if (!TextUtils.isEmpty(flagS)) {
                flag = flagS.equals("true");
            }
            return flag;
        }

        @Override
        public boolean isShowTopBottom() throws RemoteException {
            boolean flag = false;
            String flagS = CacheDiskUtils.getInstance(getCacheDir()).getString(YsApp.KEY_TOPBOTTOMSHOW_STATUS);
            if (!TextUtils.isEmpty(flagS)) {
                flag = flagS.equals("true");
            }
            return flag;
        }

        @Override
        public void setDeviceUseMode(String type) throws RemoteException {
            CacheDiskUtils.getInstance(getCacheDir()).put(getPackageName() + YsApp.KEY_DEVICEUSEMODE, type);
        }

        @Override
        public String getDeviceUseMode() throws RemoteException {
            return CacheDiskUtils.getInstance(getCacheDir()).getString(getPackageName() + YsApp.KEY_DEVICEUSEMODE);
        }

        @Override
        public void setWIFIADBEnable(boolean enable) throws RemoteException {
            if (enable) {
                ShellUtils.execCmd("setprop service.adb.tcp.port 5555", true);
                ShellUtils.execCmd("stop adbd", true);
                ShellUtils.execCmd("start adbd", true);
            } else {
                ShellUtils.execCmd("stop adbd", true);
            }
            CacheDiskUtils.getInstance(getCacheDir()).put(getPackageName() + YsApp.KEY_WIFIADB_STATUS, enable + "");
        }

        @Override
        public boolean getWIFIADBEnable() throws RemoteException {
            String flag = CacheDiskUtils.getInstance(getCacheDir()).getString(getPackageName() + YsApp.KEY_WIFIADB_STATUS);
            if (!TextUtils.isEmpty(flag))
                return Boolean.parseBoolean(flag);
            return false;
        }

        @Override
        public void setUSBADBEnable(boolean enable) throws RemoteException {
            String mode = enable ? "2" : "1";
            ShellUtils.CommandResult commandResult = ShellUtils.execCmd("setprop persist.usb.mode " + mode, true);
            Log.e("CHECK", commandResult.result + " " + commandResult.errorMsg + " setprop " + commandResult.successMsg);
            if (enable) {
                ShellUtils.CommandResult commandResult1 = ShellUtils.execCmd("echo 2 > /sys/kernel/debug/usb@fe800000/rk_usb_force_mode", true);
                Log.e("CHECK", commandResult1.result + " " + commandResult1.errorMsg + " " + commandResult1.successMsg);
            } else {
                ShellUtils.CommandResult commandResult1 = ShellUtils.execCmd("echo 1 > /sys/kernel/debug/usb@fe800000/rk_usb_force_mode", true);
                Log.e("CHECK", commandResult1.result + " " + commandResult1.errorMsg + " " + commandResult1.successMsg);
            }
            CacheDiskUtils.getInstance(getCacheDir()).put(getPackageName() + YsApp.KEY_USBADB_STATUS, enable + "");
        }

        @Override
        public boolean getUSBADBEnable() throws RemoteException {
            String flag = CacheDiskUtils.getInstance(getCacheDir()).getString(getPackageName() + YsApp.KEY_USBADB_STATUS);
            if (!TextUtils.isEmpty(flag))
                return Boolean.parseBoolean(flag);
            return false;
        }

        @Override
        public void downloadSource(String sourceJson) throws RemoteException {

        }

        @Override
        public void cancelDownload() throws RemoteException {

        }

        @Override
        public String checkSource(String sourceJson) throws RemoteException {
            return null;
        }

        @Override
        public void showError(String title, String msg) throws RemoteException {

        }
    };

    @Override
    public void onDisconnected() {
        int num = mListenerList.beginBroadcast();
        try {
            for (int i = 0; i < num; ++i) {
                YsAppServiceListener listener = mListenerList.getBroadcastItem(i);
                listener.onNetDisconnect();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mListenerList.finishBroadcast();
    }

    @Override
    public void onConnected(NetworkUtils.NetworkType networkType) {
        int num = mListenerList.beginBroadcast();
        try {
            for (int i = 0; i < num; ++i) {
                YsAppServiceListener listener = mListenerList.getBroadcastItem(i);
                listener.onNetChange(NetworkUtils.getIPAddress(true));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mListenerList.finishBroadcast();
    }

    public void onDownloadSoure(String source) {
        int num = mListenerList.beginBroadcast();
        try {
            for (int i = 0; i < num; ++i) {
                YsAppServiceListener listener = mListenerList.getBroadcastItem(i);
                listener.onDownloadSoure(source);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mListenerList.finishBroadcast();
    }

    public void onDownloadComplete() {
        int num = mListenerList.beginBroadcast();
        try {
            for (int i = 0; i < num; ++i) {
                YsAppServiceListener listener = mListenerList.getBroadcastItem(i);
                listener.onDownloadComplete();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mListenerList.finishBroadcast();
    }

    public void onDownloadError(String source) {
        int num = mListenerList.beginBroadcast();
        try {
            for (int i = 0; i < num; ++i) {
                YsAppServiceListener listener = mListenerList.getBroadcastItem(i);
                listener.onDownloadError(source);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mListenerList.finishBroadcast();
    }
}
