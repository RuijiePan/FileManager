package com.jiepier.filemanager.event;

/**
 * Created by JiePier on 16/12/20.
 */

public class ActionChoiceFolderEvent {

    private String parentPath;
    private String filePath;

    public ActionChoiceFolderEvent(String filePath, String parentPath){
        this.filePath = filePath;
        this.parentPath = parentPath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
