package com.yishu.serial;

import android.serialport.SerialPort;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class YSSerialPort implements Runnable {
    private static final String TAG = YSSerialPort.class.getName();
    public int port = 115200;//波特率
    public String serialPath = "/dev/ttysWK0";//串口地址
    public InputStream serialInput;// I/O
    public OutputStream serialOutput;//I/O
    private SerialPort serialPort;//串口对象
    private Thread loopReciveThread;
    private long handle;
    private boolean isLoopRecive;
    private boolean isExit;

    public YSSerialPort(int port, String serialPath) {
        this.port = port;
        this.serialPath = serialPath;
        handle = System.currentTimeMillis();
    }

    public boolean initSerialPort() {
        try {
            serialPort = SerialPort.newBuilder(serialPath, port).build();
            serialInput = serialPort.getInputStream();
            serialOutput = serialPort.getOutputStream();
            isExit = false;
            Log.e(TAG, "初始化成功:" + serialPath + " " + serialPort + " \n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "初始化失败:" + serialPath + " " + serialPort + " \n" + e.toString());
            return false;
        }

    }

    public void startLoopRecive() {
        if (loopReciveThread == null) {
            isLoopRecive = true;
            loopReciveThread = new Thread(this);
            loopReciveThread.start();
        }
    }

    public void stopLoopRecive() {
        if (loopReciveThread != null) {
            isLoopRecive = false;
            loopReciveThread.interrupt();
            loopReciveThread = null;
        }
    }


    public void destory() {
        stopLoopRecive();
        isExit = true;
    }

    @Override
    public void run() {
        while (true) {
            if (isExit) {
                break;
            }
            if (!isLoopRecive) {
                continue;
            }
            if (loopReciveThread.isInterrupted())
                return;
            try {
                int available = serialInput.available();
                if (available == 0)
                    continue;
                byte[] received = new byte[1024];
                int size = serialInput.read(received);//读取以下串口是否有新的数据
                if (size > 0) {
                    if (onSerialDataRecive != null)
                        onSerialDataRecive.onRecive(this, received, size);
                }
            } catch (IOException e) {
                Log.e(TAG, "串口读取数据异常:" + serialPath + " " + serialPort + " \n" + e.toString());
                break;
            }
        }
        try {
            serialOutput.close();
            serialInput.close();
        } catch (Exception e) {
            Log.e(TAG, "串口关闭异常:" + serialPath + " " + serialPort + " \n" + e.toString());
        }
        serialPort.close();
        Log.e(TAG, "销毁成功:" + serialPath + " " + serialPort + " \n");
    }

    YSOnSerialDataRecive onSerialDataRecive;
    YSOnSerialStatus onSerialStatus;

    public void setOnSerialDataRecive(YSOnSerialDataRecive onSerialDataRecive) {
        this.onSerialDataRecive = onSerialDataRecive;
    }

    public void setOnSerialStatus(YSOnSerialStatus onSerialStatus) {
        this.onSerialStatus = onSerialStatus;
    }

    public void writeData(byte[] buffer) {
        try {
            if (serialOutput != null)
                serialOutput.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
