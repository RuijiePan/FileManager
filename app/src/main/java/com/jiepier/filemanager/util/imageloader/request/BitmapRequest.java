package com.jiepier.filemanager.util.imageloader.request;

import android.widget.ImageView;

import com.jiepier.filemanager.util.imageloader.config.DisplayConfig;
import com.jiepier.filemanager.util.imageloader.core.ImageLoader;
import com.jiepier.filemanager.util.imageloader.policy.LoadPolicy;
import com.jiepier.filemanager.util.imageloader.utils.ImageViewHelper;
import com.jiepier.filemanager.util.imageloader.utils.Md5Helper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by panruijie on 17/1/13.
 * Email : zquprj@gmail.com
 * 网络请求类. 注意GET和DELETE不能传递参数,因为其请求的性质所致,用户可以将参数构建到url后传递进来到Request中.
 */

public class BitmapRequest implements Comparable<BitmapRequest>{

    Reference<ImageView> mImageViewRef;
    public DisplayConfig displayConfig;
    public ImageLoader.ImageListener imageListener;
    public String imageUri = "";
    public String imageUriMd5 = "";
    /**
     * 请求序列号
     */
    public int serialNum = 0;
    /**
     * 是否取消该请求
     */
    public boolean isCancel = false;
    public boolean justCacheInMem = false;
    /**
     * 加载策略
     */
    LoadPolicy mLoadPolicy = ImageLoader.getInstance().getConfig().loadPolicy;

    public BitmapRequest(ImageView imageView, String uri, DisplayConfig config, ImageLoader.ImageListener listener){
        mImageViewRef = new WeakReference<ImageView>(imageView);
        displayConfig = config;
        imageListener = listener;
        imageUri = uri;
        imageView.setTag(uri);
        imageUriMd5 = Md5Helper.toMD5(imageUri);
    }

    /**
     * @param policy
     */
    public void setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            mLoadPolicy = policy;
        }
    }

    /**
     * 判断imageview的tag与uri是否相等
     *
     * @return
     */
    public boolean isImageViewTagValid() {
        return mImageViewRef.get() != null && mImageViewRef.get().getTag().equals(imageUri);
    }

    public ImageView getImageView() {
        return mImageViewRef.get();
    }

    public int getImageViewWidth() {
        return ImageViewHelper.getImageViewWidth(mImageViewRef.get());
    }

    public int getImageViewHeight() {
        return ImageViewHelper.getImageViewHeight(mImageViewRef.get());
    }

    @Override
    public int compareTo(BitmapRequest o) {
        return 0;
    }
}
