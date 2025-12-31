// OnServiceListener.aidl
package com.yishu.net;

// Declare any non-default types here with import statements

interface YsAppServiceListener {

    void onNetChange(String ip); //网络变化
    void onNetDisconnect(); //网络断开
    void onDownloadSoure(String source);//正在下载的资源
    void onDownloadComplete();//下载全部完成
    void onDownloadError(String source);//下载错误的资源
}