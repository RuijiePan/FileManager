package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/3/29.
 * Email : zquprj@gmail.com
 */

public class PackageEvent {

    public static final String REMOVE = "remove";
    public static final String ADD = "add";
    public static final String REPLACE = "replace";
    private String mState;
    private String mPackageName;

    public PackageEvent(String packageName, String state) {
        this.mPackageName = packageName;
        this.mState = state;
    }

    public String getState() {
        return mState;
    }

    public PackageEvent setState(String mState) {
        this.mState = mState;
        return this;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public PackageEvent setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
        return this;
    }
}
