package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/2/23.
 * Email : zquprj@gmail.com
 */

public class CurrentJunSizeEvent {

    private long totalSize;

    public CurrentJunSizeEvent(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}
