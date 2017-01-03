package com.jiepier.filemanager.event;

import java.util.List;

/**
 * Created by JiePier on 16/12/16.
 */

public class ActionMutipeChoiceEvent {

    private List<String> list;

    public ActionMutipeChoiceEvent(List<String> list){
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
