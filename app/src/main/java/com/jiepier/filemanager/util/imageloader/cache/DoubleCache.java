package com.jiepier.filemanager.util.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.jiepier.filemanager.util.imageloader.request.BitmapRequest;

/**
 * Created by panruijie on 17/1/17.
 * Email : zquprj@gmail.com
 */

public class DoubleCache implements BitmapCache {

    /**
     *
     */
    DiskCache mDiskCache;
    /**
     *
     */
    MemoryCache mMemoryCache = new MemoryCache();

    public DoubleCache(Context context){
        mDiskCache = DiskCache.getDiskCache(context);
    }

    @Override
    public Bitmap get(BitmapRequest key) {
        Bitmap value = mMemoryCache.get(key);
        if (value == null){
            value = mDiskCache.get(key);
            saveBitmapIntoMemory(key, value);
        }
        return value;
    }

    private void saveBitmapIntoMemory(BitmapRequest key, Bitmap bitmap) {
        // 如果Value从disk中读取,那么存入内存缓存
        if (bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @Override
    public void put(BitmapRequest key, Bitmap value) {
        mDiskCache.put(key,value);
        mMemoryCache.put(key,value);
    }

    @Override
    public void remove(BitmapRequest key) {
        mDiskCache.remove(key);
        mMemoryCache.remove(key);
    }
}
