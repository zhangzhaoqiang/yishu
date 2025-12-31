package com.yishu.net;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.yishu.server.json.Heart;
import com.yishu.server.json.Msg;
import com.yishu.server.utils.GsonUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientNetFuc {
    private static String TAG = "ClientNetFuc";
    private static IClientServiceInterface manager = null;
    private static OnServiceListener onServiceListener = null;
    private static ServiceConnection mConnection = null;
    private static NetHeartThread netHeartThread = null;

    private static RemoteCallbackList<IClientServiceInterface> remoteCallbackList = new RemoteCallbackList<>();
    public static boolean isValue(){
        return manager==null;
    }
    public static void initFucConnection(Context context, OnServiceListener listener) {
        onServiceListener = listener;
        if (manager != null)
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
                manager = IClientServiceInterface.Stub.asInterface(service);
                remoteCallbackList.register(manager);
                if (onServiceListener != null) {
                    try {
                        manager.registerListener(onServiceListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                if (aidlListenner != null) {
                    aidlListenner.OnAppAIDLConnect();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                remoteCallbackList.unregister(manager);
                //mRemoteBookManager = null;
                Log.d(TAG, "onServiceDisconnected");
            }
        };
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.yishu.net", "com.yishu.net.ClientService"));
        context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public static void startHeart(int heart) {
        if (netHeartThread == null)
            netHeartThread = new NetHeartThread();
        netHeartThread.setWorkTask(heart, new NetHeartThread.WorkTask() {
            @Override
            public void run() {
                Heart heart = new Heart(System.currentTimeMillis());
                String json = "";
                Msg msg = new Msg();
                msg.msg_code = Heart.HEART_NET;
                msg.note = "心跳包";
                msg.data = GsonUtil.toJsonWtihNullField(heart);
                json = GsonUtil.toJsonFilterNullField(msg);
                sendJson(json);
            }
        });
        netHeartThread.startThread();
    }

    public static void stopHeart() {
        if (netHeartThread != null) {
            netHeartThread.stopThread();
        }

    }

    public static void destory(Context context) {
        if (manager != null && manager.asBinder().isBinderAlive()) {
            try {
                manager.unregisterListener(onServiceListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        try {
            context.unbindService(mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        manager = null;
        mConnection = null;
    }

    static Lock mLock = new ReentrantLock();

    public static void sendJson(String json, String log) {
        Log.e("CHECK", " in " + log);
        sendJson(json);
        Log.e("CHECK", " out " + log);
    }

    public static void sendJson(String json) {
        if (manager != null) {
            mLock.lock();
            int size = remoteCallbackList.beginBroadcast();
            for (int i = 0; i < size; i++) {
                IClientServiceInterface broadcastItem = remoteCallbackList.getBroadcastItem(i);
                try {
                    broadcastItem.sendData(json);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            remoteCallbackList.finishBroadcast();
            mLock.unlock();
        }
    }

    public static void setSetting(String deviceId, String deviceName, String teachCode, String serverIP) {
        if (manager != null) {
            try {
                manager.setSetting(deviceId, teachCode, deviceName, serverIP);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getSetting() {
        String s = "";
        if (manager != null) {
            try {
                s = manager.getSetting();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    public static boolean isConnect() {
        if (manager != null) {
            try {
                return manager.isConnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void cancel() {
        if (manager != null) {
            try {
                manager.cancel();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void restart() {
        if (manager != null) {
            try {
                manager.restart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static AIDLListenner aidlListenner;

    public static void setAidlListenner(AIDLListenner aidlListenner) {
        ClientNetFuc.aidlListenner = aidlListenner;
    }

    public interface AIDLListenner {
        void OnAppAIDLConnect();
    }
}
