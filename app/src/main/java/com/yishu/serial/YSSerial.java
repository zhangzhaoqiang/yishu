package com.yishu.serial;


import android.serialport.SerialPortFinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class YSSerial {
    private static YSSerial serial;
    private static final String TAG = YSSerial.class.getName();
    private SerialPortFinder serialPortFinder;//搜寻串口对象

    public static YSSerial getInstance() {
        if (serial == null) {
            serial = new YSSerial();
        }
        return serial;
    }

    private YSSerial() {
        serialPortFinder = new SerialPortFinder();
        serialPortHashMap = new HashMap<>();
    }

    public List<String> getSupportSerialList() {
        List<String> stringList = new ArrayList<>();
        stringList = Arrays.asList(serialPortFinder.getAllDevicesPath());
        for (String s : stringList) {
            Log.e(TAG, s);
        }
        if(stringList.size()==0){
            Log.e(TAG, "无可用串口连接");
        }
        return Arrays.asList(serialPortFinder.getAllDevicesPath());
    }

    public void sendData(String serialPath, byte[] buffer) {
        YSSerialPort ysSerialPort = serialPortHashMap.get(serialPath);
        if (ysSerialPort != null) {
            ysSerialPort.writeData(buffer);
        }
    }

    public HashMap<String, YSSerialPort> serialPortHashMap;

    public YSSerialPort build(String path, int port) {
        YSSerialPort ysSerialPort = new YSSerialPort(port, path);
        if (ysSerialPort.initSerialPort()) {
            serialPortHashMap.put(path, ysSerialPort);
            return ysSerialPort;
        }
        return null;
    }

    public YSSerialPort get(String path) {
        return serialPortHashMap.get(path);
    }

    public void destroy(String path) {
        for (Map.Entry<String, YSSerialPort> portEntry : serialPortHashMap.entrySet()) {
            if (portEntry.getKey().equals(path))
                portEntry.getValue().destory();
        }
    }

    public void destroy() {
        for (Map.Entry<String, YSSerialPort> portEntry : serialPortHashMap.entrySet()) {
            portEntry.getValue().destory();
        }
    }

}
