package com.jiepier.filemanager.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.blankj.utilcode.utils.AppUtils;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.ComparatorApp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 * 安装包管理
 */

public class ProcessManager {

    private static ProcessManager sInstance;
    private Context mContext;
    private List<AppProcessInfo> tempList;
    private List<AppProcessInfo> mRunningProcessList;
    private PackageManager mPackageManager;
    private ActivityManager mActivityManager;
    private ActivityManager.MemoryInfo mMemoryInfo;

    private ProcessManager(Context context){
        this.mContext = context;
        mRunningProcessList = new ArrayList<>();
        mPackageManager = mContext.getPackageManager();
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mMemoryInfo = new ActivityManager.MemoryInfo();
    }

    public static void init(Context context){

        if (sInstance == null)
            sInstance = new ProcessManager(context);
    }

    public static ProcessManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init ProcessManager first");
        }
        return sInstance;
    }

    public List<AppProcessInfo> getRunningProcessList(){

        tempList = new ArrayList<>();
        ApplicationInfo appInfo = null;
        AppProcessInfo abAppProcessInfo = null;

        for (ActivityManager.RunningAppProcessInfo info:AndroidProcesses.getRunningAppProcessInfo(mContext)){

            if (!info.processName.equals(AppUtils.getAppPackageName(mContext))) {
                abAppProcessInfo = new AppProcessInfo(info.processName, info.pid, info.uid);

                try {
                    appInfo = mPackageManager.getApplicationInfo(info.processName, 0);
                    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        abAppProcessInfo.setSystem(true);
                    } else {
                        abAppProcessInfo.setSystem(false);
                    }
                    Drawable icon = appInfo.loadIcon(mPackageManager) == null ?
                            mContext.getResources().getDrawable(R.mipmap.ic_launcher)
                            : appInfo.loadIcon(mPackageManager);
                    String name = appInfo.loadLabel(mPackageManager).toString();
                    abAppProcessInfo.setIcon(icon);
                    abAppProcessInfo.setAppName(name);

                } catch (PackageManager.NameNotFoundException e) {

                /*名字没找到，可能是应用的服务*/
                    if (info.processName.contains(":")) {
                        appInfo = getApplicationInfo(info.processName.split(":")[0]);
                        if (appInfo != null) {
                            Drawable icon = appInfo.loadIcon(mPackageManager);
                            abAppProcessInfo.setIcon(icon);
                        } else {
                            abAppProcessInfo.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
                        }
                    }
                    abAppProcessInfo.setSystem(true);
                    abAppProcessInfo.setAppName(info.processName);
                }

                long memsize = mActivityManager.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty() * 1024;
                abAppProcessInfo.setMemory(memsize);

                if (!abAppProcessInfo.isSystem())
                    tempList.add(abAppProcessInfo);
            }

        }

        //APP去重
        ComparatorApp comparator = new ComparatorApp();
        Collections.sort(tempList,comparator);
        int lastUid = 0;
        int index = -1;
        mRunningProcessList.clear();

        for (AppProcessInfo info:tempList){
            if (lastUid == info.getUid()){
                AppProcessInfo nowInfo = tempList.get(index);
                mRunningProcessList.get(index).setMemory(nowInfo.getMemory()+info.getMemory());
            }else {
                index++;
                mRunningProcessList.add(info);
                lastUid = info.getUid();
            }
        }

        return mRunningProcessList;
    }

    public ApplicationInfo getApplicationInfo( String processName) {
        if (processName == null) {
            return null;
        }
        List<ApplicationInfo> appList = mPackageManager
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : appList) {
            if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
    }

    public Observable<List<AppProcessInfo>> getRunningAppListUsingObservable(){

        return Observable.create(new Observable.OnSubscribe<List<AppProcessInfo>>(){

            @Override
            public void call(Subscriber<? super List<AppProcessInfo>> subscriber) {

                subscriber.onNext(getRunningProcessList());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<AppProcessInfo> getRunningAppList(){
        return mRunningProcessList;
    }

    public long killAllRunningApp(){

        long beforeMemory = 0;
        long endMemory = 0;

        mActivityManager.getMemoryInfo(mMemoryInfo);
        beforeMemory = mMemoryInfo.availMem;

        for (AppProcessInfo info:getRunningProcessList()){
            killBackgroundProcesses(info.getProcessName());
        }

        mActivityManager.getMemoryInfo(mMemoryInfo);
        endMemory = mMemoryInfo.availMem;

        return endMemory - beforeMemory;
    }

    public long killRunningApp(String processName){

        long beforeMemory = 0;
        long endMemory = 0;

        mActivityManager.getMemoryInfo(mMemoryInfo);
        beforeMemory = mMemoryInfo.availMem;

        try {
            killBackgroundProcesses(processName);
        }catch (Exception e){
            e.printStackTrace();
        }

        mActivityManager.getMemoryInfo(mMemoryInfo);
        endMemory = mMemoryInfo.availMem;

        return endMemory - beforeMemory;
    }

    public void killBackgroundProcesses(String processName){

        String packageName = null;
        try {
            if (!processName.contains(":")){
                packageName = processName;
            }else {
                packageName = processName.split(":")[0];
            }

            mActivityManager.killBackgroundProcesses(packageName);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Observable<Long>  killAllRunningAppUsingObservable(){

        return Observable.create(new Observable.OnSubscribe<Long>(){

            @Override
            public void call(Subscriber<? super Long> subscriber) {
                subscriber.onNext(killAllRunningApp());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Long> killRunningAppUsingObservable(Set<String> packageNameSet){

        return Observable.create(new Observable.OnSubscribe<Long>(){

            @Override
            public void call(Subscriber<? super Long> subscriber) {

                long memory = 0L;
                for (String string:packageNameSet){
                    memory += killRunningApp(string);
                }

                subscriber.onNext(memory);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Long>  killRunningAppUsingObservable(String packageName){

        return Observable.create(new Observable.OnSubscribe<Long>(){

            @Override
            public void call(Subscriber<? super Long> subscriber) {
                subscriber.onNext(killRunningApp(packageName));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
