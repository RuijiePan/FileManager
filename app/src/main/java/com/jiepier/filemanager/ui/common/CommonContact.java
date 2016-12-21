package com.jiepier.filemanager.ui.common;

import com.jiepier.filemanager.base.BaseView;

/**
 * Created by JiePier on 16/12/14.
 */

public interface CommonContact {

    interface View extends BaseView{

        void showSnackbar(String content);

        void showDialog();

        void dismissDialog();

    }

    interface BasePresenter extends com.jiepier.filemanager.base.BasePresenter<View> {

        void onItemClick(String filePath,String parentPath);

    }
}
