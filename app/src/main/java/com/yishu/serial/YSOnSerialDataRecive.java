package com.yishu.serial;

public interface YSOnSerialDataRecive {
    void onRecive(YSSerialPort serialPort,byte[] data,int size);
}
