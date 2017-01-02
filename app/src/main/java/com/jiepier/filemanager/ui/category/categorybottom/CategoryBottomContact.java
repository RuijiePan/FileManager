package com.jiepier.filemanager.ui.category.categorybottom;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;

import java.util.ArrayList;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class CategoryBottomContact {

    interface View extends BaseView{

        void setData(ArrayList<String> list);
    }

    interface Presenter extends BasePresenter<View>{

        void setIndex(int index);

    }
}
