package com.jiepier.filemanager.base;

import android.util.Log;

/**
 * Created by panruijie on 16/12/27.
 * Email : zquprj@gmail.com
 */

public abstract class BaseLazyFragment extends BaseFragment{

    protected abstract void onShow();

    protected abstract void onHide();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            onHide();
        }else {
            onShow();
        }
    }
}
