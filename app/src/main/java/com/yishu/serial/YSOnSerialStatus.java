package com.yishu.serial;

public interface YSOnSerialStatus {
    void onConnect(YSSerialPort serialPath) ;
    void onError(String serialPath,String errorMsg);
}
