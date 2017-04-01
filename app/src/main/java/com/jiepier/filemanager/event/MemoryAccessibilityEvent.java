package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/3/31.
 * Email : zquprj@gmail.com
 */

public class MemoryAccessibilityEvent {

    private String mPackageName;

    public MemoryAccessibilityEvent(String packageName) {
        this.mPackageName = packageName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }
}
