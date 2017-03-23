package com.jiepier.filemanager.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jiepier.filemanager.bean.Music;
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
 * 音乐管理
 */

public class MusicManager {

    private static MusicManager sInstance;
    private Context mContext;
    private static ArrayList<Music> mMusicList;

    public static MusicManager getInstance() {

        if (sInstance == null) {
            throw new IllegalStateException("You must be init MusicManager first");
        }
        return sInstance;
    }

    private MusicManager(Context context) {
        mContext = context;
        mMusicList = new ArrayList<>();
    }

    public static void init(Context context) {

        if (sInstance == null)
            sInstance = new MusicManager(context);
    }

    public ArrayList<Music> getMusicListBySort(SortUtil.SortMethod sort) {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String sortOrder = SortUtil.buildSortOrder(sort);

        mMusicList.clear();
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    uri, null, null, null, sortOrder);

            if (cursor != null) {
                if (cursor.moveToFirst()) {

                    do {
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                        int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                        Music music = new Music(title, album, artist, url, duration, size);
                        mMusicList.add(music);
                    } while (cursor.moveToNext());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return mMusicList;
    }

    public Observable<ArrayList<Music>> getMusicListUsingObservable(SortUtil.SortMethod sort) {

        return Observable.create(new Observable.OnSubscribe<ArrayList<Music>>() {

            @Override
            public void call(Subscriber<? super ArrayList<Music>> subscriber) {

                subscriber.onNext(getMusicListBySort(sort));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public List<Music> getMusicList() {
        return mMusicList;
    }
}
