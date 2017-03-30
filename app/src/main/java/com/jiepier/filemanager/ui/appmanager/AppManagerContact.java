package com.jiepier.filemanager.ui.appmanager;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.bean.AppInfo;

import java.util.List;

/**
 * Created by panruijie on 2017/3/29.
 * Email : zquprj@gmail.com
 */

public class AppManagerContact {

    interface View extends BaseView {

        void showDialog();

        void dismissDialog();

        void setData(List<AppInfo> data);

        void removeItem(String pckName);
    }

    interface Presenter extends BasePresenter<View> {

        void getData();
    }

}
