// IClientServiceInterface.aidl
package com.yishu.net;

import com.yishu.net.YsAppServiceListener;
// Declare any non-default types here with import statements

interface YsAppServiceInterface {

    boolean installApk(String apkfile); //安装APk
    String shellCMD(String cmd); //执行shell
    String getDeviceName();//获取设备名称
    boolean setDevice(String rsa,String deviceName); //SRA 备用密钥 设置设备名称
    void setBootApplication(String packageName); //设置启动项包名
    String getBootApplication(); //获取启动项包名
    String readSetting(String packageName,String key); //自定义内容读取
    void wirteSetting(String packageName,String key,String value); //自定义内容写入
    void registerListener(YsAppServiceListener listener); //注册返回监听
    void unregisterListener(YsAppServiceListener listener); //销毁监听
    String getIP(); //获取IP
    String getLocal(); //获取地址
    void hideTopBottom(); //隐藏上下
    void showTopBottom(); //显示上下
    void lockTopBottom(); //锁定上下
    void unLockTopBottom(); //解锁上下
    boolean isLockTopBottom();//获取上下锁定情况
    boolean isShowTopBottom();//获取上下显示情况
    void setDeviceUseMode(String type);//课堂模式 单机模式
    String getDeviceUseMode();//课堂模式 单机模式
    void setWIFIADBEnable(boolean enable);//wifi adb开关
    boolean getWIFIADBEnable();
    void setUSBADBEnable(boolean enable);//usb adb开关
    boolean getUSBADBEnable();
    //下载服务
    void downloadSource(String sourceJson);//资源下载 格式 List<Source>
    void cancelDownload();//取消资源下载
    String checkSource(String sourceJson);//检查缺损的资源 输入要求的 List<Source> 返回有缺损List<Source>
    //显示异常UI
    void showError(String title,String msg);

}