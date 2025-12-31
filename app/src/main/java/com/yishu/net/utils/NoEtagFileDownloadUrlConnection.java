package com.yishu.net.utils;

import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

import java.io.IOException;
import java.net.URL;

public class NoEtagFileDownloadUrlConnection extends FileDownloadUrlConnection {

    public NoEtagFileDownloadUrlConnection(String originUrl, Configuration configuration) throws IOException {
        super(originUrl, configuration);
    }

    public NoEtagFileDownloadUrlConnection(URL url, Configuration configuration) throws IOException {
        super(url, configuration);
    }

    public NoEtagFileDownloadUrlConnection(String originUrl) throws IOException {
        super(originUrl);
    }

    @Override
    public void addHeader(String name, String value) {
        if ("If-Match".equals(name)) {
            return;
        }
        super.addHeader(name, value);
    }
}

