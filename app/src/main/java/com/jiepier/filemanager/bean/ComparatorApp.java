package com.jiepier.filemanager.bean;

import java.util.Comparator;

/**
 * Created by panruijiesx on 2016/11/24.
 */

public class ComparatorApp implements Comparator<AppProcessInfo> {

    @Override
    public int compare(AppProcessInfo app, AppProcessInfo appAnother) {
        if (app.getUid() == appAnother.getUid()){
            if (app.getMemory() < appAnother.getMemory()){
                return 1;
            }else if (app.getMemory() == appAnother.getMemory()){
                return 0;
            }else {
                return -1;
            }
        }else {
            return app.getUid() - appAnother.getUid() ;
        }
    }

}
