package com.yishu.hardware;

import android.util.Log;

import com.yishu.serial.YSSerial;

import java.util.List;

public class YSBroadSetting implements BroadInfoFuc{
    private static final String TAG=YSBroadSetting.class.getName();
    private static BroadInfoFuc rk3399;

    private static int i=0;
    public static boolean checkBroadAED( List<String> supportSerialList) {
        for(String s:supportSerialList){
           if(s.equals(rk3399.getBroadAED())){
               Log.e(TAG,s+" 支持AED");
               return true;
           }
//           if( s.equals(dc_rk3399.getBroadLightting())){
//                Log.e(TAG,s+" 支持灯光");
//            }
        }
        return false;
    }

    public static boolean checkBroadLightting( List<String> supportSerialList) {

        if(supportSerialList.get(0).equals("/dev/ttyS0")){
            rk3399=new YS_RK3399();
            Log.e("supportSerialList","亿晟板子");
        }
        if(supportSerialList.get(0).equals("/dev/ttysWK0")){
            rk3399=new DC_RK3399();
            Log.e("supportSerialList","定昌板子");
        }
        for(String s:supportSerialList){

           if( s.equals(rk3399.getBroadLightting())){
                Log.e(TAG,s+" 支持灯光");
               return true;
            }
        }
        return false;
    }
    private YSBroadSetting(){

    }
    private static YSBroadSetting ysBroadSetting;
    public static YSBroadSetting getInstance() {
        if(ysBroadSetting==null)
            ysBroadSetting=new YSBroadSetting();
        return ysBroadSetting;
    }
    @Override
    public String getBroadName() {
        return rk3399.getBroadName();
    }

    @Override
    public String getBroadLightting() {
        return rk3399.getBroadLightting();
    }

    @Override
    public String getBroadAED() {
        return rk3399.getBroadAED();
    }
}
