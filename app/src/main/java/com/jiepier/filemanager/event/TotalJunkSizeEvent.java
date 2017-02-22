package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public class TotalJunkSizeEvent {

    private String totalSize;

    public TotalJunkSizeEvent(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }
}
