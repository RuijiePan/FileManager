package com.jiepier.filemanager.task.callback;

import com.jiepier.filemanager.bean.JunkInfo;

import java.util.ArrayList;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 */

public interface IScanCallBack {

    void onBegin();

    void onProgress(JunkInfo junkInfo);

    void onFinish(ArrayList<JunkInfo> children);
}