package com.jiepier.filemanager.event;

import com.jiepier.filemanager.bean.JunkInfo;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public class CurrenScanJunkEvent {

    public static final int SYS_CAHCE = 0;
    public static final int OVER_CACHE = 1;
    private int type;
    private JunkInfo junkInfo;

    public CurrenScanJunkEvent(int type, JunkInfo junkInfo) {
        this.junkInfo = junkInfo;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JunkInfo getJunkInfo() {
        return junkInfo;
    }

    public void setJunkInfo(JunkInfo junkInfo) {
        this.junkInfo = junkInfo;
    }
}
