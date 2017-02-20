package com.jiepier.filemanager.ui.category.storage;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkInfo;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by panruijie on 2017/2/19.
 * Email : zquprj@gmail.com
 */

public class StorageContact {

    interface Presenter extends BasePresenter<View> {

        void startScanTask();

        void startCleanTask(ArrayList<JunkInfo> overList, ArrayList<JunkInfo> sysCacheList, Set<String> processList);

    }

    interface View extends BaseView {

        void showSystemCacheDialog();

        void showProcessDialog();

        void showOverDialog();

        void dimissSystemCacheDialog();

        void dimissOverDialog();

        void dimissProcessDialog();

        void setCurrenOverScanJunk(JunkInfo junk);

        void setCurrenSysCacheScanJunk(JunkInfo junk);

        void setData(ArrayList<JunkInfo> overList, ArrayList<JunkInfo> sysCacheList, ArrayList<AppProcessInfo> mProcessList);

        void setTotalJunk(long junkSize);

        void setRunningAppData(ArrayList<AppProcessInfo> list);
    }
}
