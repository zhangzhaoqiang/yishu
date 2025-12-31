package com.yishu.hardware;

public class YS_RK3399 implements BroadInfoFuc {
    public static final String SERIAL_Light="/dev/ttys3";
    public static final String SERIAL_AED="/dev/ttys2";

    @Override
    public String getBroadName() {
        return "亿晟3399";
    }

    @Override
    public String getBroadLightting() {
        return YS_RK3399.SERIAL_Light;
    }

    @Override
    public String getBroadAED() {
        return YS_RK3399.SERIAL_AED;
    }
}