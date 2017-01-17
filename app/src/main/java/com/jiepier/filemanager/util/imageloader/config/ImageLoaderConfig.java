package com.jiepier.filemanager.util.imageloader.config;

import android.graphics.Bitmap;

import com.jiepier.filemanager.util.imageloader.cache.BitmapCache;
import com.jiepier.filemanager.util.imageloader.policy.LoadPolicy;
import com.jiepier.filemanager.util.imageloader.policy.SerialPolicy;

/**
 * Created by panruijie on 17/1/13.
 * Email : zquprj@gmail.com
 */

public class ImageLoaderConfig {

    /**
     * 图片缓存配置对象
     */
    public BitmapCache bitmapCache;
    /**
     * 加载图片时的loading和加载失败的图片配置对象
     */
    public DisplayConfig displayConfig = new DisplayConfig();
    /**
     * 加载策略
     */
    public LoadPolicy loadPolicy = new SerialPolicy();
    /**
     *
     */
    public int threadCount = Runtime.getRuntime().availableProcessors() + 1;
    /**
     * @param count
     * @return
     */
    public ImageLoaderConfig setThreadCount(int count) {
        threadCount = Math.max(1, count);
        return this;
    }

    public ImageLoaderConfig setCache(BitmapCache cache) {
        bitmapCache = cache;
        return this;
    }

    public ImageLoaderConfig setLoadingPlaceholder(int resId) {
        displayConfig.loadingResId = resId;
        return this;
    }

    public ImageLoaderConfig setNotFoundPlaceholder(int resId) {
        displayConfig.failedResId = resId;
        return this;
    }

    public ImageLoaderConfig setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            loadPolicy = policy;
        }
        return this;
    }
}
