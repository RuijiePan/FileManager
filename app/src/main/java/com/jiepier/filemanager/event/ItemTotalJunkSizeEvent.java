package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public class ItemTotalJunkSizeEvent {

    private int index;
    private String totalSize;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ItemTotalJunkSizeEvent(int index, String totalSize) {
        this.index = index;
        this.totalSize = totalSize;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }
}
