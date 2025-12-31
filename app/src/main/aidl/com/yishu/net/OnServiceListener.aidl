// OnServiceListener.aidl
package com.yishu.net;

// Declare any non-default types here with import statements

interface OnServiceListener {
    void OnConnect();
    void OnDisConnect();
    void OnRecived(String json);
}