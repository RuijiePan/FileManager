package com.jiepier.filemanager.manager;

import android.content.Context;

import com.facebook.ads.NativeAd;

/**
 * Created by panruijie on 17/1/25.
 * Email : zquprj@gmail.com
 */

public class AdManager {

    public static AdManager sInstance;
    private Context mContext;
    private NativeAd mNativeAd;

    public static AdManager getInstance(Context context){

        if (sInstance == null){
            synchronized (AdManager.class){
                if (sInstance == null)
                    sInstance = new AdManager(context);
            }
        }

        return sInstance;
    }

    public AdManager(Context context){

        this.mContext = context.getApplicationContext();

    }

    public AdManager setAdId(String AdId){

        if (sInstance == null)
            throw new IllegalStateException("You must be getInstacne AdManager first");

        mNativeAd = new NativeAd(mContext,AdId);
        return sInstance;
    }

    public NativeAd getNativeAd(){

        if (mNativeAd!=null)
            return mNativeAd;

        throw new IllegalStateException("NativeAd is null,please check it first");
    }
}
