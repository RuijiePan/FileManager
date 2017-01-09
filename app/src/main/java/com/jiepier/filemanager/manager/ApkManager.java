package com.jiepier.filemanager.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import com.jiepier.filemanager.util.SortUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 * 安装包管理
 */

public class ApkManager {

    private static ApkManager sInstance;
    private Context mContext;
    private ArrayList<String> mApkList;

    private ApkManager(Context context){
        this.mContext = context;
        mApkList = new ArrayList<>();
    }

    public static void init(Context context){

        if (sInstance == null)
            sInstance = new ApkManager(context);
    }

    public static ApkManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init ApkManager first");
        }
        return sInstance;
    }

    public List<String> getApkListBySort(SortUtil.SortMethod sort){

        Uri uri = MediaStore.Files.getContentUri("external");
        String[] columns = new String[] {
                MediaStore.Files.FileColumns._ID
                , MediaStore.Files.FileColumns.DATA
                , MediaStore.Files.FileColumns.SIZE
                , MediaStore.Files.FileColumns.DATE_MODIFIED
        };
        String selection = MediaStore.Files.FileColumns.DATA + " LIKE '%.apk'";
        String sortOrder = SortUtil.buildSortOrder(sort);

        Cursor cursor = mContext.getContentResolver().query(
            uri,columns,selection,null,sortOrder
        );

        mApkList.clear();
        if (cursor != null){

            try {
                cursor.moveToFirst();

                mApkList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                while (cursor.moveToNext()){
                    mApkList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                cursor.close();
            }

        }

        return mApkList;
    }

    public Observable<List<String>> getApkListUsingObservable(SortUtil.SortMethod sort){

        return Observable.create(new Observable.OnSubscribe<List<String>>(){

            @Override
            public void call(Subscriber<? super List<String>> subscriber) {

                subscriber.onNext(getApkListBySort(sort));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<String> getApkList(){
        return mApkList;
    }
}
