package com.jiepier.filemanager.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jiepier.filemanager.util.SortUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 * 安装包管理
 */

public class PictureManager {

    private static PictureManager sInstance;
    private Context mContext;
    private ArrayList<String> mPictureList;

    private PictureManager(Context context){
        this.mContext = context;
        mPictureList = new ArrayList<>();
    }

    public static void init(Context context){

        if (sInstance == null)
            sInstance = new PictureManager(context);
    }

    public static PictureManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init PictureManager first");
        }
        return sInstance;
    }

    public List<String> getApkListBySort(SortUtil.SortMethod sort){

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        String[] selectionArgs = new String[] { "image/jpeg", "image/png" };
        String sortOrder = SortUtil.buildSortOrder(sort);

        Cursor cursor = mContext.getContentResolver().query(
            uri,null,selection,selectionArgs,sortOrder
        );

        mPictureList.clear();
        if (cursor != null){

            try {
                cursor.moveToFirst();

                mPictureList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                while (cursor.moveToNext()){
                    mPictureList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                cursor.close();
            }

        }

        return mPictureList;
    }

    public Observable<List<String>> getPictureListUsingObservable(SortUtil.SortMethod sort){

        return Observable.create(new Observable.OnSubscribe<List<String>>(){

            @Override
            public void call(Subscriber<? super List<String>> subscriber) {

                subscriber.onNext(getApkListBySort(sort));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<String> getPictureList(){
        return mPictureList;
    }
}
