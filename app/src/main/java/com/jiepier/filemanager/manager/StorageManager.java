package com.jiepier.filemanager.manager;

import android.content.Context;

/**
 * Created by panruijie on 2017/2/14.
 * Email : zquprj@gmail.com
 * 手机内存存储管理（sd卡类）:扫描和清理
 */

public class StorageManager {

    private static StorageManager sInstance;
    private ScanManager mScanManager;
    private CleanManager mCleanManager;
    private Context mContext;

    private StorageManager(Context context) {

        mContext = context.getApplicationContext();
        ScanManager.init(context);
        CleanManager.init(context);

        mScanManager = ScanManager.getInstance();
        mCleanManager = CleanManager.getInstance();
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
