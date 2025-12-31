package com.yishu.net;

import com.yishu.lightting.YSLightting;
import com.yishu.lightting.YSLighttingContant;
import com.yishu.net.app.YsApp;
import com.yishu.server.utils.GsonUtil;

public class YsAppCMD {

    public static void deal(String key, String value) {
        switch (key) {
            case YsApp.CMD_DEVICE_LIGHTTING:
                cmdLight(value);
                break;
        }
    }

    private static void cmdLight(String value) {
        YsLighttingValue ysLighttingValue = GsonUtil.convert(value, YsLighttingValue.class);
        if(ysLighttingValue==null)
            return;
        switch (ysLighttingValue.lightName){
            case YsLighttingValue.NONE:
                light(YSLighttingContant.NONE, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.FLU_RED:
                light(YSLighttingContant.FLU_RED, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.FLU_GREEN:
                light(YSLighttingContant.FLU_GREEN, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.STATIC_RED:
                light(YSLighttingContant.STATIC_RED, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.STATIC_GREEN:
                light(YSLighttingContant.STATIC_GREEN, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.STATIC_BLUE:
                light(YSLighttingContant.STATIC_BLUE, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.STATIC_WHITE:
                light(YSLighttingContant.STATIC_WHITE, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.BREATH_NONE:
                light(YSLighttingContant.BREATH_NONE, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.BREATH_RED:
                light(YSLighttingContant.BREATH_RED, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.BREATH_GREEN:
                light(YSLighttingContant.BREATH_GREEN, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.BREATH_BLUE:
                light(YSLighttingContant.BREATH_BLUE, ysLighttingValue.costTime);
                break;
            case YsLighttingValue.BREATH_WHITE:
                light(YSLighttingContant.BREATH_WHITE, ysLighttingValue.costTime);
                break;
        }
    }
    private static void light(byte[] buffer,int costTime){
        if(costTime==0){
            YSLightting.getInstance().lightting(buffer);
        }else {
            YSLightting.getInstance().lightting(buffer, costTime);
        }
    }
}
