package com.jiepier.filemanager.ui.category.memory;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryFragment extends BaseFragment implements MemoryContact.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.memory_Progressbar)
    ContentLoadingProgressBar memoryProgressbar;
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

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
    }

    @Override
    public void showLoadingView() {
        memoryProgressbar.show();
    }

    @Override
    public void dimissLoadingView() {
        memoryProgressbar.hide();
    }

    @Override
    public void showMemoryClean(long memory) {
        ToastUtil.showToast(getContext(), memory / 1024 / 1024 + "");
    }

    @Override
    public void setData(List<AppProcessInfo> list) {
        mAdapter.add(list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
