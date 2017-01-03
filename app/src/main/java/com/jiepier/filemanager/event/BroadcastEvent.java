package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 17/1/3.
 * Email : zquprj@gmail.com
 */

public class BroadcastEvent {

    private String path;

    public BroadcastEvent(){
    }

    public BroadcastEvent(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
