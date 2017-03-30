package com.jiepier.filemanager.ui.appmanager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.bean.AppInfo;
import com.jiepier.filemanager.widget.ProgressWheel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by JiePier on 16/12/7.
 */

public class AppManagerFragment extends BaseFragment implements AppManagerContact.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress)
    ProgressWheel mProgress;
    private AppManagerAdapter mAdapter;
    private AppManagerPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_app_manager;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mPresenter = new AppManagerPresenter(getContext());
        mPresenter.attachView(this);
        mPresenter.getData();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void showDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissDialog() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void setData(List<AppInfo> data) {
        mAdapter = new AppManagerAdapter(getContext(), data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void removeItem(String pckName) {
        mAdapter.removeItem(pckName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
