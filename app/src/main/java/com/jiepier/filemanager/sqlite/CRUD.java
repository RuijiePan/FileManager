package com.jiepier.filemanager.sqlite;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by panruijie on 17/1/4.
 * Email : zquprj@gmail.com
 */

public interface CRUD {

    boolean insertSQL(String type,String path);

    boolean updateSQL(String type, List<String> list);

    boolean deleteSQL(String type,String path);

    boolean updateSQL(String type,String orignalPath,String path);

    SQLiteDatabase getSQLite(String type);

    Observable<ArrayList<String>> select(String type);
}
