package com.jiepier.filemanager.ui.category.categorybottom;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.manager.ApkManager;
import com.jiepier.filemanager.manager.CategoryManager;

import java.util.ArrayList;

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

    public CategoryBottomPresenter(Context context){
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        mCategoryManager = CategoryManager.getInstance();
    }

    @Override
    public void setIndex(int index) {

        switch (index){
            case AppConstant.DOC_INDEX:
                mView.setData((ArrayList<String>) mCategoryManager.getDocList());
                break;
            case AppConstant.ZIP_INDEX:
                mView.setData((ArrayList<String>) mCategoryManager.getZipList());
                break;
            case AppConstant.APK_INDEX:
                mView.setData((ArrayList<String>) mCategoryManager.getApkList());
                break;
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
