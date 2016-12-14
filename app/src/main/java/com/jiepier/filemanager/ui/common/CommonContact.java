package com.jiepier.filemanager.ui.common;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;

import java.io.File;

/**
 * Created by JiePier on 16/12/14.
 */

public interface CommonContact {

    interface View extends BaseView{

        void showSnackbar(String content);

        void showDialog();

        void dismissDialog();

        void openFile(File file);
    }

    interface Presenter extends BasePresenter<View>{

        void onItemClick(String filePath);
    }
}
