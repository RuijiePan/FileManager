package com.jiepier.filemanager.bean;

import java.util.ArrayList;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 */

public class JunkGroup {

    private long totalSize;
    private ArrayList<JunkProcessInfo> mApkList;
    private ArrayList<JunkProcessInfo> mProcessList;
    private ArrayList<JunkProcessInfo> mSysCacheList;
    private ArrayList<JunkProcessInfo> mTempList;
    private ArrayList<JunkProcessInfo> mLogList;
    private ArrayList<JunkProcessInfo> mBigFileList;

    public JunkGroup() {

    }

    public JunkGroup(ArrayList<JunkProcessInfo> mApkList, ArrayList<JunkProcessInfo> mProcessList, ArrayList<JunkProcessInfo> mSysCacheList, ArrayList<JunkProcessInfo> mTempList, ArrayList<JunkProcessInfo> mLogList, ArrayList<JunkProcessInfo> mBigFileList) {
        this.mApkList = mApkList;
        this.mProcessList = mProcessList;
        this.mSysCacheList = mSysCacheList;
        this.mTempList = mTempList;
        this.mLogList = mLogList;
        this.mBigFileList = mBigFileList;
    }

    public ArrayList<JunkProcessInfo> getApkList() {
        return mApkList;
    }

    public JunkGroup setApkList(ArrayList<JunkProcessInfo> mApkList) {
        this.mApkList = mApkList;
        return this;
    }

    public ArrayList<JunkProcessInfo> getProcessList() {
        return mProcessList;
    }

    public JunkGroup setProcessList(ArrayList<JunkProcessInfo> mProcessList) {
        this.mProcessList = mProcessList;
        return this;
    }

    public ArrayList<JunkProcessInfo> getSysCacheList() {
        return mSysCacheList;
    }

    public JunkGroup setSysCacheList(ArrayList<JunkProcessInfo> mSysCacheList) {
        this.mSysCacheList = mSysCacheList;
        return this;
    }

    public ArrayList<JunkProcessInfo> getTempList() {
        return mTempList;
    }

    public JunkGroup setTempList(ArrayList<JunkProcessInfo> mTempList) {
        this.mTempList = mTempList;
        return this;
    }

    public ArrayList<JunkProcessInfo> getLogList() {
        return mLogList;
    }

    public JunkGroup setLogList(ArrayList<JunkProcessInfo> mLogList) {
        this.mLogList = mLogList;
        return this;
    }

    public ArrayList<JunkProcessInfo> getBigFileList() {
        return mBigFileList;
    }

    public JunkGroup setBigFileList(ArrayList<JunkProcessInfo> mBigFileList) {
        this.mBigFileList = mBigFileList;
        return this;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public ArrayList<JunkProcessInfo> getJunkList(int index) {
        switch (index) {
            case JunkType.PROCESS:
                return mProcessList;
            case JunkType.CACHE:
                return mSysCacheList;
            case JunkType.APK:
                return mApkList;
            case JunkType.LOG:
                return mLogList;
            case JunkType.TEMP:
                return mTempList;
            case JunkType.BIG_FILE:
                return mBigFileList;
        }
        return null;
    }
}
