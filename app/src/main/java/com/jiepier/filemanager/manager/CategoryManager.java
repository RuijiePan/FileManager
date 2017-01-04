package com.jiepier.filemanager.manager;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.utils.AppUtils;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.sqlite.DataManager;
import com.jiepier.filemanager.util.SortUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

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
    private DataManager mDataManager;
    private SortUtil.SortMethod mSortMethod = SortUtil.SortMethod.DATE;

    private CategoryManager(Context context){

        ApkManager.init(context);
        DocManager.init(context);
        ZipManager.init(context);
        DataManager.init(context, AppUtils.getAppVersionCode(context));

        mApkManager = ApkManager.getInstance();
        mDocManager = DocManager.getInstance();
        mZipManager = ZipManager.getInstance();
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

    public Observable<ArrayList<String>> getApkList(){
        return mDataManager.select(DataManager.APK);
    }

    public Observable<List<String>> getApkListUsingObservable(){
        return mApkManager.getApkListUsingObservable(mSortMethod);
    }

    public Observable<ArrayList<String>> getDocList(){
        return mDataManager.select(DataManager.DOC);
    }

    public Observable<List<String>> getDocListUsingObservable(){
        return mDocManager.getDocListUsingObservable(mSortMethod);
    }

    public Observable<ArrayList<String>> getZipList(){
        return mDataManager.select(DataManager.ZIP);
    }

    public Observable<List<String>> getZipListUsingObservable(){
        return mZipManager.getZipListUsingObservable(mSortMethod);
    }

    public void update(){

        getDocListUsingObservable()
                .subscribe(list -> {
                    mDataManager.updateSQL(DataManager.DOC,list);
                });

        getZipListUsingObservable()
                .subscribe(list -> {
                    mDataManager.updateSQL(DataManager.ZIP,list);
                });

        getApkListUsingObservable()
                .subscribe(list -> {
                    mDataManager.updateSQL(DataManager.APK,list);
                });

    }

}


