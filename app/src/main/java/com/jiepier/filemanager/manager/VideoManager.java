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
 * Video管理
 */

public class VideoManager {

    private static VideoManager sInstance;
    private Context mContext;
    private ArrayList<String> mVideoList;

    private VideoManager(Context context) {
        this.mContext = context;
        mVideoList = new ArrayList<>();
    }

    public static void init(Context context) {

        if (sInstance == null) {
            sInstance = new VideoManager(context);
        }
    }

    public static VideoManager getInstance() {

        if (sInstance == null) {
            throw new IllegalStateException("You must be init VideoManager first");
        }
        return sInstance;
    }

    public List<String> getVideoListBySort(SortUtil.SortMethod sort) {

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String sortOrder = SortUtil.buildSortOrder(sort);

        Cursor cursor = mContext.getContentResolver().query(
                uri, null, null, null, sortOrder
        );

        mVideoList.clear();
        if (cursor != null) {

            try {
                cursor.moveToFirst();

                mVideoList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                while (cursor.moveToNext()) {
                    mVideoList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }

        }

        return mVideoList;
    }

    public Observable<List<String>> getVideoListUsingObservable(SortUtil.SortMethod sort) {

        return Observable.create(new Observable.OnSubscribe<List<String>>() {

            @Override
            public void call(Subscriber<? super List<String>> subscriber) {

                subscriber.onNext(getVideoListBySort(sort));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<String> getVideoList() {
        return mVideoList;
    }
}
