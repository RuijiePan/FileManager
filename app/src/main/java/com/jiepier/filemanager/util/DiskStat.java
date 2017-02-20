package com.jiepier.filemanager.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by mazhuang on 2016/12/8.
 * ref: https://www.liaohuqiu.net/cn/posts/storage-in-android/
 * http://chroya.iteye.com/blog/760821
 */

public class DiskStat {

    private long mExternalBlockSize;
    private long mExternalBlockCount;
    private long mExternalAvailableBlocks;

    private long mInternalBlockSize;
    private long mInternalBlockCount;
    private long mInternalAvailableBlocks;

    public DiskStat() {
        calculateInternalSpace();
        calculateExternalSpace();
    }

    public long getTotalSpace() {
        return mInternalBlockSize * mInternalBlockCount
                + mExternalBlockSize * mExternalBlockCount;
    }

    public long getUsedSpace() {
        return mInternalBlockSize * (mInternalBlockCount - mInternalAvailableBlocks)
                + mExternalBlockSize * (mExternalBlockCount - mExternalAvailableBlocks);
    }

    public long getUsableSpace() {
        return mInternalBlockSize * mInternalAvailableBlocks
                + mExternalBlockSize * mExternalAvailableBlocks;
    }

    private void calculateExternalSpace() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mExternalBlockSize = sf.getBlockSizeLong();
                mExternalBlockCount = sf.getBlockCountLong();
                mExternalAvailableBlocks = sf.getAvailableBlocksLong();
            } else {
                mExternalBlockSize = sf.getBlockSize();
                mExternalBlockCount = sf.getBlockCount();
                mExternalAvailableBlocks = sf.getAvailableBlocks();
            }
        }
    }

    private void calculateInternalSpace() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mInternalBlockSize = sf.getBlockSizeLong();
            mInternalBlockCount = sf.getBlockCountLong();
            mInternalAvailableBlocks = sf.getAvailableBlocksLong();
        } else {
            mInternalBlockSize = sf.getBlockSize();
            mInternalBlockCount = sf.getBlockCount();
            mInternalAvailableBlocks = sf.getAvailableBlocks();
        }
    }
}
