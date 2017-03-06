package com.jiepier.filemanager.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static android.os.Environment.isExternalStorageEmulated;

/**
 * Created by panruijie on 2017/3/6.
 * Email : zquprj@gmail.com
 * 用于获取系统存储相关信息的工具类
 */

public class StorageUtil {

    private static String TAG = "StorageUtil";

    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static class SDCardInfo {
        public long mTotal = 0L;
        public long mFree = 0L;
    }

    /**
     * 获取sd卡总共空间和可用空间
     * <p/>
     * 如果sd卡不可用则返回rom空间大小
     */
    public static SDCardInfo getSDCardInfo(Context context) {
        SDCardInfo sdcard = new SDCardInfo();
        Set<String> paths = getAllExternalPaths(context);

        if (paths != null && !paths.isEmpty()) {
            for (String path : paths) {
                Log.w(TAG, "SD卡路径 -- " + path);
                sdcard.mFree += getPathFreeSize(path);
                sdcard.mTotal += getPathTotalSize(path);
            }
        }

        // 非内置sd卡，加上rom空间大小
        /*设备的外存是否是用内存模拟的，是则返回true
        该方法判断手机是否有内置sd卡*/
        if (!isExternalStorageEmulated() || sdcard.mTotal == 0) {
            final String rom = Environment.getDataDirectory().getPath();
            Log.w(TAG, "ROM路径 -- " + rom);
            sdcard.mFree += getPathFreeSize(rom);
            sdcard.mTotal += getPathTotalSize(rom);
        }
        return sdcard;
    }

    /**
     * 获取指定目录的总可用空间
     *
     * @param path 目录的路径
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static long getPathTotalSize(String path) {
        long size = 0L;
        if (!TextUtils.isEmpty(path)) {
            StatFs sf = null;
            try {
                sf = new StatFs(path);
            } catch (Exception e) {
                Log.w(TAG, "获取总空间出错：" + Log.getStackTraceString(e));
                size = 0;
            }

            if (sf != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    size = sf.getBlockSizeLong() * sf.getBlockCountLong();
                } else {
                    // (jira ZB-43)计算数值如果是int类型的话，计算结果也会先当成int类型处理，可能造成结果溢出变为负数
                    size = (long) sf.getBlockSize() * sf.getBlockCount();
                }
            }
        }

        return size;
    }

    /**
     * 获取SD卡剩余空间(单位byte)
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static long getSDCardFreeSize() {
        if (!isSDCardAvailable()) {
            return 1;
        }

        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // return 空间的数据块大小 × 空闲的数据块的数量
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return sf.getBlockSizeLong() * sf.getAvailableBlocksLong();
        } else {
            return (long) sf.getBlockSize() * sf.getAvailableBlocks();
        }
    }

    /**
     * 获得手机sdcard的总空间大小(单位KB)
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Deprecated
    public static long getSDCardTotalSize() {

        if (!isSDCardAvailable()) {
            return 1;
        }

        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return sf.getBlockSizeLong() * sf.getBlockCountLong();
        } else {
            // (jira ZB-43)计算数值如果是int类型的话，计算结果也会先当成int类型处理，可能造成结果溢出变为负数
            return (long) sf.getBlockSize() * sf.getBlockCount();
        }
    }

    /**
     * 获取指定目录的总可用空间
     *
     * @param path 目录的路径
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static long getPathFreeSize(String path) {
        long size = 0L;
        if (!TextUtils.isEmpty(path)) {
            StatFs sf = null;
            try {
                sf = new StatFs(path);
            } catch (Exception e) {
                Log.w(TAG, "获取空闲空间出错：" + Log.getStackTraceString(e));
                size = 0;
            }
            if (sf != null) {
                // return 空间的数据块大小 × 空闲的数据块的数量
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    size = sf.getBlockSizeLong() * sf.getAvailableBlocksLong();
                } else {
                    size = (long) sf.getBlockSize() * sf.getAvailableBlocks();
                }
            }
        }
        return size;
    }

    private static Set<String> getAllExternalPaths(Context context) {
        final Set<String> sdCardDirs = new HashSet<>();
        BufferedReader bufferedReader = null;
        try {
            Object storageObject = context.getApplicationContext()
                    .getSystemService(Context.STORAGE_SERVICE);
            Class<?> smCLass = Class.forName("android.os.storage.StorageManager");
            Class[] arrayOfClass = new Class[0];
            Method localMethod = smCLass.getMethod("getVolumePaths", arrayOfClass);
            Object[] arrayOfObject = new Object[0];
            String[] returnFilePath = (String[]) localMethod.invoke(storageObject, arrayOfObject);
            for (String path : returnFilePath) {
                File mountRoot = new File(path);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite() || mountRoot.getTotalSpace() == 0) {
                    continue;
                }
                sdCardDirs.add(path);
            }

            // 当前系统所安装的文件系统信息（包括手动安装的）
            File file = new File("proc/mounts");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            bufferedReader = new BufferedReader(isr);
            String result = "";
            do {
                try {
                    result = bufferedReader.readLine();
                    if (result != null && result.contains("uid=1000")
                            && result.contains("gid=1015")
                            && !result.contains("aesc")) {
                        if (result.contains(" ")) {
                            String[] paths = result.split(" ");
                            if (paths.length >= 4) {
                                result = paths[1];
                                sdCardDirs.add(result);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (TextUtils.isEmpty(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        filterSdCardPath(sdCardDirs);
        return sdCardDirs;
    }

    /**
     * 过滤三星盖世一的某条映射路径，避免SD卡显示重复
     */
    private static void filterSdCardPath(Set<String> sdCardDirs) {
        if (sdCardDirs == null || sdCardDirs.isEmpty()) {
            return;
        }
        if (!"GT-I9000".equals(android.os.Build.MODEL.trim())) {
            return;
        }
        sdCardDirs.remove("/mnt/sdcard/external_sd");
    }

    public static String getStorageInfo(Context context) {
        SDCardInfo sdCardInfo = getSDCardInfo(context);
        return FormatUtil.formatFileSize(sdCardInfo.mFree).toString() + "/" +
                FormatUtil.formatFileSize(sdCardInfo.mTotal).toString();
    }
}
