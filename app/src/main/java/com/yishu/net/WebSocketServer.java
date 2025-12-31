package com.yishu.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.yishu.net.app.YsApp;
import com.yishu.server.BroadcastClient;
import com.yishu.server.json.Broadcast;
import com.yishu.server.json.Device;
import com.yishu.server.json.Msg;
import com.yishu.server.utils.GsonUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class WebSocketServer {
    public static int BROADCAST_PROT = 40000;

    private WebSocketServer() {

    }

    private static WebSocketServer webSocketServer;
    private WebSocketClient webSocketClient;
    private BroadcastClient broadcastClient;

    private String ip;
    private int port;
    private String serverURI = "";
    private static final String TAG = WebSocketServer.class.getSimpleName();

    private void log(String s) {
        Log.e(TAG, s);
    }

    private Context context;

    public static WebSocketServer getInstance() {
        if (webSocketServer == null)
            webSocketServer = new WebSocketServer();
        return webSocketServer;
    }
    public String deviceDec="";
    public void init(Context context) {
        this.context = context;
        loadDeviceDec();
    }
    public void loadDeviceDec(){
        String string = CacheDiskUtils.getInstance(context.getCacheDir()).getString(YsApp.SHOP_PACKAGE + YsApp.CLASS_TYPE);
        String deviceName = CacheDiskUtils.getInstance(context.getCacheDir()).getString(YsApp.KEY_DEVICENAME);
        switch (string) {
            case "学员机":
                deviceDec="student";
                break;
            case "教师机":
                deviceDec="teacher";
                break;
        }
        serverURI = getServerUri(ip, port+"", deviceDec+deviceName);
        log(""+deviceDec+" "+string+" "+serverURI);
    }
    public void startReciveBroadCast() {
        broadcastClient = new BroadcastClient(BROADCAST_PROT);
        broadcastClient.startFind(new BroadcastClient.OnStatusListner() {
            @Override
            public void onFind(Broadcast broadcast) {
                log(broadcast.getIp() + " " + broadcast.getPort() + " " + broadcast.getYishu());
                ip = broadcast.getIp();
                port = Integer.parseInt(broadcast.getPort());
                serverURI = getServerUri(broadcast.getIp(), broadcast.getPort(), deviceDec);
                log(serverURI);
                onAutoConnect();
            }
        });
    }

    public void onAutoConnect() {
        if (isWebSocketValue()) {
            try {
                initAutoSocketService();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectWebSocket(String serverIP, int port, String desc) {
        if (isWebSocketValue()) {
            try {
                initWebSocketService(serverIP, port, desc);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isWebSocketValue() {
        if (webSocketClient == null)
            return true;
        if (!webSocketClient.isOpen() || webSocketClient.isClosed()) {
            return true;
        }
        return false;
    }

    public void initAutoSocketService() throws URISyntaxException {
        loadDeviceDec();
        initWebSocketService(ip, port, deviceDec);
    }

    public void initWebSocketService(String serverIP, int port, String desc) throws URISyntaxException {
        webSocketClient = new WebSocketClient(new URI(serverURI)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                log("onOpen");
                Device device = new Device();
                String deviceName = CacheDiskUtils.getInstance(context.getCacheDir()).getString(YsApp.KEY_DEVICENAME);
                String deviceInfoJson=CacheDiskUtils.getInstance(context.getCacheDir()).getString("com.yishu.net" + YsApp.KEY_DEVICE_INFO);
                Device.DeviceInfo deviceInfo=GsonUtil.convert(deviceInfoJson, Device.DeviceInfo.class);
                device.deviceInfo=deviceInfo;
                device.deviceId = deviceInfo.device_id;
                device.deviceName = deviceName;
                device.deviceVersion = BuildConfig.VERSION_NAME;
                String string = CacheDiskUtils.getInstance(context.getCacheDir()).getString(YsApp.SHOP_PACKAGE + YsApp.CLASS_TYPE);
                device.device_nick_name=string+deviceInfo.device_index;
                String json = GsonUtil.toJsonWtihNullField(device);
                webSocketClient.send(getSendMsgString(Device.SET_DEVICE_INFO, json, "上传设备信息"));
                if (onStatusCallBack != null)
                    onStatusCallBack.onClientConnect();
            }

            @Override
            public void onMessage(String message) {
                log("onMessage: " + message);
                if (onStatusCallBack != null)
                    onStatusCallBack.OnRecived(message);
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                log("onMessage: bytes " + bytes.toString());
                super.onMessage(bytes);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                log("onClose:  " + code+" "+reason+" "+remote);
                if (onStatusCallBack != null)
                    onStatusCallBack.onClientDisConnect();
                startReciveBroadCast();
            }

            @Override
            public void onError(Exception ex) {
                log("onError:  " + ex.toString()+" "+ex.getMessage()+" "+ex);
                ex.printStackTrace();
            }
        };
        webSocketClient.setConnectionLostTimeout(2000);
        webSocketClient.connect();
    }

    public synchronized void senMsg(String msg) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(msg);
        }
    }

    public boolean isServerDiff(String serverIP, String port) {
        if (TextUtils.isEmpty(ip))
            return true;
        if (serverIP.equals(ip)) {
            return false;
        }
        return true;
    }

    public String getServerUri(String serverIP, String port, String desc) {
        return "ws://" + serverIP + ":" + port + "/" + desc;
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

    private OnStatusCallBack onStatusCallBack;

    public void registerCallBack(OnStatusCallBack onStatusCallBack) {
        this.onStatusCallBack = onStatusCallBack;
    }

    public boolean isConnect() {
        if(webSocketClient==null)
            return false;
        return webSocketClient.isOpen();
    }


    interface OnStatusCallBack {
        void onClientConnect();

        void onClientDisConnect();

        void OnRecived(String message);
    }
}
