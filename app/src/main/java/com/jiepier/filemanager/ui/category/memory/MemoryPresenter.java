package com.jiepier.filemanager.ui.category.memory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.event.UpdateMemoryInfoEvent;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.util.Set;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryPresenter implements MemoryContact.Presenter {

    private Context mContext;
    private boolean mScanFinish;
    private CategoryManager mCategoryManager;
    private MemoryContact.View mView;
    private CompositeSubscription mCompositeSubscription;

    public MemoryPresenter(Context context) {
        mContext = context;
        mScanFinish = false;
        mCategoryManager = CategoryManager.getInstance();
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getRunningAppInfo() {

        mView.showLoadingView();

        mCategoryManager.getRunningAppList()
                .subscribe(appProcessInfos -> {
                    mView.dimissLoadingView();
                    mView.showBoomView();
                    mView.setData(appProcessInfos);
                    mScanFinish = true;
                }, Throwable::printStackTrace);
    }

    @Override
    public void killRunningAppInfo(Set<String> set) {

        if (!mScanFinish) {
            return;
        }

        mView.showLoadingView();

        mCategoryManager.killRunningAppUsingObservable(set)
            .subscribe(memory -> {
                mView.dimissLoadingView();
                mView.showMemoryClean(mContext.getString(R.string.clean) + ":" + (memory >> 20) + " M");
                mView.notifityItem();
                RxBus.getDefault().post(new UpdateMemoryInfoEvent());
            }, Throwable::printStackTrace);
    }

    @Override
    public void attachView(@NonNull MemoryContact.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mCompositeSubscription.isUnsubscribed()) {
            this.mCompositeSubscription.unsubscribe();
        }
        this.mCompositeSubscription = null;
    }
}
