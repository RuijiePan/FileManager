package com.jiepier.filemanager.event;

/**
 * Created by JiePier on 16/12/20.
 */

public class ChangeDefaultDirEvent {

    private String type;

    public ChangeDefaultDirEvent(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
