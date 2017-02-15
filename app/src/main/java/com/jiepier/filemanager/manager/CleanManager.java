package com.jiepier.filemanager.manager;

import android.content.Context;

/**
 * Created by panruijie on 2017/2/14.
 * Email : zquprj@gmail.com
 */

public class CleanManager {

    private static CleanManager sInstance;
    private Context mContext;

    private CleanManager(Context context) {

        mContext = context.getApplicationContext();
    }

    public void init(Context context) {

        if (sInstance == null) {
            synchronized (CleanManager.class) {
                if (sInstance == null) {
                    sInstance = new CleanManager(context);
                }
            }
        }
    }

    public CleanManager getInstance() {

        if (sInstance == null) {
            throw new IllegalStateException("You must be init CleanManager first");
        }
        return sInstance;
    }
}
