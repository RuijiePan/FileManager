package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/2/27.
 * Email : zquprj@gmail.com
 */

public class StorageRefreshEvent {

    private boolean isCheck;
    private int position;
    private int size;

    public StorageRefreshEvent(int position, int size, boolean isCheck) {
        this.position = position;
        this.size = size;
        this.isCheck = isCheck;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
