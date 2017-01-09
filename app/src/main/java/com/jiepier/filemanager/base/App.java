package com.jiepier.filemanager.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.Utils;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.manager.DocManager;
import com.jiepier.filemanager.sqlite.DataManager;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SharedUtil;

/**
 * Created by JiePier on 16/12/19.
 */

public class App extends Application {

    public static Context sContext;
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

    }

}
