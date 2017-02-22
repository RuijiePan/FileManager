package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public class JunkDismissDialogEvent {

    private int index;

    public JunkDismissDialogEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
