package com.yishu.net;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;

public class NetStateChangeReceiver extends BroadcastReceiver {

    private static class InstanceHolder {
        private static final NetStateChangeReceiver INSTANCE = new NetStateChangeReceiver();
    }


    @Override
    public void onReceive(Context context, Intent intent) {

//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//            NetworkType networkType = NetworkUtil.getNetworkType(context);
//            Log.i("NetStateChangeReceiver", networkType.toString());
//            notifyObservers(networkType);
//        }
    }


}

