package com.jiepier.filemanager.manager;

import android.app.ActivityManager;
import android.content.Context;

import com.blankj.utilcode.utils.AppUtils;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.sqlite.DataManager;
import com.jiepier.filemanager.util.SortUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/29.
 * Email : zquprj@gmail.com
 * 文件管理类 : 普通get方法线程阻塞、rx方法线程不阻塞
 */

public class CategoryManager {

    private String TAG = getClass().getSimpleName();
    private static CategoryManager sInstance;
    private ApkManager mApkManager;
    private DocManager mDocManager;
    private ZipManager mZipManager;
    private ProcessManager mProcessManager;
    private DataManager mDataManager;
    private SortUtil.SortMethod mSortMethod = SortUtil.SortMethod.DATE;

    private CategoryManager(Context context){

        ApkManager.init(context);
        DocManager.init(context);
        ZipManager.init(context);
        ProcessManager.init(context);
        DataManager.init(context, AppUtils.getAppVersionCode(context));

        mApkManager = ApkManager.getInstance();
        mDocManager = DocManager.getInstance();
        mZipManager = ZipManager.getInstance();
        mProcessManager = ProcessManager.getInstance();
        mDataManager = DataManager.getInstance();
    }

    public static void init(Context context){

        if (sInstance == null){
            sInstance = new CategoryManager(context);
        }
    }

    public static CategoryManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init CategoryManager first");
        }
        return sInstance;
    }

    public SortUtil.SortMethod getSortMethod() {
        return this.mSortMethod;
    }

    public CategoryManager setSortMethod(SortUtil.SortMethod mSortMethod) {
        this.mSortMethod = mSortMethod;
        return this;
    }

    //从数据库找
    public Observable<ArrayList<String>> getApkList(){
        return mDataManager.selectUsingObservable(DataManager.APK);
    }

    //从ContentProvicer找
    public Observable<List<String>> getApkListUsingObservable(){
        return mApkManager.getApkListUsingObservable(mSortMethod);
    }

    public Observable<ArrayList<String>> getDocList(){
        return mDataManager.selectUsingObservable(DataManager.DOC);
    }

    public Observable<List<String>> getDocListUsingObservable(){
        return mDocManager.getDocListUsingObservable(mSortMethod);
    }

    public Observable<ArrayList<String>> getZipList(){
        return mDataManager.selectUsingObservable(DataManager.ZIP);
    }

    public Observable<List<String>> getZipListUsingObservable(){
        return mZipManager.getZipListUsingObservable(mSortMethod);
    }

    public Observable<List<AppProcessInfo>> getRunningAppList(){
        return mProcessManager.getRunningAppListUsingObservable();
    }

    public Observable<Long> killAllRunningAppUsingObservable(){
        return mProcessManager.killAllRunningAppUsingObservable();
    }

    public Observable<Long> killRunningAppUsingObservable(Set<String> packageNameSet){
        return mProcessManager.killRunningAppUsingObservable(packageNameSet);
    }

    public Observable<Long> killRunningAppUsingObservable(String packageName){
        return mProcessManager.killRunningAppUsingObservable(packageName);
    }

    public void update(){

        //此处不应该在主线程执行数据库操作
        getDocListUsingObservable()
                .observeOn(Schedulers.io())
                .subscribe(list -> {
                    mDataManager.updateSQL(DataManager.DOC,list);
                });

        getZipListUsingObservable()
                .observeOn(Schedulers.io())
                .subscribe(list -> {
                    mDataManager.updateSQL(DataManager.ZIP,list);
                });

        getApkListUsingObservable()
                .observeOn(Schedulers.io())
                .subscribe(list -> {
                    mDataManager.updateSQL(DataManager.APK,list);
                });

    }

}


