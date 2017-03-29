package com.jiepier.filemanager.ui.appmanager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.event.PackageEvent;
import com.jiepier.filemanager.util.AppUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by JiePier on 16/12/7.
 */

public class AppManagerFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private AppManagerAdapter mAdapter;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_app_manager;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mAdapter = new AppManagerAdapter(getContext(), AppUtil.getInstalledApplicationInfo(getContext(), true));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void initListeners() {
        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(PackageEvent.class)
                .subscribe(packageEvent -> {
                    if (packageEvent.getState().equals(PackageEvent.REMOVE)) {
                        mAdapter.removeItem(packageEvent.getPackageName());
                    }
                }, Throwable::printStackTrace));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }
}
