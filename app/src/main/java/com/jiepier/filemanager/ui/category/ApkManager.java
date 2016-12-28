package com.jiepier.filemanager.ui.category;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.jiepier.filemanager.util.SortUtil;

import java.util.List;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 * 安装包管理
 */

public class ApkManager {

    private static ApkManager instance;
    private Context mContext;

    public static ApkManager getInstance(){
        if (instance == null){
            throw new IllegalStateException("you must be init first");
        }
        return instance;
    }

    public void init(Context context){
        this.mContext = context;
        instance = new ApkManager();
    }

    public List<String> getApkListBySort(SortUtil.SortMethod sort){

        Uri uri = uri = MediaStore.Files.getContentUri("external");
        String[] columns = new String[] {
                MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED
        };
        String selection = MediaStore.Files.FileColumns.DATA + " LIKE '%.apk'";
        String sortOrder = SortUtil.buildSortOrder(sort);

        Cursor cursor = mContext.getContentResolver().query(
            uri,columns,selection,null,sortOrder
        );

        cursor.close();
        return null;
    }

}
