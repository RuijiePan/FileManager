package com.jiepier.filemanager.ui.category;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public interface FileCategoryContact {

    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter{

        void clickMemoryProgressbar();

        void clickStorageProgressbar();

        void clickMusic();

        void clickVideo();

        void clickApk();

        void clickDoc();

        void clickPicture();

        void clickZip();
    }
}
