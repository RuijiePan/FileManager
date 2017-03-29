package com.jiepier.filemanager.bean;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.Comparator;

/**
 * Created by panruijie on 2017/3/28.
 * Email : zquprj@gmail.com
 */

public class AppInfo implements Comparator<AppInfo> {

    private PackageInfo mPackageInfo;
    private boolean mIsSystem;
    private Drawable mDrawable;
    private String mName;
    private String mPackageName;
    private long mSize;
    private long mInstallTime;

    public AppInfo(PackageInfo mPackageInfo, boolean mIsSystem, Drawable mDrawable, String mName, String mPackageName, long mSize, long mInstallTime) {
        this.mPackageInfo = mPackageInfo;
        this.mIsSystem = mIsSystem;
        this.mDrawable = mDrawable;
        this.mName = mName;
        this.mPackageName = mPackageName;
        this.mSize = mSize;
        this.mInstallTime = mInstallTime;
    }

    public AppInfo() {

    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public AppInfo setPackageInfo(PackageInfo mPackageInfo) {
        this.mPackageInfo = mPackageInfo;
        return this;
    }

    public boolean isSystem() {
        return mIsSystem;
    }

    public AppInfo isSystem(boolean mIsSystem) {
        this.mIsSystem = mIsSystem;
        return this;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public AppInfo setDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
        return this;
    }

    public String getName() {
        return mName;
    }

    public AppInfo setName(String mName) {
        this.mName = mName;
        return this;
    }

    public long getSize() {
        return mSize;
    }

    public AppInfo setSize(long mSize) {
        this.mSize = mSize;
        return this;
    }

    public long getInstallTime() {
        return mInstallTime;
    }

    public AppInfo setInstallTime(long mInstallTime) {
        this.mInstallTime = mInstallTime;
        return this;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public AppInfo setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
        return this;
    }

    @Override
    public int compare(AppInfo o1, AppInfo o2) {
        if ((o1.isSystem() && o2.isSystem()) ||
                (!o1.isSystem() && !o2.isSystem())) {
            return o1.getName().compareTo(o2.getName());
        } else if (o1.isSystem() && !o2.isSystem()) {
            return 1;
        } else if (!o1.isSystem() && o2.isSystem()) {
            return -1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "mPackageInfo=" + mPackageInfo +
                ", mIsSystem=" + mIsSystem +
                ", mDrawable=" + mDrawable +
                ", mName='" + mName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mSize=" + mSize +
                ", mInstallTime=" + mInstallTime +
                '}';
    }
}
