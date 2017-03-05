package com.jiepier.filemanager.ui.category.storage;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.bean.JunkGroup;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.entity.MultiItemEntity;

import java.util.ArrayList;
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

        void updateJunkInfo(List<MultiItemEntity> list);
    }

    interface View extends BaseView {

        void setAdapterData(List<MultiItemEntity> data);

        void showDialog();

        void dimissDialog(int index);

        void setCurrenOverScanJunk(JunkInfo junk);

        void setCurrenSysCacheScanJunk(JunkInfo junk);

        void setData(JunkGroup junkGroup);

        void setTotalJunk(String junkSize);

        void groupClick(boolean isExpand, int position);

        void setItemTotalJunk(int index, String junkSize);

    }
}
