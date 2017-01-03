package com.jiepier.filemanager.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jiepier.filemanager.util.SortUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 * 文档管理
 */

public class DocManager {

    private static DocManager sInstance;
    private Context mContext;
    private List<String> mDocList;
    private HashSet<String> mDocMimeTypesSet = new HashSet<String>() {
        {
            add("text/plain");
            add("text/plain");
            add("application/pdf");
            add("application/msword");
            add("application/vnd.ms-excel");
            add("application/vnd.ms-excel");
        }
    };

    private DocManager(Context context){
        mContext = context;
        mDocList = new ArrayList<>();
    }

    public static void init(Context context){

        if (sInstance == null)
            sInstance = new DocManager(context);
    }

    public static DocManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init DocManager first");
        }
        return sInstance;
    }

    public List<String> getDocListBySort(SortUtil.SortMethod sort){

        Uri uri = MediaStore.Files.getContentUri("external");

        String[] columns = new String[] {
                MediaStore.Files.FileColumns._ID
                , MediaStore.Files.FileColumns.DATA
                , MediaStore.Files.FileColumns.SIZE
                , MediaStore.Files.FileColumns.DATE_MODIFIED
        };

        String selection = getDocSelection();

        String sortOrder = SortUtil.buildSortOrder(sort);

        Cursor cursor = mContext.getContentResolver().query(
                uri,columns,selection,null,sortOrder
        );

        mDocList.clear();
        if (cursor != null){
            cursor.moveToFirst();

            mDocList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
            while (cursor.moveToNext()){
                mDocList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
            }
        }

        if (cursor != null)
            cursor.close();

        return mDocList;
    }

    public Observable<List<String>> getDocListUsingObservable(SortUtil.SortMethod sort){

        return Observable.create(new Observable.OnSubscribe<List<String>>(){

            @Override
            public void call(Subscriber<? super List<String>> subscriber) {

                subscriber.onNext(getDocListBySort(sort));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public List<String> getDocList(){
        return mDocList;
    }

    private String getDocSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = mDocMimeTypesSet.iterator();
        while(iter.hasNext()) {
            selection.append("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return  selection.substring(0, selection.lastIndexOf(")") + 1);
    }
}
