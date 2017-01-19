package com.jiepier.filemanager.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.Utils;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SharedUtil;
import com.jiepier.filemanager.util.imageloader.cache.MemoryCache;
import com.jiepier.filemanager.util.imageloader.config.ImageLoaderConfig;
import com.jiepier.filemanager.util.imageloader.core.ImageLoader;
import com.jiepier.filemanager.util.imageloader.policy.ReversePolicy;

/**
 * Created by JiePier on 16/12/19.
 */

public class App extends Application {

    public static Context sContext;
    public static int sHeight;
    public static int sWidth;
    private String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {

        sContext = this;

        Utils.init(this);
        CategoryManager.init(this);
        Settings.updatePreferences(this);

        /*如果是第一次加载，那么数据库里没有数据，那么直接扫描获取数据，否则在主界面通过广播
                扫描完之后再进行数据更新*/
        if (SharedUtil.getBoolean(this,AppConstant.IS_FIRST,true)){
            CategoryManager.getInstance().update();
            SharedUtil.putString(this,AppConstant.DEFAULT_SCAN_PATH,
                    Settings.getDefaultDir());
            SharedUtil.putBoolean(this,AppConstant.IS_FIRST,false);
        }

        sWidth = ScreenUtils.getScreenWidth();
        sHeight = ScreenUtils.getScreenHeight();

        /*ImageLoaderConfig config = new ImageLoaderConfig()
                .setLoadingPlaceholder(R.drawable.image_loading)
                .setNotFoundPlaceholder(R.drawable.image_load_failure)
                .setCache(new MemoryCache())
                .setThreadCount(4)
                .setLoadPolicy(new ReversePolicy());
        ImageLoader.getInstance().init(config);*/
    }

    public static Context getAppContext(){
        return sContext;
    }
}
