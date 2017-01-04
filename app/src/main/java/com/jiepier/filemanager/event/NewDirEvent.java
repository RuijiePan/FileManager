package com.jiepier.filemanager.event;

/**
 * Created by JiePier on 16/12/20.
 */

public class NewDirEvent {

    private String path;
    private String type;

    public NewDirEvent(String type,String path){
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
