package com.yishu.net.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtil {
    public static String urlEncodeURL(String str) {
        try {
            String result = URLEncoder.encode(str,"UTF-8");
            result = result.replaceAll("%3A",":").replaceAll("%2F","/").replaceAll("\\+","%20");//+实际上是 空格 url encode而来
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

//    只对url地址中的中文进行编码

    public static String encode(String url) {
        String[] split = url.split("/");
        String out="";
        for(int i=0;i<url.length();i++){
            char c = url.charAt(i);
            if(c=='/'||c==':'){
                out+=c;
            }else{
                String ins=new String(new char[]{c});
                out+=getURLEncoderString(ins);
            }
        }
//        for(int i=0;i<split.length;i++){
//            out+=getURLEncoderString(split[i])+"/";
//        }
//        try {
//            Matcher matcher = Pattern.compile("[\u4e00\u9fa5]").matcher(url);
//            while (matcher.find()) {
//                String tmp = matcher.group();
//                url = url.replaceAll(tmp, java.net.URLEncoder.encode(tmp,"UTF-8"));
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return out;
    }
    private final static String ENCODE = "UTF-8"; //自己的编码格式

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getURLDecoderString(String str) {

        String result = "";

        if (null == str) {

            return "";

        }

        try {

            result = java.net.URLDecoder.decode(str, ENCODE);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return result;

    }

}
