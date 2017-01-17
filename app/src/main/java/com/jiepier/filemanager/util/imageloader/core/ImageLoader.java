package com.jiepier.filemanager.util.imageloader.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jiepier.filemanager.util.imageloader.cache.BitmapCache;
import com.jiepier.filemanager.util.imageloader.cache.MemoryCache;
import com.jiepier.filemanager.util.imageloader.config.DisplayConfig;
import com.jiepier.filemanager.util.imageloader.config.ImageLoaderConfig;
import com.jiepier.filemanager.util.imageloader.policy.SerialPolicy;
import com.jiepier.filemanager.util.imageloader.request.BitmapRequest;

/**
 * Created by panruijie on 17/1/13.
 * Email : zquprj@gmail.com
 */

public class ImageLoader {

    private static ImageLoader sInstance;

    private RequestQueue mImageQueue;

    private volatile BitmapCache mCache = new MemoryCache();

    /**
     * 图片加载配置对象
     */
    private ImageLoaderConfig mConfig;

    public static ImageLoader getInstance(){
        if (sInstance == null){
            synchronized (ImageLoader.class){
                if (sInstance == null)
                    sInstance = new ImageLoader();
            }
        }
        return sInstance;
    }

    public void init(ImageLoaderConfig config){
        mConfig = config;
        mCache = mConfig.bitmapCache;
        checkConfig();
        mImageQueue = new RequestQueue(mConfig.threadCount);
        mImageQueue.start();
    }

    private void checkConfig() {
        if (mConfig == null) {
            throw new RuntimeException(
                    "The config of ImageLoader is Null, please call the init(ImageLoaderConfig config) method to initialize");
        }

        if (mConfig.loadPolicy == null) {
            mConfig.loadPolicy = new SerialPolicy();
        }
        if (mCache == null) {
            mCache = new MemoryCache();
        }

    }

    public void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, null, null);
    }

    public void displayImage(ImageView imageView, String uri, DisplayConfig config) {
        displayImage(imageView, uri, config, null);
    }

    public void displayImage(ImageView imageView, String uri, ImageListener listener) {
        displayImage(imageView, uri, null, listener);
    }

    public void displayImage(final ImageView imageView, final String uri,
                             final DisplayConfig config, final ImageListener listener) {
        BitmapRequest request = new BitmapRequest(imageView, uri, config, listener);
        // 加载的配置对象,如果没有设置则使用ImageLoader的配置
        request.displayConfig = request.displayConfig != null ? request.displayConfig
                : mConfig.displayConfig;
        // 添加对队列中
        mImageQueue.addRequest(request);
    }

    public ImageLoaderConfig getConfig() {
        return mConfig;
    }


    public void stop() {
        mImageQueue.stop();
    }

    public static interface ImageListener {
        public void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }
}

