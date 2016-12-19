package com.jiepier.filemanager.event;

/**
 * Created by JiePier on 16/12/19.
 */

public class AllChoiceEvent {

    private String path;

    public AllChoiceEvent(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
