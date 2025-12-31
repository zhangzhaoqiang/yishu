package com.yishu.net.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

public class ContextUtils {
    public static void startActivityClass(Context context, String packageName, String activityName){
        Intent activityIntent = new Intent();
        activityIntent.setClassName(packageName, packageName+activityName);
        context.startActivity(activityIntent);
    }
    public static void startActivity(Context context,String packageName){
        Intent activityIntent = new Intent();
        activityIntent.setClassName(packageName, getLaunchActivityForPackage(context,packageName));
        context.startActivity(activityIntent);
    }
    private static String getLaunchActivityForPackage(Context context,String packageName){
        PackageManager pm = context.getPackageManager();
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(Intent.CATEGORY_INFO);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, 0);
        // Otherwise, try to find a main launcher activity.
        if (ris == null || ris.size() <= 0) {
            // reuse the intent instance
            intentToResolve.removeCategory(Intent.CATEGORY_INFO);
            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = pm.queryIntentActivities(intentToResolve, 0);
        }
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        return ris.get(0).activityInfo.name;
    }
}
