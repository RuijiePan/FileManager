package com.jiepier.filemanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by panruijie on 2017/3/31.
 * Email : zquprj@gmail.com
 */

public class AccessibilityUtil {

    public static void gotoInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.w("AccessibilityUtil", e.toString());
        }
    }
}
