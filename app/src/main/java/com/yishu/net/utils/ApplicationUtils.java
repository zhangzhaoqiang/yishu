package com.yishu.net.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApplicationUtils {

    private static Context context;

    private ApplicationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        ApplicationUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    public static String processCMD(String cmd){
        try {
            byte[] buffer=new byte[1024*4];
            Process process = Runtime.getRuntime().exec(cmd);
            int len = process.getInputStream().read(buffer);
            if(len>0)
                return new String(buffer,0,len);
            else
                return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /*
    public static String getPackageInfo(){
        processCMD("Test");
        String json="[";
        String packageList=processCMD("pm list package -3");
        String[] split = packageList.split("\n");
        for(String packageName:split){
            String name = packageName.replace("package:", "");
            String getVersion="su\n dumpsys package " +name+ " | grep \"version\"";
            String data = processCMD(getVersion);
            Log.e("CHECK",data);
        }
        json+="]";
        return json;
    }
    */
    public static String getPackageInfo() {
        String json="[";
        try {
            PackageManager pm = context.getPackageManager();
            Process process = Runtime.getRuntime().exec("pm list package -3");
            BufferedReader bis = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            boolean isFrist=true;
            while ((line = bis.readLine()) != null) {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(line.replace("package:", ""), PackageManager.GET_GIDS);
                json+=(isFrist?"":",")+"{\"packageName\":\""+packageInfo.packageName+"\",\n"
                        +"\"versionName\":\""+packageInfo.versionName+"\",\n"
                        +"\"versionCode\":"+packageInfo.versionCode+"\n}";
                if(isFrist){
                    isFrist=false;
                }
            }
        } catch (IOException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        json+="]";
        return json;
    }

}