package com.yishu.net;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yishu.net.app.YsApp;
import com.yishu.server.json.Device;
import com.yishu.server.utils.GsonUtil;

import java.util.List;

public class YsAppFuc {

    private YsAppFuc() {

    }

    public static final String packageName = "com.yishu.net";
    private static YsAppFuc ysAppFuc = null;
    private ServiceConnection mConnection = null;
    private YsAppServiceInterface manager = null;
    private String TAG = YsAppFuc.class.getSimpleName();

    public static YsAppFuc getInstance() {
        if (ysAppFuc == null)
            ysAppFuc = new YsAppFuc();
        return ysAppFuc;
    }

    public void initFuc(Context context, YsAppInterface ysAppInterface) {
        if (mConnection != null)
            return;
        mConnection = new ServiceConnection() {
            @Override
            public void onBindingDied(ComponentName name) {
                ServiceConnection.super.onBindingDied(name);
                Log.d(TAG, "onBindingDied");
            }

            @Override
            public void onNullBinding(ComponentName name) {
                ServiceConnection.super.onNullBinding(name);
                Log.d(TAG, "onNullBinding");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                manager = YsAppServiceInterface.Stub.asInterface(service);
                ysAppInterface.onSuccess(manager);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //mRemoteBookManager = null;
                Log.d(TAG, "onServiceDisconnected");
            }
        };
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName(packageName, "com.yishu.net.YsAppService"));
        context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public Device.DeviceInfo getDeviceInfo() {
        return GsonUtil.convert(getSetting(YsApp.KEY_DEVICE_INFO), Device.DeviceInfo.class);
    }
    public List<Device.DeviceSoftWare> getDeviceSoftWares() {
        return GsonUtil.convert(getSetting(YsApp.KEY_DEVICE_SOFTWARE_LIST), new TypeToken<List<Device.DeviceSoftWare>>(){}.getType());
    }
    public Device.DeviceHardWare getDeviceHardWare() {
        return GsonUtil.convert(getSetting(YsApp.KEY_DEVICE_HARDWARE), Device.DeviceHardWare.class);
    }
    public Device.OrganizationInfo getDeviceOrganizationInfo() {
        return GsonUtil.convert(getSetting(YsApp.KEY_ORGANIZATION), Device.OrganizationInfo.class);
    }

    public String getSetting(String key) {
        String data = "";
        try {
            data = manager.readSetting(packageName, key);
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
        return data;
    }
    public void wirteSetting(String key,Object object) {
        try {
            manager.wirteSetting(packageName,key,GsonUtil.toJsonFilterNullField(object));
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
    public void wirteSetting(String key,String msg) {
        try {
            manager.wirteSetting(packageName,key,msg);
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
    public void deinit(Context context) {
        context.unbindService(mConnection);
    }

    public interface YsAppInterface {
        void onSuccess(YsAppServiceInterface manager);

        void onError();
    }
}
