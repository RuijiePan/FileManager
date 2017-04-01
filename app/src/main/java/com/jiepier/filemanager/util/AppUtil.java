package com.jiepier.filemanager.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.jiepier.filemanager.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 2017/3/28.
 * Email : zquprj@gmail.com
 */

public class AppUtil {

    //获取已经安装的应用
    public static List<PackageInfo> getInstalledPackages(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<PackageInfo> pakList = new ArrayList<>();

        try {
            pakList = packageManager.getInstalledPackages(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pakList;
    }

    public static Observable<List<AppInfo>> getInstalledAppInfoUsingObservable(Context context, boolean filterSystem) {
        return Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {

            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                subscriber.onNext(getInstalledApplicationInfo(context, filterSystem));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取已经安装的应用
     *
     * @param context
     * @param filterSystem 是否过滤是系统应用
     * @return
     */
    public static List<AppInfo> getInstalledApplicationInfo(Context context, boolean filterSystem) {
        List<PackageInfo> tempList = getInstalledPackages(context);
        List<AppInfo> pakList = new ArrayList<>();

        for (PackageInfo info : tempList) {
            AppInfo appInfo = new AppInfo();
            ApplicationInfo applicationInfo = info.applicationInfo;

            if (filterSystem) {
                if (!isSystemApp(applicationInfo)) {
                    appInfo.setPackageInfo(info)
                            .setPackageName(info.packageName)
                            .setName(getApplicationName(context, applicationInfo))
                            .setDrawable(getIconByPkgname(context, info.packageName))
                            .isSystem(false)
                            .setSize(getAppSize(applicationInfo))
                            .setInstallTime(getInstallTime(applicationInfo));
                    pakList.add(appInfo);
                }
            } else {
                appInfo.setPackageInfo(info)
                        .setPackageName(info.packageName)
                        .setName(getApplicationName(context, applicationInfo))
                        .setDrawable(getIconByPkgname(context, info.packageName))
                        .isSystem(isSystemApp(applicationInfo))
                        .setSize(getAppSize(applicationInfo))
                        .setInstallTime(getInstallTime(applicationInfo));
                pakList.add(appInfo);
            }
        }

        //排序
        Collections.sort(pakList);
        return pakList;
    }

    /**
     * 获取应用名字
     *
     * @param context
     * @param info
     * @return
     */
    public static String getApplicationName(Context context, ApplicationInfo info) {
        PackageManager pkgManager = context.getApplicationContext().getPackageManager();
        return info.loadLabel(pkgManager).toString();
    }

    /**
     * 获取应用ICON图标
     */
    public static Drawable getIconByPkgname(Context context, String pkgName) {
        if (pkgName != null) {
            PackageManager pkgManager = context.getApplicationContext().getPackageManager();
            try {
                PackageInfo pkgInfo = pkgManager.getPackageInfo(pkgName, 0);
                return pkgInfo.applicationInfo.loadIcon(pkgManager);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断应用是否是系统应用
     */
    public static boolean isSystemApp(ApplicationInfo info) {
        boolean isSystemApp = false;
        if (info != null) {
            isSystemApp = (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                    || (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
        }
        return isSystemApp;
    }

    /**
     * 获取安装时间
     */
    public static long getInstallTime(ApplicationInfo info) {
        String sourceDir;
        try {
            sourceDir = info.sourceDir;
            File file = new File(sourceDir);
            return file.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用大小
     */
    public static long getAppSize(ApplicationInfo info) {
        String publicSourceDir;
        try {
            publicSourceDir = info.publicSourceDir;
            File file = new File(publicSourceDir);
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
