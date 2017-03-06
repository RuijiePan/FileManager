package com.jiepier.filemanager.sqlite;

import com.jiepier.filemanager.preview.MimeTypes;

import java.io.File;
import java.util.ArrayList;

import rx.Observable;

/**
 * Created by panruijie on 17/1/4.
 * Email : zquprj@gmail.com
 */

public class SqlUtil {

    public static void insert(String path) {

        String type = getType(path);
        if (type != null) {
            DataManager.getInstance()
                    .insertSQLUsingObservable(type, path)
                    .subscribe(aBoolean -> {

                    });
        }
    }

    public static void update(String orignalPath, String path) {

        String type = getType(path);
        if (type != null) {
            DataManager.getInstance()
                    .updateSQLUsingObservable(type, orignalPath, path)
                    .subscribe(aBoolean -> {

                    });
        }
    }

    public static void delete(String path) {

        String type = getType(path);
        if (type != null) {
            DataManager.getInstance()
                    .deleteSQLUsingObservable(type, path)
                    .subscribe(aBoolean -> {

                    });
        }

    }

    //查找结果是不同类型，故不在此做查询
    public static Observable<ArrayList<String>> select(String type) {

        return DataManager.getInstance()
                .selectUsingObservable(type);
    }

    private static String getType(String path) {

        //按文件频率进行选择，文件最多放前面
        File f = new File(path);
        if (MimeTypes.isDoc(f)) {
            return DataManager.DOC;
        } else if (MimeTypes.isZip(f)) {
            return DataManager.ZIP;
        } else if (MimeTypes.isMusic(f)) {
            return DataManager.MUSIC;
        } else if (MimeTypes.isVideo(f)) {
            return DataManager.VIDEO;
        } else if (MimeTypes.isApk(f)) {
            return DataManager.APK;
        }

        return null;
    }

}
