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

public class ZipManager {

    private static ZipManager sInstance;
    private Context mContext;
    private static ArrayList<String> mZipList;

    public static ZipManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init ZipManager first");
        }
        return sInstance;
    }

    private ZipManager (Context context){
        mContext = context;
        mZipList = new ArrayList<>();
    }

    public static void init(Context context){

        if (sInstance == null)
            sInstance = new ZipManager(context);
    }

    public List<String> getZipListBySort(SortUtil.SortMethod sort){

        Uri uri = MediaStore.Files.getContentUri("external");

        String[] columns = new String[] {
                MediaStore.Files.FileColumns._ID
                , MediaStore.Files.FileColumns.DATA
                , MediaStore.Files.FileColumns.SIZE
                , MediaStore.Files.FileColumns.DATE_MODIFIED
        };
        String selection =  "(" + MediaStore.Files.FileColumns.MIME_TYPE +
                " == '" + "application/zip" + "')";
        String sortOrder = SortUtil.buildSortOrder(sort);

        Cursor cursor = mContext.getContentResolver().query(
            uri,columns,selection,null,sortOrder
        );

        mZipList.clear();
        if (cursor != null){

            try {
                cursor.moveToFirst();

                mZipList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                while (cursor.moveToNext()) {
                    mZipList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                cursor.close();
            }
        }

        return mZipList;
    }

    public Observable<List<String>> getZipListUsingObservable(SortUtil.SortMethod sort){

        return Observable.create(new Observable.OnSubscribe<List<String>>(){

            @Override
            public void call(Subscriber<? super List<String>> subscriber) {

                subscriber.onNext(getZipListBySort(sort));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public List<String> getZipList(){
            return mZipList;
    }
}
