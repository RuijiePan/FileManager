package com.jiepier.filemanager.manager;

import android.content.Context;

/**
 * Created by panruijie on 2017/2/14.
 * Email : zquprj@gmail.com
 * 计算手机内存管理类
 */

public class StorageManager {

    private static StorageManager sInstance;
    private Context mContext;

    private StorageManager(Context context) {

        mContext = context.getApplicationContext();
    }

    public static void init(Context context) {

        if (sInstance == null) {
            synchronized (StorageManager.class) {
                if (sInstance == null) {
                    sInstance = new StorageManager(context);
                }
            }
        }
    }

    public StorageManager getInstance() {

        if (sInstance == null) {
            throw new IllegalStateException("You must be init CleanManager first");
        }
        return sInstance;
    }

}
