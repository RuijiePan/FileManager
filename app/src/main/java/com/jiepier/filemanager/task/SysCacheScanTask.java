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
import com.jiepier.filemanager.task.callback.IScanCallBack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 */

public class SysCacheScanTask extends AsyncTask<Void, Void, Void> {

    private IScanCallBack mCallBack;
    private int mScanCount;
    private int mTotalCount;
    private ArrayList<JunkInfo> mSysCaches;
    private HashMap<String, String> mAppNames;
    private long mTotalSize = 0L;

    public SysCacheScanTask(IScanCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mCallBack.onBegin();
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
                        .setName(info.getName())
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
