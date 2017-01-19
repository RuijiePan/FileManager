package com.jiepier.filemanager.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.utils.AppUtils;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.ImageFolder;
import com.jiepier.filemanager.bean.Music;
import com.jiepier.filemanager.sqlite.DataManager;
import com.jiepier.filemanager.util.SortUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/29.
 * Email : zquprj@gmail.com
 * 文件管理类 : 普通get方法线程阻塞、rx方法线程不阻塞
 */

public class CategoryManager {

    private String TAG = getClass().getSimpleName();
    private static CategoryManager sInstance;
    private MusicManager mMusicManager;
    private VideoManager mVideoManager;
    private PictureManager mPictureManager;
    private ApkManager mApkManager;
    private DocManager mDocManager;
    private ZipManager mZipManager;
    private ProcessManager mProcessManager;
    private DataManager mDataManager;
    private SortUtil.SortMethod mSortMethod = SortUtil.SortMethod.DATE;

    private CategoryManager(Context context){

        MusicManager.init(context);
        VideoManager.init(context);
        PictureManager.init(context);
        ApkManager.init(context);
        DocManager.init(context);
        ZipManager.init(context);
        ProcessManager.init(context);
        DataManager.init(context, AppUtils.getAppVersionCode(context));

        mMusicManager = MusicManager.getInstance();
        mVideoManager = VideoManager.getInstance();
        mPictureManager = PictureManager.getInstance();
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
    public Observable<ArrayList<Music>> getMusicList(){
        return mDataManager.selectMusicSQLUsingObservable();
    }

    //从ContentProvicer找
    public Observable<ArrayList<Music>> getMusicListUsingObservable(){
        return mMusicManager.getMusicListUsingObservable(mSortMethod);
    }

    public Observable<ArrayList<String>> getVideoList(){
        return mDataManager.selectUsingObservable(DataManager.VIDEO);
    }

    public Observable<List<String>> getVideoListUsingObservable(){
        return mVideoManager.getVideoListUsingObservable(mSortMethod);
    }

    public Observable<ArrayList<ImageFolder>> getPictureList(){
        return mDataManager.selectPictureSQLUsingObservable();
    }

    public Observable<ArrayList<ImageFolder>> getPictureListUsingObservable(){
        return mPictureManager.getPictureListUsingObservable(mSortMethod);
    }

    public Observable<ArrayList<String>> getApkList(){
        return mDataManager.selectUsingObservable(DataManager.APK);
    }

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

        getMusicListUsingObservable()
                .observeOn(Schedulers.io())
                .subscribe(musics -> {
                    mDataManager.updateMusic(musics);
                });

        getVideoListUsingObservable()
                .observeOn(Schedulers.io())
                .subscribe(list -> {
                    mDataManager.updateSQL(DataManager.VIDEO,list);
                });

        getPictureListUsingObservable()
                .observeOn(Schedulers.io())
                .subscribe(pictures->{
                    mDataManager.updatePicture(pictures);
                });

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


