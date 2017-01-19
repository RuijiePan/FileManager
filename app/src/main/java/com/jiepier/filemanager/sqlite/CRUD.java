package com.jiepier.filemanager.sqlite;

import android.database.sqlite.SQLiteDatabase;

import com.jiepier.filemanager.bean.ImageFolder;
import com.jiepier.filemanager.bean.Music;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by panruijie on 17/1/4.
 * Email : zquprj@gmail.com
 */

public interface CRUD {

    //apk、word、zip、video接口
    boolean insertSQL(String type,String path);

    Observable<Boolean> insertSQLUsingObservable(String type,String path);

    boolean updateSQL(String type, List<String> list);

    Observable<Boolean> updateSQLUsingObservable(String type,List<String> list);

    boolean deleteSQL(String type,String path);

    Observable<Boolean> deleteSQLUsingObservable(String type,String path);

    boolean updateSQL(String type,String orignalPath,String path);

    Observable<Boolean> updateSQLUsingObservable(String type,String orignalPath,String path);

    SQLiteDatabase getSQLite(String type);

    ArrayList<String> select(String type);

    Observable<ArrayList<String>> selectUsingObservable(String type);

    //music接口
    boolean insertMusicSQL(Music music);

    Observable<Boolean> insertMusicSQLUsingObservable(Music music);

    boolean deleteMusic(String path);

    Observable<Boolean> deleteMusicSQLUsingObservable(String path);

    boolean updateMusic(Music music,String newPath);

    boolean updateMusic(List<Music> list);

    Observable<Boolean> updateMusicSQLUsingObservable(Music music,String newPath);

    ArrayList<Music> selectMusic();

    Observable<ArrayList<Music>> selectMusicSQLUsingObservable();

    //picture接口
    boolean insertPictureSQL(String path);

    boolean insertPictureSQL(ImageFolder imageFolder);

    Observable<Boolean> insertPictureSQLUsingObservable(String path);

    Observable<Boolean> insertPictureSQLUsingObservable(ImageFolder imageFolder);

    boolean deletePictureSQL(String path);

    Observable<Boolean> deletePictureSQLUsingObservable(String path);

    boolean updatePicture(List<ImageFolder> list);

    boolean updatePictureSQL(String oldPath,String newPath);

    Observable<Boolean> updatePictureSQLUsingObservable(String path,String newPath);

    ArrayList<ImageFolder> selectPicture();

    ImageFolder selectPictureFloder(String path);

    Observable<ArrayList<ImageFolder>> selectPictureSQLUsingObservable();
}
