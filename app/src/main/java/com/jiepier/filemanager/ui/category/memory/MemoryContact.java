package com.jiepier.filemanager.ui.category.memory;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.bean.AppProcessInfo;

import java.util.List;
import java.util.Set;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryContact {

    interface View extends BaseView{

        void showLoadingView();

        void dimissLoadingView();

        void showMemoryClean(long memory);

        void setData(List<AppProcessInfo> list);
    }

    interface Presenter extends BasePresenter<View>{

        void getRunningAppInfo();

        void killRunningAppInfo(Set<String> set);
    }
}
