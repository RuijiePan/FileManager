package com.jiepier.filemanager.manager;

import android.content.Context;

import com.jiepier.filemanager.util.SortUtil;

import java.util.List;

import rx.Observable;

/**
 * Created by panruijie on 16/12/29.
 * Email : zquprj@gmail.com
 * 文件管理类 : 普通get方法线程阻塞、rx方法线程不阻塞
 */

public class CategoryManager {

    private static CategoryManager sInstance;
    private ApkManager mApkManager;
    private DocManager mDocManager;
    private ZipManager mZipManager;
    private SortUtil.SortMethod mSortMethod = SortUtil.SortMethod.SIZE;

    private CategoryManager(Context context){

        ApkManager.init(context);
        DocManager.init(context);
        ZipManager.init(context);

        mApkManager = ApkManager.getInstance();
        mDocManager = DocManager.getInstance();
        mZipManager = ZipManager.getInstance();

    }

    public static void init(Context context){

        if (sInstance == null){
            synchronized (CategoryManager.class){
                if (sInstance == null)
                    sInstance = new CategoryManager(context);
            }
        }
    }

    public static CategoryManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init CategoryManager first");
        }
        return sInstance;
    }

    public CategoryManager setSortMethod(SortUtil.SortMethod mSortMethod) {
        this.mSortMethod = mSortMethod;
        return this;
    }

    public List<String> getApkList(){
        return mApkManager.getApkListBySort(mSortMethod);
    }

    public Observable<List<String>> getApkListUsingObservable(){
        return mApkManager.getApkListUsingObservable(mSortMethod);
    }

    public List<String> getApkListWithoutQuery(){
        return mApkManager.getApkList();
    }

    public List<String> getDocList(){
        return mDocManager.getDocListBySort(mSortMethod);
    }

    public Observable<List<String>> getDocListUsingObservable(){
        return mDocManager.getDocListUsingObservable(mSortMethod);
    }

    public List<String> getDocListWithoutQuery(){
        return mDocManager.getDocList();
    }

    public List<String> getZipList(){
        return mZipManager.getApkListBySort(mSortMethod);
    }

    public Observable<List<String>> getZipListUsingObservable(){
        return mZipManager.getZipListUsingObservable(mSortMethod);
    }

    public List<String> getZipListWithoutQuery(){
        return mZipManager.getZipList();
    }
}

