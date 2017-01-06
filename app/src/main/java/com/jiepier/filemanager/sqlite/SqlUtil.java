package com.jiepier.filemanager.sqlite;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.event.TypeEvent;
import com.jiepier.filemanager.preview.MimeTypes;
import com.jiepier.filemanager.sqlite.DataManager;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.io.File;
import java.util.ArrayList;

import rx.Observable;

/**
 * Created by panruijie on 17/1/4.
 * Email : zquprj@gmail.com
 */

public class SqlUtil {

    public static void insert(String path){

        String type = getType(path);
        if (type != null)
            DataManager.getInstance().insertSQLUsingObservable(type,path);
    }

    public static void update(String orignalPath,String path){

        String type = getType(path);
        if (type != null)
            DataManager.getInstance().updateSQLUsingObservable(type,orignalPath,path);
    }

    public static void delete(String path){

        String type = getType(path);
        if (type != null)
            DataManager.getInstance().deleteSQLUsingObservable(type,path);
    }

    public static Observable<ArrayList<String>> select(String type){

        return DataManager.getInstance().selectUsingObservable(type);
    }

    private static String getType(String path){

        File f = new File(path);
        if (MimeTypes.isApk(f)){
            return DataManager.APK;
        }else if (MimeTypes.isDoc(f)){
            return DataManager.DOC;
        }else if (MimeTypes.isZip(f)){
            return DataManager.ZIP;
        }

        return null;
    }
}
