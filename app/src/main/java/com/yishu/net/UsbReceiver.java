package com.yishu.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ShellUtils;

public class UsbReceiver extends BroadcastReceiver {

    public static final String TAG="YSUsbReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        // 这里可以拿到插入的USB设备对象
        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        switch (intent.getAction()) {
            case UsbManager.ACTION_USB_DEVICE_ATTACHED: // 插入USB设备
                if (isUsbCameraDevice(usbDevice)) {
//                    for(int i=0;i<5;i++){
//                        ShellUtils.execCmd("chmod 777 /dev/video"+i,true);
//                    }
                    Log.e("CHECK","Usb摄像头已插入");
//                    Toast.makeText(UsbCameraActivity.this, "Usb摄像头已插入", Toast.LENGTH_LONG).show();
                    //do some thing
                }
                break;
            case UsbManager.ACTION_USB_DEVICE_DETACHED: // 拔出USB设备
                if (isUsbCameraDevice(usbDevice)) {
                    Log.e("CHECK","Usb摄像头已拔出");
//                    Toast.makeText(UsbCameraActivity.this, "Usb摄像头已拔出", Toast.LENGTH_LONG).show();
                    //do some thing
                }
                break;
            default:
                break;
        }

    }
    private static final int USB_CAMERA_TYPE = 14;
    /**
     * 判断当前Usb设备是否是Camera设备
     */
    private boolean isUsbCameraDevice(UsbDevice usbDevice) {
        Log.i(TAG, "isUsbCameraDevice  usbDevice " + usbDevice.getProductName() + usbDevice.getDeviceClass() + ", subclass = " + usbDevice.getDeviceSubclass());
        if (usbDevice == null) {
            return false;
        }
        boolean isCamera = false;
        int interfaceCount = usbDevice.getInterfaceCount();
        for (int interIndex = 0; interIndex < interfaceCount; interIndex++) {
            UsbInterface usbInterface = usbDevice.getInterface(interIndex);
            //usbInterface.getName()遇到过为null的情况
            if ((usbInterface.getName() == null || usbDevice.getProductName().equals(usbInterface.getName())) && usbInterface.getInterfaceClass() == USB_CAMERA_TYPE) {
                isCamera = true;
                break;
            }
        }
        Log.i(TAG, "onReceive usbDevice = " + usbDevice.getProductName() + "isCamera = " + isCamera);
        return isCamera;
    }

}
