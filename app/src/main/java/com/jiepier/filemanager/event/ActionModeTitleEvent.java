package com.jiepier.filemanager.event;

/**
 * Created by JiePier on 16/12/21.
 */

public class ActionModeTitleEvent {

    private int count;

    public ActionModeTitleEvent(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
