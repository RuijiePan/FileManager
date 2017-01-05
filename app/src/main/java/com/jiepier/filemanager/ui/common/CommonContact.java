package com.jiepier.filemanager.ui.common;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;

/**
 * Created by JiePier on 16/12/14.
 */

public interface CommonContact {

    interface View extends BaseView{

        void setLongClick(boolean longClick);

        void clearSelect();

        void showSnackBar(String content);

        void refreshAdapter();

        void allChoiceClick();
    }

    interface Presenter extends BasePresenter<View> {

        void onItemClick(String filePath,String parentPath);

    }
}
