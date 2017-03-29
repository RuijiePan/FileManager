package com.jiepier.filemanager.util;

import android.util.Log;

/**
 * Created by panruijie on 2017/3/24.
 * Email : zquprj@gmail.com
 */

public class Loger {

    private final static int LOG_LEVEL = 0;
    public static boolean DEBUG = false;

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static boolean isLogable(String tag, int level) {
        return Log.isLoggable(tag, level);
    }

    public static boolean isLogable(int level) {
        return DEBUG && level > LOG_LEVEL;
    }

    public static final void v(String tag, String msg) {
        if (isLogable(Log.VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public static final void e(String tag, String msg) {
        if (isLogable(Log.ERROR)) {
            Log.e(tag, msg);
        }
    }

    public static final void i(String tag, String msg) {
        if (isLogable(Log.INFO)) {
            Log.i(tag, msg);
        }
    }

    public static final void w(String tag, String msg) {
        if (isLogable(Log.WARN)) {
            Log.w(tag, msg);
        }
    }
}
