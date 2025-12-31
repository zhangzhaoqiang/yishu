package com.yishu.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BlueAutoParingReciver extends BroadcastReceiver {
    public BlueAutoParingReciver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            for(String name:autoConnect){
                if (bluetoothDevice.getName().equals(name)) {
                    this.abortBroadcast();
                    try {
                        ClsUtils.setPin(bluetoothDevice.getClass(), bluetoothDevice, "1234");
                    } catch (Exception var5) {
                        var5.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public static String[] autoConnect={
            "ZGGC-M-XFFS","ZGGC-M-QDGS","ZGGC-S-XFFS","ZGGC-s-QDGS"
    };

    public static boolean isAuto=true;
}
