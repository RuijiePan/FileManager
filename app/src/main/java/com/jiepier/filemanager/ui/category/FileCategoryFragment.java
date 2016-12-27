package com.jiepier.filemanager.ui.category;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseLazyFragment;
import com.jiepier.filemanager.widget.PowerProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/12/7.
 */

public class FileCategoryFragment extends BaseLazyFragment {

    @BindView(R.id.memoryProgressbar)
    PowerProgressBar memoryProgressbar;
    @BindView(R.id.storageProgressbar)
    PowerProgressBar storageProgressbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file_manager;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        memoryProgressbar.setStartAngle(45);
        storageProgressbar.setStartAngle(45);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onShow() {
        memoryProgressbar.startAnimation();
        storageProgressbar.startAnimation();
    }

    @Override
    protected void onHide() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
