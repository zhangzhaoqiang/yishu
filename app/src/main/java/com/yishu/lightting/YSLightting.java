package com.yishu.lightting;

import com.yishu.hardware.YSBroadSetting;
import com.yishu.serial.YSSerial;
import com.yishu.serial.YSSerialPort;

import java.util.Timer;
import java.util.TimerTask;

public class YSLightting {
    YSSerialPort port;
    public static int LighttingPort = 115200;

    private YSLightting() {

        if (YSBroadSetting.checkBroadLightting(YSSerial.getInstance().getSupportSerialList())) {
            port = YSSerial.getInstance().build(YSBroadSetting.getInstance().getBroadLightting(), LighttingPort);
            port.startLoopRecive();
//            lightting(YSLighttingContant.NONE);
        }
    }

    private static YSLightting ysLightting;

    public void lightting(byte[] cmd) {
        if (port != null)
            port.writeData(cmd);
    }

    Timer timer = new Timer();

    public void lightting(byte[] cmd, long cost) {
        if (cost < 300) {
            cost = 300;
        }
        lightting(cmd);
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                lightting(YSLighttingContant.NONE);
            }
        }, cost);
    }

    public static YSLightting getInstance() {
        if (ysLightting == null)
            ysLightting = new YSLightting();
        return ysLightting;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void destory() {
        if (port != null)
            YSSerial.getInstance().destroy(port.serialPath);
    }
}
