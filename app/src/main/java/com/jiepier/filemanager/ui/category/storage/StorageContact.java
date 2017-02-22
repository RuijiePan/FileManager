package com.jiepier.filemanager.ui.category.storage;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.JunkProcessInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by panruijie on 2017/2/19.
 * Email : zquprj@gmail.com
 */

public class StorageContact {

    interface Presenter extends BasePresenter<View> {

        void startScanTask();

        void startCleanTask(ArrayList<JunkInfo> overList, ArrayList<JunkInfo> sysCacheList, Set<String> processList);

        void initAdapterData();
    }

    interface View extends BaseView {

        void setAdapterData(List<MultiItemEntity> data);

        void showDialog();

        void dimissDialog(int index);

        void setCurrenOverScanJunk(JunkInfo junk);

        void setCurrenSysCacheScanJunk(JunkInfo junk);

        void setData(HashMap<Integer, ArrayList<JunkProcessInfo>> hashMap);

        void setTotalJunk(String junkSize);

        void setRunningAppData(ArrayList<AppProcessInfo> list);

        void setItemTotalJunk(int index, String junkSize);

    }
}
