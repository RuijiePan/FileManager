package com.jiepier.filemanager.event;

import com.jiepier.filemanager.bean.JunkGroup;
import com.jiepier.filemanager.bean.JunkType;


/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public class JunkDataEvent {

    private JunkGroup junkGroup;
    private JunkType junkType;
    private int index;

    public JunkDataEvent(JunkType junkType, int index) {
        this.junkType = junkType;
        this.junkGroup = null;
        this.index = index;
    }

    public JunkDataEvent(JunkGroup junkGroup) {
        this.junkGroup = junkGroup;
        this.junkType = null;
        index = -1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public JunkType getJunkType() {
        return junkType;
    }

    public void setJunkType(JunkType junkType) {
        this.junkType = junkType;
    }

    public JunkGroup getJunkGroup() {
        return junkGroup;
    }

    public void setJunkGroup(JunkGroup junkGroup) {
        this.junkGroup = junkGroup;
    }
}
