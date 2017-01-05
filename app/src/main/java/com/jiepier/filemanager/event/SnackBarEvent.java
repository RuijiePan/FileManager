package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 17/1/5.
 * Email : zquprj@gmail.com
 */

public class SnackBarEvent {

    private String content;

    public SnackBarEvent(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
