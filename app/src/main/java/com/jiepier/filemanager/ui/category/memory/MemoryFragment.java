package com.jiepier.filemanager.ui.category.memory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.ui.category.memory.accessibility.MemoryAccessibilityManager;
import com.jiepier.filemanager.util.Loger;
import com.jiepier.filemanager.widget.BoomView;
import com.jiepier.filemanager.widget.ProgressWheel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryFragment extends BaseFragment implements MemoryContact.View, AccessibilityManager.AccessibilityStateChangeListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.cleanView)
    BoomView mCleanView;
    @BindView(R.id.memory_Progressbar)
    ProgressWheel memoryProgressbar;
    private MemoryAdapter mAdapter;
    private MemoryPresenter mPresenter;
    private boolean mAccessibilityEnable;
    private AccessibilityManager mAccessibilityManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_memory;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mAdapter = new MemoryAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        //cleanView.setVisibility(View.VISIBLE);
        mPresenter = new MemoryPresenter(getContext());
        mPresenter.attachView(this);
    }

    @Override
    protected void initData() {

        mPresenter.getRunningAppInfo();
        mAccessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        mAccessibilityManager.addAccessibilityStateChangeListener(this);
    }

    @Override
    protected void initListeners() {

        mCleanView.setViewClickListener(() -> {
            mCleanView.startAnimation();
        });

        mCleanView.setAnimatorListener(new BoomView.OnAnimatorListener() {
            @Override
            public void onAnimationEnd() {
                //辅助功能不可用，则开启普通杀
               /* if (!mAccessibilityEnable) {
                    mPresenter.killRunningAppInfo(mAdapter.getChooseSet());
                } else {*/
                //开启辅助杀
                MemoryAccessibilityManager.getInstance(getContext()).startTask(mAdapter.getChooseSet());
                // }
            }
        });
    }

    @Override
    public void showLoadingView() {
        memoryProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dimissLoadingView() {
        memoryProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void notifityItem() {
        mAdapter.notifityItem();
    }

    @Override
    public void showBoomView() {
        mCleanView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMemoryClean(String content) {
        showToast(content);
    }

    @Override
    public void setData(List<AppProcessInfo> list) {
        mAdapter.add(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mAccessibilityManager.removeAccessibilityStateChangeListener(this);
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        mAccessibilityEnable = enabled;
        Loger.w("ruijie", "current accessobility is " + enabled);
    }
}
