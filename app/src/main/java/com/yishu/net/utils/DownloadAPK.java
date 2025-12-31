package com.yishu.net.utils;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yishu.server.msgfuc.UpdateMsgFuc;

import java.io.File;

public class DownloadAPK {

    public static void downloadApk(){

    }

    public static final String path="/sdcard/appshopTemp/";
    public static void downloadAPK(String url) {
        String tempFile = path+ "install.apk";
        File dstfile=new File(tempFile);
        UpdateMsgFuc.sendUpdate("开始下载");
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(url)
                .setPath(tempFile)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("CHECK", " pending " + soFarBytes + " " + totalBytes + " " + url);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.e("CHECK", " connected " + soFarBytes + " " + totalBytes);

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("CHECK", " progress " + soFarBytes + " " + totalBytes);
                        int process = (int) ((soFarBytes / (totalBytes * 1.0f)) * 100);
                        UpdateMsgFuc.sendUpdate("下载中"+process);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.e("CHECK", " blockComplete ");

                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        Log.e("CHECK", " retry ");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.e("CHECK", " completed——completed");
                        String cmd = "pm install -r '" + dstfile.getAbsolutePath() + "'";
//                        UpdateMsgFuc.sendUpdate("完成升级");
                        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(cmd, true);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("CHECK", " paused ");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e("CHECK", " error " + url + e.getMessage());
                        UpdateMsgFuc.sendUpdate("升级失败");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.e("CHECK", " warn ");
                    }
                });
        baseDownloadTask.start();
    }
}
