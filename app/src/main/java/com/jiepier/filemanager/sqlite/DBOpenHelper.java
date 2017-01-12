package com.jiepier.filemanager.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JiePier on 16/11/20.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    //id、title、album、artist、url、duration、size
    private static final String CREATE_MUISC_TABLE_SQL = "create table music( id " +
            "integer primary key autoincrement,title varchar not null" +
            ",album varchar not null,artist varchar not null" +
            ",url varchar not null,duration integer not null" +
            ",size integer not null)";
    private static final String CREATE_DOC_TABLE_SQL = "create table doc( id " +
            "integer primary key autoincrement,path varchar not null)";
    private static final String CREATE_ZIP_TABLE_SQL = "create table zip( id " +
            "integer primary key autoincrement,path varchar not null)";
    private static final String CREATE_APK_TABLE_SQL = "create table apk( id " +
            "integer primary key autoincrement,path varchar not null)";
    private static final String DROP_MUSIC_SQL = "drop table if exists music";
    private static final String DROP_DOC_SQL = "drop table if exists doc";
    private static final String DROP_ZIP_SQL = "drop table if exists zip";
    private static final String DROP_APK_SQL = "drop table if exists apk";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MUISC_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_DOC_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_ZIP_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_APK_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_MUSIC_SQL);
        sqLiteDatabase.execSQL(DROP_DOC_SQL);
        sqLiteDatabase.execSQL(DROP_ZIP_SQL);
        sqLiteDatabase.execSQL(DROP_APK_SQL);

        onCreate(sqLiteDatabase);
    }
}
