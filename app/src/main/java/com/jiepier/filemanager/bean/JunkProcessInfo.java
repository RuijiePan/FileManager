package com.jiepier.filemanager.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;

/**
 * Created by panruijie on 2017/2/21.
 * Email : zquprj@gmail.com
 */

public class JunkProcessInfo implements MultiItemEntity {

    public static final int PROCESS = 0;
    public static final int CACHE = 1;
    public static final int APK = 2;
    public static final int TEMP = 3;
    public static final int LOG = 4;
    public static final int BIG_FILE = 5;

    private JunkInfo junkInfo;
    private AppProcessInfo appProcessInfo;
    private int iconResourceId;
    private boolean isCheck;

    public JunkProcessInfo(JunkInfo junkInfo, int type) {
        this.junkInfo = junkInfo;
        if (type == CACHE) {
            iconResourceId = R.drawable.icon_cache_white_24dp;
            isCheck = false;
        } else if (type == APK) {
            iconResourceId = R.drawable.icon_apk_white_24dp;
            isCheck = true;
        } else if (type == TEMP) {
            iconResourceId = R.drawable.icon_apk_white_24dp;
            isCheck = true;
        } else if (type == LOG) {
            iconResourceId = R.drawable.icon_apk_white_24dp;
            isCheck = true;
        } else if (type == BIG_FILE) {
            iconResourceId = R.drawable.icon_apk_white_24dp;
            isCheck = false;
        }
    }

    public JunkProcessInfo(AppProcessInfo appProcessInfo) {
        this.appProcessInfo = appProcessInfo;
        isCheck = true;
    }

    public JunkProcessInfo(JunkInfo junkInfo, AppProcessInfo appProcessInfo) {
        this.junkInfo = junkInfo;
        this.appProcessInfo = appProcessInfo;
    }

    public JunkInfo getJunkInfo() {
        return junkInfo;
    }

    public void setJunkInfo(JunkInfo junkInfo) {
        this.junkInfo = junkInfo;
    }

    public AppProcessInfo getAppProcessInfo() {
        return appProcessInfo;
    }

    public void setAppProcessInfo(AppProcessInfo appProcessInfo) {
        this.appProcessInfo = appProcessInfo;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int getItemType() {
        return AppConstant.TYPE_CHILD;
    }
}
