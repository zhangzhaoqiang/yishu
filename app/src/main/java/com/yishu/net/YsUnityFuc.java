package com.yishu.net;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;


public class YsUnityFuc {
    public static Context context;
    public static YsAppServiceInterface manager;

    public static void init() {
        try {
            Class<?> clazz = Class.forName("com.unity3d.player.UnityPlayer");
            Field[] fields = clazz.getFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                int modifiers = field.getModifiers();
                boolean flag = (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers));
                if (flag) {
                    String name = field.getName();
                    if (name.equals("currentActivity")) {
                        try {
                            // field.get(null)  表示获取这个静态变量的值
                            context = (Context) field.get(null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        YsAppFuc.getInstance().initFuc(context, new YsAppFuc.YsAppInterface() {
            @Override
            public void onSuccess(YsAppServiceInterface manager) {
                YsUnityFuc.manager = manager;
                initListener();
                UnitySendMessage("init", "success");
            }

            @Override
            public void onError() {
                UnitySendMessage("init", "error");
            }
        });
    }
    private static final String defaultNULL="null";
    private static YsAppServiceListener listener = new YsAppServiceListener.Stub() {
        @Override
        public void onNetChange(String ip) throws RemoteException {
            UnitySendMessage("onNetChange", ip);
        }

        @Override
        public void onNetDisconnect() throws RemoteException {
            UnitySendMessage("onNetDisconnect", defaultNULL);
        }

        @Override
        public void onDownloadSoure(String source) throws RemoteException {
            UnitySendMessage("onDownloadSoure", source);
        }

        @Override
        public void onDownloadComplete() throws RemoteException {
            UnitySendMessage("onDownloadComplete", defaultNULL);
        }

        @Override
        public void onDownloadError(String source) throws RemoteException {
            UnitySendMessage("onDownloadError", source);
        }
    };

    private static void initListener() {
        try {
            if (manager != null)
                manager.registerListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void UnitySendMessage(String method, String parm) {
        try {
            Class<?> clazz = Class.forName("com.unity3d.player.UnityPlayer");
            Method[] ms = clazz.getDeclaredMethods();
            for (int i = 0; i < ms.length; i++) {
                Method m = ms[i];
                if (m.getName().equals("UnitySendMessage")) {
                    Type[] types = m.getGenericParameterTypes();
                    m.invoke(null, "YsAndroid", method, parm);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean installApk(String apkfile) {
        boolean flag = false;
        if (manager != null) {
            try {
                flag = manager.installApk(apkfile);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return flag;
    }

    public static String shellCMD(String cmd) {
        String flag = "";
        if (manager != null) {
            try {
                flag = manager.shellCMD(cmd);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return flag;
    }

    public static String getDeviceName() {
        String deviceName = "";
        if (manager != null) {
            try {
                deviceName = manager.getDeviceName();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return deviceName;
    }

    public static boolean setDevice(String rsa, String deviceName) {
        boolean flag = false;
        if (manager != null) {
            try {
                flag = manager.setDevice(rsa, deviceName);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return flag;
    }

    public static void setBootApplication(String packageName) {
        if (manager != null) {
            try {
                manager.setBootApplication(packageName);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static String getBootApplication() {
        String deviceName = "";
        if (manager != null) {
            try {
                deviceName = manager.getBootApplication();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return deviceName;
    }

    public static String readSetting(String packageName, String key) {
        String str = "";
        if (manager != null) {
            try {
                str = manager.readSetting(packageName, key);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return str;
    }

    public static void wirteSetting(String packageName, String key, String value) {
        if (manager != null) {
            try {
                manager.wirteSetting(packageName, key, value);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static String getIP() {
        String ip = "";
        if (manager != null) {
            try {
                ip = manager.getIP();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return ip;
    }

    public static String getLocal() {
        String local = "";
        if (manager != null) {
            try {
                local = manager.getDeviceUseMode();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return local;
    }

    public static void hideTopBottom() {
        if (manager != null) {
            try {
                manager.hideTopBottom();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void showTopBottom() {
        if (manager != null) {
            try {
                manager.showTopBottom();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void lockTopBottom() {
        if (manager != null) {
            try {
                manager.lockTopBottom();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void unLockTopBottom() {
        if (manager != null) {
            try {
                manager.unLockTopBottom();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static boolean isLockTopBottom() {
        boolean isShow = false;
        if (manager != null) {
            try {
                isShow = manager.isLockTopBottom();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return isShow;
    }

    public static boolean isShowTopBottom() {
        boolean isShow = false;
        if (manager != null) {
            try {
                isShow = manager.isShowTopBottom();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return isShow;
    }

    public static void setDeviceUseMode(String type) {
        if (manager != null) {
            try {
                manager.setDeviceUseMode(type);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static String getDeviceUseMode() {
        String deviceMode = "";
        if (manager != null) {
            try {
                deviceMode = manager.getDeviceUseMode();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return deviceMode;
    }

    public static void downloadSource(String sourceJson) {
        if (manager != null) {
            try {
                manager.downloadSource(sourceJson);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void cancelDownload() {
        if (manager != null) {
            try {
                manager.cancelDownload();
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static String checkSource(String sourceJson) {
        String str = "";
        if (manager != null) {
            try {
                str = manager.checkSource(sourceJson);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
        return str;
    }

    public static void showError(String title, String msg) {
        if (manager != null) {
            try {
                manager.showError(title, msg);
            } catch (RemoteException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void deinit() {
        if (manager != null) {
            try {
                manager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (context != null)
                YsAppFuc.getInstance().deinit(context);
        }

    }
}
