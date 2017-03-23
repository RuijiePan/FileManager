package com.jiepier.filemanager.util;

/**
 * Created by JiePier on 16/12/6.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jiepier.filemanager.base.App;

//TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbSharedUtil.java
 * 描述：保存到 SharedPreferences 的数据.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-10-09 下午11:52:13
 */
public class SharedUtil {

    private static final String SHARED_PATH = "app_share";
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(App.sContext);
        Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(App.sContext);
        return sharedPreferences.getInt(key, 0);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(App.sContext);
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(App.sContext);
        return sharedPreferences.getString(key, null);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(App.sContext);
        Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(App.sContext);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
    }

    public static void remove(String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(App.sContext);
        Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
    }

}
