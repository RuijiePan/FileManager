package com.jiepier.filemanager.task;

import android.os.AsyncTask;
import android.os.Environment;

import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.task.callback.IScanCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 * 扫描类，默认扫描层级为10
 */

public class OverScanTask extends AsyncTask<Void, Void, Void> {

    private IScanCallBack mCallBack;
    private final int SCAN_LEVEL = 10;
    private static final int TEN_MB = 10 * 1024 * 1024;
    private boolean mIsOverTime = true;
    private JunkInfo mApkInfo;
    private JunkInfo mLogInfo;
    private JunkInfo mTempInfo;
    private JunkInfo mBigFileInfo;
    private ArrayList<JunkInfo> mList;
    private ArrayList<JunkInfo> mApkList;
    private ArrayList<JunkInfo> mLogList;
    private ArrayList<JunkInfo> mTempList;
    private ArrayList<JunkInfo> mBigFileList;

    public OverScanTask(IScanCallBack scanCallBack) {
        mCallBack = scanCallBack;
        mApkInfo = new JunkInfo();
        mLogInfo = new JunkInfo();
        mTempInfo = new JunkInfo();
        mBigFileInfo = new JunkInfo();

        mList = new ArrayList<>();
        mApkList = new ArrayList<>();
        mLogList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mBigFileList = new ArrayList<>();
    }

    private void travelPath(File root , int level) {

        if (root == null || !root.exists() || level > SCAN_LEVEL) {
            return;
        }

        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    JunkInfo info = null;

                    if (name.endsWith(".apk")) {
                        info = getJunkInfo(file);
                        mApkInfo.getChildren().add(info);
                        mApkInfo.setSize(mApkInfo.getSize() + info.getSize());
                    } else if (name.endsWith(".log")) {
                        info = getJunkInfo(file);
                        mLogInfo.getChildren().add(info);
                        mLogInfo.setSize(mLogInfo.getSize() + info.getSize());
                    } else if (name.endsWith(".tmp") || name.endsWith(".temp")) {
                        info = getJunkInfo(file);
                        mTempInfo.getChildren().add(info);
                        mTempInfo.setSize(mTempInfo.getSize() + info.getSize());
                    } else if (file.length() > TEN_MB) {
                        info = getJunkInfo(file);
                        mBigFileInfo.getChildren().add(info);
                        mBigFileInfo.setSize(mBigFileInfo.getSize() + info.getSize());
                    }

                    if (info != null) {
                        mCallBack.onProgress(info);
                    }
                } else {
                    if (level < SCAN_LEVEL) {
                        travelPath(file , level + 1);
                    }
                }
            }
        }
    }

    private JunkInfo getJunkInfo(File file) {

        JunkInfo junkInfo = new JunkInfo();
        junkInfo.setSize(file.length())
                .setName(file.getName())
                .setPath(file.getAbsolutePath())
                .setChild(false)
                .setVisible(true);
        return junkInfo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Observable.timer(30 * 1000, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    if (mIsOverTime) {
                        mCallBack.onOverTime();
                    }
                });
    }

    @Override
    protected Void doInBackground(Void... params) {
        mCallBack.onBegin();

        if (isCancelled()) {
            mCallBack.onCancel();
            return null;
        }

        File externalDir = Environment.getExternalStorageDirectory();
        if (externalDir != null) {
            travelPath(externalDir, 0);
        }

        if (mApkInfo.getSize() > 0L) {
            Collections.sort(mApkInfo.getChildren());
            Collections.reverse(mApkInfo.getChildren());
            mList.add(mApkInfo);
            mApkList.add(mApkInfo);
        }

        if (mLogInfo.getSize() > 0L) {
            Collections.sort(mLogInfo.getChildren());
            Collections.reverse(mLogInfo.getChildren());
            mList.add(mLogInfo);
            mLogList.add(mLogInfo);
        }

        if (mTempInfo.getSize() > 0L) {
            Collections.sort(mTempInfo.getChildren());
            Collections.reverse(mTempInfo.getChildren());
            mList.add(mTempInfo);
            mTempList.add(mTempInfo);
        }

        if (mBigFileInfo.getSize() > 0L) {
            Collections.sort(mBigFileInfo.getChildren());
            Collections.reverse(mBigFileInfo.getChildren());
            mList.add(mBigFileInfo);
            mBigFileList.add(mBigFileInfo);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mCallBack.onFinish(mApkList, mLogList, mTempList, mBigFileList);
        mIsOverTime = false;
        super.onPostExecute(aVoid);
    }
}
