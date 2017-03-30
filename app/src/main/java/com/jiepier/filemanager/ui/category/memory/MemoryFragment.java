package com.jiepier.filemanager.ui.category.memory;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.widget.BoomView;
import com.jiepier.filemanager.widget.ProgressWheel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryFragment extends BaseFragment implements MemoryContact.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.cleanView)
    BoomView cleanView;
    @BindView(R.id.memory_Progressbar)
    ProgressWheel memoryProgressbar;
    private MemoryAdapter mAdapter;
    private MemoryPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_memory;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mAdapter = new MemoryAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        //cleanView.setVisibility(View.VISIBLE);
        mPresenter = new MemoryPresenter(getContext());
        mPresenter.attachView(this);
    }

    @Override
    protected void initData() {

        mPresenter.getRunningAppInfo();

    }

    @Override
    protected void initListeners() {

        mAdapter.setOnCheckBoxClickListener(new MemoryAdapter.OnCheckBoxClickListener() {
            @Override
            public void check(int position) {

            }

            @Override
            public void unCheck(int position) {

            }
        });

        cleanView.setViewClickListener(() -> {
            cleanView.startAnimation();
        });

        cleanView.setAnimatorListener(new BoomView.OnAnimatorListener() {
            @Override
            public void onAnimationEnd() {
                mPresenter.killRunningAppInfo(mAdapter.getChooseSet());
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
        cleanView.setVisibility(View.VISIBLE);
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
    }

}
