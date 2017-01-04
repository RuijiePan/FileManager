package com.jiepier.filemanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.jiepier.filemanager.R;
import com.stericson.RootTools.RootTools;

public final class Settings {

    private Settings() {}

    private static SharedPreferences mPrefs;

    private static int mTheme;

    public static void updatePreferences(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        mTheme = Integer.parseInt(mPrefs.getString("preference_theme", Integer.toString(R.style.AppThemeDark)));

        rootAccess();
    }

    public static boolean showThumbnail() {
        return mPrefs.getBoolean("showpreview", true);
    }

    public static boolean showHiddenFiles() {
        return mPrefs.getBoolean("displayhiddenfiles", true);
    }

    public static boolean rootAccess() {
        return mPrefs.getBoolean("enablerootaccess", false) && RootTools.isAccessGiven();
    }

    public static boolean reverseListView() {
        return mPrefs.getBoolean("reverseList", false);
    }

    public static String getDefaultDir() {
        return mPrefs.getString("defaultdir", Environment.getExternalStorageDirectory().getPath());
    }

    public static void setDefaultDir(String path) {
        mPrefs.edit().putString("defaultdir", path).apply();
    }

    public static int getListAppearance() {
        return Integer.parseInt(mPrefs.getString("viewmode", "1"));
    }

    public static int getSortType() {
        return Integer.parseInt(mPrefs.getString("sort", "1"));
    }

}
