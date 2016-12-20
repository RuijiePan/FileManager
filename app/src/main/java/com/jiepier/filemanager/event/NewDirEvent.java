package com.jiepier.filemanager.event;

/**
 * Created by JiePier on 16/12/20.
 */

public class NewDirEvent {

    private String path;

    public NewDirEvent(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
