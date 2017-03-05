package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 2017/3/3.
 * Email : zquprj@gmail.com
 */

public class JunkTypeClickEvent {

    private boolean isExpand;
    private int position;

    public JunkTypeClickEvent(boolean isExpand, int position) {
        this.isExpand = isExpand;
        this.position = position;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
