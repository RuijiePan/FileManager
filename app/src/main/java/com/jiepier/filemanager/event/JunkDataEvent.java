package com.jiepier.filemanager.event;

import com.jiepier.filemanager.bean.JunkGroup;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public class JunkDataEvent {

    private JunkGroup junkGroup;

    public JunkDataEvent(JunkGroup junkGroup) {
        this.junkGroup = junkGroup;
    }

    public JunkGroup getJunkGroup() {
        return junkGroup;
    }

    public void setJunkGroup(JunkGroup junkGroup) {
        this.junkGroup = junkGroup;
    }
}
