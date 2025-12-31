package com.yishu.net.scp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileInstall {
    @SerializedName("dataList")
    public List<FileClass> dataList;

    public List<FileClass> getDataList() {
        return dataList;
    }

    public void setDataList(List<FileClass> dataList) {
        this.dataList = dataList;
    }

    public FileInstall() {
    }

    public static class FileClass{
        @SerializedName("path")
        public String path;
        @SerializedName("aimPath")
        public String aimPath;
        @SerializedName("type")
        public String type;
        @SerializedName("isInstall")
        public boolean isInstall;
        @SerializedName("name")
        public String name;

        public FileClass(String path, String aimPath, String type, boolean isInstall, String name, boolean isDir) {
            this.path = path;
            this.aimPath = aimPath;
            this.type = type;
            this.isInstall = isInstall;
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getAimPath() {
            return aimPath;
        }

        public void setAimPath(String aimPath) {
            this.aimPath = aimPath;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isInstall() {
            return isInstall;
        }

        public void setInstall(boolean install) {
            isInstall = install;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}


