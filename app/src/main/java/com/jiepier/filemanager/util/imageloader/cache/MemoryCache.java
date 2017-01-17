package com.jiepier.filemanager.util.imageloader.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.jiepier.filemanager.util.imageloader.request.BitmapRequest;

/**
 * Created by panruijie on 17/1/13.
 * Email : zquprj@gmail.com
 */

public class MemoryCache implements BitmapCache {

    private LruCache<String, Bitmap> mMemeryCache;

    public MemoryCache(){

        // 计算可使用的最大内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        // 取4分之一的可用内存作为缓存
        final int cacheSize = maxMemory / 4;

        mMemeryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getWidth() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(BitmapRequest key) {
        return mMemeryCache.get(key.imageUri);
    }

    @Override
    public void put(BitmapRequest key, Bitmap value) {
        mMemeryCache.put(key.imageUri,value);
    }

    @Override
    public void remove(BitmapRequest key) {
        mMemeryCache.remove(key.imageUri);
    }
}
