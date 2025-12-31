package com.yishu.net;

import android.app.Application;
import android.util.Log;

import com.blankj.utilcode.util.ShellUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.yishu.bluetooth.BltManager;

import com.yishu.net.utils.ApplicationUtils;
import com.yishu.net.utils.NoEtagFileDownloadUrlConnection;

import java.io.File;
import java.io.IOException;

public class LocalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(100000*15000) // set connection timeout.
                        .readTimeout(100000*15000) // set read timeout.
                ){
                    @Override
                    public FileDownloadConnection create(String originUrl) throws IOException {
                        if(originUrl.indexOf(".png")!=0||originUrl.indexOf(".jpg")!=0){
                            return new NoEtagFileDownloadUrlConnection(originUrl);
                        }else {
                            return super.create(originUrl);
                        }
                    }
                })
                .commit();
        ApplicationUtils.init(getApplicationContext());
//        ShellUtils.execCmd("find /sdcard/ -name \"*.temp\" -exec rm -rf {} \\;",true);
        ShellUtils.execCmd("find /sdcard/ -name \"*.apk\" -exec rm -rf {} \\;",true);
        ShellUtils.execCmd("find /sdcard/ -name \"Thumbs.db\" -exec rm -rf {} \\;",true);
        ShellUtils.execCmd("chmod 777 /dev/video0",true);
//        BltManager.getInstance().initBltManager(this);
    }


}
