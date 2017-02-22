package com.jiepier.filemanager.event;

import com.jiepier.filemanager.bean.JunkProcessInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public class JunkDataEvent {

    private HashMap<Integer, ArrayList<JunkProcessInfo>> hashMap;

    public JunkDataEvent(HashMap<Integer, ArrayList<JunkProcessInfo>> hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap<Integer, ArrayList<JunkProcessInfo>> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, ArrayList<JunkProcessInfo>> hashMap) {
        this.hashMap = hashMap;
    }
}
