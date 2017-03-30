package com.jiepier.filemanager.ui.appmanager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.event.PackageEvent;
import com.jiepier.filemanager.util.AppUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 2017/3/30.
 * Email : zquprj@gmail.com
 */

public class AppManagerPresenter implements AppManagerContact.Presenter {

    private AppManagerContact.View mView;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;

    public AppManagerPresenter(Context context) {
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(PackageEvent.class)
                .subscribe(packageEvent -> {
                    if (packageEvent.getState().equals(PackageEvent.REMOVE)) {
                        mView.removeItem(packageEvent.getPackageName());
                    }
                }, Throwable::printStackTrace));
    }

    @Override
    public void attachView(@NonNull AppManagerContact.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
    }

    @Override
    public void getData() {
        if (mView != null) {
            mView.showDialog();
        } else {
            throw new IllegalStateException("You must be attachView first");
        }

        AppUtil.getInstalledAppInfoUsingObservable(mContext, true)
                .subscribe(appInfos -> {
                    mView.setData(appInfos);
                    mView.dismissDialog();
                }, Throwable::printStackTrace);
    }

}
