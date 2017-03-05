package com.jiepier.filemanager.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.Utils;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.manager.CleanManager;
import com.jiepier.filemanager.manager.ScanManager;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SharedUtil;

/**
 * Created by JiePier on 16/12/19.
 */

public class App extends Application {

    public static App sContext;
    public static int sHeight;
    public static int sWidth;
    private String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {

        sContext = this;

        //初始化工具类
        Utils.init(this);
        //初始化category各个模块需要的单例
        CategoryManager.init(this);
        //初始化扫描、清理管理类
        ScanManager.init(this);
        CleanManager.init(this);
        //初始化文件初始路径和各种Preferences参数
        Settings.updatePreferences(this);
        //初始化facebook广告
        /*AdManager.getInstance(this)
                .setAdId("200400853762325_200401077095636");*/

        /*如果是第一次加载，那么数据库里没有数据，那么直接扫描获取数据，否则在主界面通过广播
                扫描完之后再进行数据更新*/
        if (SharedUtil.getBoolean(this, AppConstant.IS_FIRST, true)) {
            CategoryManager.getInstance().update();
            SharedUtil.putString(this, AppConstant.DEFAULT_SCAN_PATH,
                    Settings.getDefaultDir());
            SharedUtil.putBoolean(this, AppConstant.IS_FIRST, false);
        }

        sWidth = ScreenUtils.getScreenWidth();
        sHeight = ScreenUtils.getScreenHeight();

    }

    public static Context getAppContext() {
        return sContext;
    }
}
