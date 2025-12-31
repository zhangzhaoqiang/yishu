// IClientServiceInterface.aidl
package com.yishu.net;

import com.yishu.net.OnServiceListener;
// Declare any non-default types here with import statements

interface IClientServiceInterface {
    void starConnect(String serverIP,int port,String desc);//初始化链接
    void sendData(String s);
    boolean isConnect();
    void registerListener(OnServiceListener listener); // 注册接口
    void unregisterListener(OnServiceListener listener); // 退订接口
    void setSetting(String deviceID,String teachHostCode,String deviceName,String serverIP);//添加配置项目
    String getSetting();
    void startNetWork(int time);
    void cancel();//清除链接
    void restart();//重启链接
}