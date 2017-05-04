package com.jiepier.filemanager.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.RemoteException;

import com.jiepier.filemanager.util.FileUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 2017/2/14.
 * Email : zquprj@gmail.com
 * 清理管理类
 */

public class CleanManager {

    private static CleanManager sInstance;
    private Context mContext;

    private CleanManager(Context context) {

        mContext = context.getApplicationContext();
    }

    public static void init(Context context) {

        if (sInstance == null) {
            synchronized (CleanManager.class) {
                if (sInstance == null) {
                    sInstance = new CleanManager(context);
                }
            }
        }
    }

    public static CleanManager getInstance() {

        if (sInstance == null) {
            throw new IllegalStateException("You must be init CleanManager first");
        }
        return sInstance;
    }

    public Observable<Boolean> cleanJunksUsingObservable(List<String> junkList) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {

                subscriber.onNext(cleanJunks(junkList));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public boolean cleanJunks(List<String> junkList) {

        for (int i = 0; i < junkList.size(); i++) {
            try {
                FileUtil.deleteTarget(junkList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public Observable<Boolean> cleanAppsCacheUsingObservable(List<String> packageNameList) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {

                subscriber.onNext(cleanAppsCache(packageNameList));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public boolean cleanAppsCache(List<String> packageNameList) {

        File externalDir = mContext.getExternalCacheDir();
        if (externalDir == null) {
            return true;
        }

        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);
        for (ApplicationInfo info : installedPackages) {
            if (packageNameList.contains(info.packageName)) {
                String externalCacheDir = externalDir.getAbsolutePath()
                        .replace(mContext.getPackageName(), info.packageName);
                File externalCache = new File(externalCacheDir);
                if (externalCache.exists() && externalCache.isDirectory()) {
                    FileUtil.deleteTarget(externalCacheDir);
                }
            }
        }

        final boolean[] isSuccess = {false};
        try {
            Method freeStorageAndNotify = pm.getClass()
                    .getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
            long freeStorageSize = Long.MAX_VALUE;

            freeStorageAndNotify.invoke(pm, freeStorageSize, new IPackageDataObserver.Stub() {

                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                    isSuccess[0] = succeeded;
                }
            });

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return isSuccess[0];
    }
}
