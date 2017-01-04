package com.jiepier.filemanager.event;

/**
 * Created by panruijie on 17/1/4.
 * Email : zquprj@gmail.com
 */

public class CategoryTypeEvent {

    public String type;

    public CategoryTypeEvent(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
