package com.jiepier.filemanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jiepier.filemanager.bean.Music;
import com.jiepier.filemanager.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 17/1/4.
 * Email : zquprj@gmail.com
 */

public class DataManager implements CRUD{

    private static DataManager sInstance;
    public static final String MUSIC = "music";
    public static final String DOC = "doc";
    public static final String ZIP = "zip";
    public static final String APK = "apk";
    private DBOpenHelper mMusicHelper;
    private DBOpenHelper mDocHelper;
    private DBOpenHelper mZipHelper;
    private DBOpenHelper mApkHelper;
    private SQLiteDatabase mDb;

    public static void init(Context context,int version){

        if (sInstance == null){
            synchronized (DataManager.class){
                if (sInstance == null)
                    sInstance = new DataManager(context,version);
            }
        }
    }

    private DataManager(Context context,int version){

        mMusicHelper = new DBOpenHelper(context,"music.db",null,version);
        mDocHelper = new DBOpenHelper(context,"doc.db",null,version);
        mZipHelper = new DBOpenHelper(context,"zip.db",null,version);
        mApkHelper = new DBOpenHelper(context,"apk.db",null,version);
    }

    public static DataManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init DataManager first");
        }
        return sInstance;
    }

    @Override
    public SQLiteDatabase getSQLite(String type) {
        switch (type){
            case MUSIC:
                return mMusicHelper.getWritableDatabase();
            case DOC:
                return mDocHelper.getWritableDatabase();
            case ZIP:
                return mZipHelper.getWritableDatabase();
            case APK:
                return mApkHelper.getWritableDatabase();
            default:
                return null;
        }
    }

    @Override
    public boolean insertSQL(String type, String path) {

        if (type.equals(MUSIC)){
            Music music = FileUtil.fileToMusic(new File(path));
            return insertMusicSQL(music);
        }else {
            mDb = getSQLite(type);
            ContentValues newValues = new ContentValues();
            newValues.put("path", path);
            return mDb.insert(type, null, newValues) > 0;
        }

    }

    @Override
    public Observable<Boolean> insertSQLUsingObservable(String type, String path) {

        return Observable.create(new Observable.OnSubscribe<Boolean>(){

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {

                subscriber.onNext(insertSQL(type,path));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean updateSQL(String type, List<String> list) {

        mDb = getSQLite(type);
        try {
            mDb.delete(type,null,null);

            for (int i= 0; i<list.size() ;i++)
                insertSQL(type,list.get(i));

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Observable<Boolean> updateSQLUsingObservable(String type, List<String> list) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(updateSQL(type,list));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public boolean deleteSQL(String type, String path) {

        if (type.equals(MUSIC)){
            return deleteMusic(path);
        }else {
            mDb = getSQLite(type);
            return mDb.delete(type, "path=?", new String[]{path}) > 0;
        }
    }

    @Override
    public Observable<Boolean> deleteSQLUsingObservable(String type, String path) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(deleteSQL(type,path));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean updateSQL(String type, String orignalPath, String path) {

        if (type.equals(MUSIC)){
            Music music = FileUtil.fileToMusic(new File(orignalPath));
            return updateMusic(music,path);
        }else {
            mDb = getSQLite(type);
            ContentValues newValues = new ContentValues();
            newValues.put("path", path);

            return mDb.update(type, newValues, "path=?", new String[]{orignalPath}) > 0;
        }
    }

    @Override
    public Observable<Boolean> updateSQLUsingObservable(String type,String orignalPath, String path) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(updateSQL(type,orignalPath,path));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public ArrayList<String> select(String type) {

        mDb = getSQLite(type);

        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = mDb.rawQuery("select path from "+type,null);

            if (cursor.moveToFirst()){
                do {
                    String path = cursor.getString(cursor.getColumnIndex("path"));
                    list.add(path);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }

        return list;
    }

    @Override
    public Observable<ArrayList<String>> selectUsingObservable(String type) {

        return Observable.create(new Observable.OnSubscribe<ArrayList<String>>(){

            @Override
            public void call(Subscriber<? super ArrayList<String>> subscriber) {
                subscriber.onNext(select(type));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean insertMusicSQL(Music music) {

        mDb = getSQLite(MUSIC);

        ContentValues newValues = new ContentValues();
        newValues.put("title",music.getTitle());
        newValues.put("album",music.getAlbum());
        newValues.put("artist",music.getArtist());
        newValues.put("url",music.getUrl());
        newValues.put("duration",music.getDuration());
        newValues.put("size",music.getSize());

        return mDb.insert(MUSIC,null,newValues)>0;
    }

    @Override
    public Observable<Boolean> insertMusicSQLUsingObservable(Music music) {

        return Observable.create(new Observable.OnSubscribe<Boolean>(){

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(insertMusicSQL(music));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean deleteMusic(String path) {

        mDb = getSQLite(MUSIC);
        return mDb.delete(MUSIC,"url=?",new String[]{path}) > 0;
    }

    @Override
    public Observable<Boolean> deleteMusicSQLUsingObservable(String path) {
        mDb = getSQLite(MUSIC);

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(deleteMusic(path));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean updateMusic(Music music, String newPath) {
        mDb = getSQLite(MUSIC);

        ContentValues newValues = new ContentValues();
        newValues.put("url",newPath);

        return mDb.update(MUSIC,newValues,"id=?",new String[]{music.getId()+""}) > 0;
    }

    @Override
    public boolean updateMusic(List<Music> list) {

        mDb = getSQLite(MUSIC);

        try {
            mDb.delete(MUSIC,null,null);

            for (int i= 0; i<list.size() ;i++)
                insertMusicSQL(list.get(i));

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Observable<Boolean> updateMusicSQLUsingObservable(Music music, String newPath) {
        mDb = getSQLite(MUSIC);

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(updateMusic(music,newPath));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public ArrayList<Music> selectMusic() {
        mDb = getSQLite(MUSIC);

        ArrayList<Music> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery("select * from music", null);

            if (cursor.moveToFirst()){

                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String album = cursor.getString(cursor.getColumnIndexOrThrow("album"));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow("artist"));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    int size = cursor.getInt(cursor.getColumnIndexOrThrow("size"));

                    Music music = new Music(id, title, album, artist, url, duration, size);
                    list.add(music);
                }while (cursor.moveToNext());

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }

        return list;
    }

    @Override
    public Observable<ArrayList<Music>> selectMusicSQLUsingObservable() {
        mDb = getSQLite(MUSIC);

        return Observable.create(new Observable.OnSubscribe<ArrayList<Music>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Music>> subscriber) {
                subscriber.onNext(selectMusic());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
