package com.jiepier.filemanager.ui.category.music;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.constant.AppConstant;
import com.jiepier.filemanager.event.ActionChoiceFolderEvent;
import com.jiepier.filemanager.event.TypeEvent;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.Settings;

import java.io.File;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class MusicPresenter implements MusicContact.Presenter{

    private Context mContext;
    private CategoryManager mCategoryManager;
    private CompositeSubscription mCompositeSubscription;
    private MusicContact.View mView;

    public MusicPresenter(Context context){
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        mCategoryManager = CategoryManager.getInstance();

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(TypeEvent.class)
                .map(TypeEvent::getType)
                .subscribe(type -> {
                    if (type == AppConstant.SELECT_ALL){
                        mView.selectAll();
                    }else if (type == AppConstant.REFRESH){
                        mView.setDataByObservable(mCategoryManager.getMusicList());
                    }else if (type == AppConstant.CLEAN_CHOICE){
                        mView.clearSelect();
                    }
                }, Throwable::printStackTrace));

    }

    @Override
    public void onItemClick(String path) {
        File file = new File(path);

        //解压文件
        if (FileUtil.isSupportedArchive(file)){
            RxBus.getDefault().post(new ActionChoiceFolderEvent(path, Settings.getDefaultDir()));
        }else {
            FileUtil.openFile(mContext,file);
        }

    }

    @Override
    public void getData() {
        mView.showDialog();

        mCategoryManager.getMusicList()
                .subscribe(musics -> {
                    mView.setData(musics);
                    mView.dimissDialog();
                }, Throwable::printStackTrace);
    }

    @Override
    public void attachView(@NonNull MusicContact.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (mCompositeSubscription.isUnsubscribed())
            this.mCompositeSubscription.unsubscribe();
        this.mCompositeSubscription = null;
    }

}
