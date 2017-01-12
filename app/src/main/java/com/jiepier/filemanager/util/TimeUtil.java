package com.jiepier.filemanager.util;

/**
 * Created by JiePier on 16/11/14.
 */

public class TimeUtil {

    public static String getTime(int time){
        int minute = time/1000/60;
        int second = time/1000%60;
        return (minute<10?"0"+minute:minute) + ":" +
                (second<10?"0"+second:second);
    }
}
