package com.jiepier.filemanager.ui.category.music;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.bean.Music;
import com.jiepier.filemanager.ui.category.categorybottom.CategoryBottomContact;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by panruijie on 17/1/12.
 * Email : zquprj@gmail.com
 */

public class MusicContact {

    interface View extends BaseView{

        void showDialog();

        void dimissDialog();

        void setData(ArrayList<Music> list);

        void selectAll();

        void clearSelect();

        void setDataByObservable(Observable<ArrayList<Music>> observable);
    }

    interface Presenter extends BasePresenter<View> {

        void onItemClick(String path);

        void getData();
    }
}
