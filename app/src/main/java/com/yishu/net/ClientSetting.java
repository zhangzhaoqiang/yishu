package com.yishu.net;

import android.text.TextUtils;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.yishu.net.app.YsApp;
import com.yishu.server.json.User;
import com.yishu.server.utils.GsonUtil;

import java.io.Serializable;

public class ClientSetting implements Serializable {
    public String version;
    public String name;//用户名称
    public String SerialNumber;//课堂预留编号
    public String ip;//设备IP
    public String deviceName;//设备名称
    public String deviceID;//设备ID
    public String serverIP;//课堂中服务端 ip
    public String port;//服务端链接 端口
    public String bordcastPort;//搜索广播端口
    public String user;//课堂用户
    public String pwd;//课堂密码
    public User userLocal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBordcastPort() {
        return bordcastPort;
    }

    public void setBordcastPort(String bordcastPort) {
        this.bordcastPort = bordcastPort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public static ClientSetting getDefault(){
        ClientSetting clientSetting=new ClientSetting();
        clientSetting.bordcastPort="";
        clientSetting.deviceID="";
        clientSetting.deviceName="";
        clientSetting.SerialNumber="";
        clientSetting.ip="";
        clientSetting.port="";


        return clientSetting;
    }
    public static ClientSetting loadSetting(){
        String string = CacheDiskUtils.getInstance().getString(YsApp.KEY_CLIENT_SETTING);
        if(TextUtils.isEmpty(string)){
            ClientSetting clientSetting=getDefault();
            clientSetting.bordcastPort="40000";
            clientSetting.port="8787";
            return clientSetting;
        }

        ClientSetting clientSetting= GsonUtil.convert(string,ClientSetting.class);
        return clientSetting;
    }
}
