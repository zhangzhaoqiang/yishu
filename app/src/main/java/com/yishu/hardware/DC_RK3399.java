package com.yishu.hardware;

public class DC_RK3399 implements BroadInfoFuc{
    public static final String SERIAL_Light="/dev/ttysWK2";
    public static final String SERIAL_AED="/dev/ttysWK0";

    @Override
    public String getBroadName() {
        return "定昌3399";
    }

    @Override
    public String getBroadLightting() {
        return DC_RK3399.SERIAL_Light;
    }

    @Override
    public String getBroadAED() {
        return DC_RK3399.SERIAL_AED;
    }
}
