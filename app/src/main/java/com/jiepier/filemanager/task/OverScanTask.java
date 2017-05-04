package com.jiepier.filemanager.task;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.entity.ApkInfo;
import com.jiepier.filemanager.preview.MimeTypes;
import com.jiepier.filemanager.task.callback.IScanCallBack;
import com.jiepier.filemanager.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 * 扫描类，默认扫描层级为5
 */

public class OverScanTask extends AsyncTask<Void, Void, Void> {

    private IScanCallBack mCallBack;
    private final int SCAN_LEVEL = 5;
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

        if (root == null || !root.exists() || level > SCAN_LEVEL || isCancelled()) {
            return;
        }

        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isCancelled()) {
                    return;
                }
                if (file.isFile()) {
                    JunkInfo info = null;

                    if (MimeTypes.isApk(file)) {
                        info = getJunkInfo(file);
                        mApkInfo.getChildren().add(info);
                        mApkInfo.setSize(mApkInfo.getSize() + info.getSize());

                        //如果是已经安装的apk并且版本号>=当前扫描到的apk，则勾选
                        ApkInfo apkInfo = FileUtil.getApkInfo(file.getAbsolutePath());
                        if (apkInfo != null && !TextUtils.isEmpty(apkInfo.getPackageName())) {
                            mApkInfo.isCheck(compareToInstall(apkInfo.getVersionCode(), apkInfo.getPackageName()));
                        }
                    } else if (MimeTypes.isLog(file)) {
                        info = getJunkInfo(file);
                        mLogInfo.getChildren().add(info);
                        mLogInfo.setSize(mLogInfo.getSize() + info.getSize());
                    } else if (MimeTypes.isTempFile(file)) {
                        info = getJunkInfo(file);
                        mTempInfo.getChildren().add(info);
                        mTempInfo.setSize(mTempInfo.getSize() + info.getSize());
                    } else if (MimeTypes.isBigFile(file)) {
                        info = getJunkInfo(file);
                        info.isCheck(false);
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
                .setVisible(true)
                .isCheck(true);
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

    private boolean compareToInstall(int versionCode, String packageName) {
        PackageInfo info;
        try {
            info = App.sContext.getPackageManager().getPackageInfo(packageName, 0);
            return versionCode <= info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
