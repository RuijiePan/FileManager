package com.jiepier.filemanager.task;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.RemoteException;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.task.callback.ISysScanCallBack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 */

public class SysCacheScanTask extends AsyncTask<Void, Void, Void> {

    private ISysScanCallBack mCallBack;
    private int mScanCount;
    private int mTotalCount;
    private ArrayList<JunkInfo> mSysCaches;
    private HashMap<String, String> mAppNames;
    private long mTotalSize = 0L;
    private boolean mIsOverTime = true;

    public SysCacheScanTask(ISysScanCallBack callBack) {
        this.mCallBack = callBack;
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

        PackageManager pm = App.getAppContext().getPackageManager();
        List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);

        IPackageStatsObserver.Stub observer = new PackageStatsObserver();
        mTotalCount = installedPackages.size();
        mSysCaches = new ArrayList<>();
        mAppNames = new HashMap<>();

        for (int i = 0 ; i < mTotalCount ; i++) {
            ApplicationInfo info = installedPackages.get(i);
            mAppNames.put(info.packageName, pm.getApplicationLabel(info).toString());
            getPackageInfo(info.packageName, observer);
        }

        mIsOverTime = false;
        return null;
    }

    private void getPackageInfo(String packageName, IPackageStatsObserver.Stub observer) {

        try {
            PackageManager pm = App.sContext.getPackageManager();
            Method getPackageSizeInfo = pm.getClass()
                    .getMethod("getPackageSizeInfo", String.class , IPackageStatsObserver.class);

            getPackageSizeInfo.invoke(pm, packageName, observer);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private class PackageStatsObserver extends IPackageStatsObserver.Stub {

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            mScanCount++;
            if (succeeded && pStats != null) {
                JunkInfo info = new JunkInfo();
                info.setPackageName(pStats.packageName)
                        .setName(pStats.packageName)
                        .setSize(pStats.cacheSize + pStats.externalCacheSize);

                if (info.getSize() > 0) {
                    mSysCaches.add(info);
                    mTotalSize += info.getSize();
                }
                mCallBack.onProgress(info);
            }

            if (mScanCount == mTotalCount) {
                JunkInfo junkInfo = new JunkInfo();
                junkInfo.setName(App.sContext.getString(R.string.system_cache))
                        .setSize(mTotalSize)
                        .setChildren(mSysCaches)
                        .setVisible(true)
                        .setChild(false);

                Collections.sort(mSysCaches);
                Collections.reverse(mSysCaches);

                ArrayList<JunkInfo> list = new ArrayList<>();
                list.add(junkInfo);
                mCallBack.onFinish(list);
            }
        }
    }
}
