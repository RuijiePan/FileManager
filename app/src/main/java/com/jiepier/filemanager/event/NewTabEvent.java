package com.jiepier.filemanager.event;

/**
 * Created by JiePier on 16/12/14.
 */

public class NewTabEvent {

    private String path;

    public NewTabEvent(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
