package com.yishu.net.app;

import java.io.Serializable;
import java.util.List;

public class Source implements Serializable {
    public String fileName;//文件名称
    public String url;// 远程全路径
    public String dst;//目标地址
    public String fileSize;//文件大小 文件大小不一致则开始下载\
    public String localFileMd5;
    public boolean isReload;// 文件大小一致 重新下载这个文件


    public Source(String fileName, String url, String dst, String fileSize, boolean isReload) {
        this.fileName = fileName;
        this.url = url;
        this.dst = dst;
        this.fileSize = fileSize;
        this.isReload = isReload;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalFileMd5() {
        return localFileMd5;
    }

    public void setLocalFileMd5(String localFileMd5) {
        this.localFileMd5 = localFileMd5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isReload() {
        return isReload;
    }

    public void setReload(boolean reload) {
        isReload = reload;
    }
}
