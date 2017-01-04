package com.jiepier.filemanager.ui.category.categorybottom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.event.ActionChoiceFolderEvent;
import com.jiepier.filemanager.event.BroadcastEvent;
import com.jiepier.filemanager.event.ChoiceFolderEvent;
import com.jiepier.filemanager.event.NewTabEvent;
import com.jiepier.filemanager.event.TypeEvent;
import com.jiepier.filemanager.manager.ApkManager;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class CategoryBottomPresenter implements CategoryBottomContact.Presenter{

    private Context mContext;
    private CategoryManager mCategoryManager;
    private CompositeSubscription mCompositeSubscription;
    private CategoryBottomContact.View mView;
    private int mIndex;

    public CategoryBottomPresenter(Context context){
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
                        switch (mIndex){
                            case AppConstant.DOC_INDEX:
                                mView.setDataByObservable(mCategoryManager.getDocList());
                                break;
                            case AppConstant.ZIP_INDEX:
                                mView.setDataByObservable(mCategoryManager.getZipList());
                                break;
                            case AppConstant.APK_INDEX:
                                mView.setDataByObservable(mCategoryManager.getApkList());
                                break;
                        }
                    }else if (type == AppConstant.CLEAN_CHOICE){
                        mView.clearSelect();
                    }
                }, Throwable::printStackTrace));


    }

    @Override
    public void setIndex(int index) {

        mIndex = index;
        mView.showDialog();

        switch (index){
            case AppConstant.DOC_INDEX:
                mCategoryManager.getDocList().subscribe(list -> {
                    mView.setData(list);
                    mView.dimissDialog();
                });
                break;
            case AppConstant.ZIP_INDEX:
                mCategoryManager.getZipList().subscribe(list -> {
                    mView.setData(list);
                    mView.dimissDialog();
                });
                break;
            case AppConstant.APK_INDEX:
                mCategoryManager.getApkList().subscribe(list -> {
                    mView.setData(list);
                    mView.dimissDialog();
                });
                break;
        }
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
    public void attachView(@NonNull CategoryBottomContact.View view) {
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
