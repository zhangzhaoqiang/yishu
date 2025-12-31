package com.yishu.net;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;



import com.yishu.server.json.Msg;
import com.yishu.server.utils.GsonUtil;

import java.net.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;


public class ClientService extends Service implements WebSocketServer.OnStatusCallBack{
    private static final String TAG = ClientService.class.getSimpleName();
    private String ipAddressString;

    private String serverURI = "";
    private static boolean isInit = false;

    private void log(String s) {
        Log.e(TAG, s);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        WebSocketServer.getInstance().init(getApplicationContext());
        WebSocketServer.getInstance().registerCallBack(this);
        WebSocketServer.getInstance().startReciveBroadCast();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private static String getSendMsgString(int msg_code, String data, String note) {
        String json = "";
        Msg msg = new Msg();
        msg.msg_code = msg_code;
        msg.note = note;
        msg.data = data;
        json = GsonUtil.toJsonFilterNullField(msg);
        return json;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log("onBind "+intent.getPackage());
        return mBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        log("unBind ");
        super.unbindService(conn);
    }

//    private static RemoteCallbackList<OnServiceListener> mListenerList = new RemoteCallbackList<>();
    private static ArrayList<OnServiceListener> arrayList=new ArrayList<>();
    private IBinder mBinder = new IClientServiceInterface.Stub() {

        @Override
        public void starConnect(String serverIP, int port, String desc) throws RemoteException {
            if(TextUtils.isEmpty(serverIP)){
                WebSocketServer.getInstance().startReciveBroadCast();
            }else {
                WebSocketServer.getInstance().connectWebSocket(serverIP,port,desc);
            }
        }

        @Override
        public void sendData(String json) throws RemoteException {
            WebSocketServer.getInstance().senMsg(json);
        }

        @Override
        public boolean isConnect() throws RemoteException {
            return  WebSocketServer.getInstance().isConnect();
        }

        @Override
        public void registerListener(OnServiceListener listener) throws RemoteException {
            arrayList.add(listener);
//            mListenerList.register(listener);
//            int num = mListenerList.beginBroadcast();
//            mListenerList.finishBroadcast();
//            Log.e(TAG, "添加完成, 注册接口数: " + num + " " + ClientService.this + " " + mListenerList);
        }

        @Override
        public void unregisterListener(OnServiceListener listener) throws RemoteException {
            arrayList.remove(listener);
//            mListenerList.unregister(listener);
//            int num = mListenerList.beginBroadcast();
//            mListenerList.finishBroadcast();
//            Log.e(TAG, "删除完成, 注册接口数: " + num);
        }

        @Override
        public void setSetting(String deviceID, String teachHostCode, String deviceName, String serverIP) throws RemoteException {
            log(serverIP + " 设备服务链接重启");
        }

        @Override
        public String getSetting() throws RemoteException {
            return "";
        }


        @Override
        public void startNetWork(int time) throws RemoteException {

        }

        @Override
        public void cancel() throws RemoteException {

        }

        @Override
        public void restart() throws RemoteException {

        }
    };


    @Override
    public boolean onUnbind(Intent intent) {
        log("onUnbind " + intent.getPackage());
        return super.onUnbind(intent);
    }


    @Override
    public void onClientConnect() {
        for(OnServiceListener listener:arrayList){
            try {
                listener.OnConnect();
            } catch (RemoteException e) {

            }
        }
//        int num = mListenerList.beginBroadcast();
//        for (int i = 0; i < num; ++i) {
//            OnServiceListener listener = mListenerList.getBroadcastItem(i);
//            try {
//                listener.OnConnect();
//            } catch (RemoteException e) {
//
//            }
//        }
//        mListenerList.finishBroadcast();
    }

    @Override
    public void onClientDisConnect() {
        for(OnServiceListener listener:arrayList){
            try {
                listener.OnDisConnect();
            } catch (RemoteException e) {

            }
        }
//        int num = mListenerList.beginBroadcast();
//        for (int i = 0; i < num; ++i) {
//            OnServiceListener listener = mListenerList.getBroadcastItem(i);
//            try {
//                listener.OnDisConnect();
//            } catch (RemoteException e) {
//
//            }
//        }
//        mListenerList.finishBroadcast();
    }

    @Override
    public void OnRecived(String msg) {
        ArrayList<OnServiceListener> remove=new ArrayList<>();
        for(OnServiceListener listener:arrayList){
            try {
                listener.OnRecived(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
                remove.add(listener);
            }
        }
        for(OnServiceListener re:remove){
            arrayList.remove(re);
        }

//        int num = mListenerList.beginBroadcast();
//        Log.e(TAG, num + " beginBroadcast" + " " + mListenerList);
//        for (int i = 0; i < num; ++i) {
//            OnServiceListener listener = mListenerList.getBroadcastItem(i);
//            try {
//                listener.OnRecived(msg);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//                Log.e(TAG, num + " " + e.getMessage());
//            }
//            Log.e(TAG, num + " " + listener);
//        }
//        mListenerList.finishBroadcast();
//        Log.e(TAG, num + " finishBroadcast");
    }


    public interface ClientServiceStatus {
        void onClientConnect();

        void onClientDisConnect();

        void LogMsg(String log);
    }

    public interface MessageListener {
        void onRecived(String msg);

        void onSend(String msg);
    }

    //获取本机IP地址
    public static String getIpAddressString() {

        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }
}
