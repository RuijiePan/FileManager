package com.jiepier.filemanager.bean;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.bean.entity.AbstractExpandableItem;
import com.jiepier.filemanager.bean.entity.MultiItemEntity;

/**
 * Created by panruijie on 2017/2/21.
 * Email : zquprj@gmail.com
 */

public class JunkType extends AbstractExpandableItem<JunkProcessInfo> implements MultiItemEntity {

    public static final int PROCESS = 0;
    public static final int CACHE = 1;
    public static final int APK = 2;
    public static final int TEMP = 3;
    public static final int LOG = 4;
    public static final int BIG_FILE = 5;

    private String title;
    private String totalSize;
    private int iconResourceId;
    private boolean isCheck;
    private boolean isProgressVisible;

    public JunkType() {

    }

    public JunkType(String title, String totalSize, int iconResourceId, boolean isCheck, boolean isProgressVisible) {
        this.title = title;
        this.totalSize = totalSize;
        this.iconResourceId = iconResourceId;
        this.isCheck = isCheck;
        this.isProgressVisible = isProgressVisible;
    }

    public String getTitle() {
        return title;
    }

    public JunkType setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public JunkType setTotalSize(String totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public JunkType setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public JunkType setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public boolean isProgressVisible() {
        return isProgressVisible;
    }

    public JunkType setProgressVisible(boolean progressVisible) {
        isProgressVisible = progressVisible;
        return this;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return AppConstant.TYPE_TITLE;
    }

    @Override
    public String toString() {
        return "JunkType{" +
                "title='" + title + '\'' +
                ", totalSize='" + totalSize + '\'' +
                ", iconResourceId=" + iconResourceId +
                ", isCheck=" + isCheck +
                ", isProgressVisible=" + isProgressVisible +
                '}';
    }
}
