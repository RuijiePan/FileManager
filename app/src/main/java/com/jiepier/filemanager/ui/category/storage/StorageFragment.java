package com.jiepier.filemanager.ui.category.storage;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.JunkProcessInfo;
import com.jiepier.filemanager.widget.BoomView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by panruijie on 2017/2/19.
 * Email : zquprj@gmail.com
 */

public class StorageFragment extends BaseFragment implements StorageContact.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.cleanView)
    BoomView mCleanView;
    private TextView mTvProgress;
    private TextView mTvTotalSize;
    private StoragePresenter mPresnter;
    private StorageAdapter mAdapter;
    private View mHeadView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_storage;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mHeadView = inflater.inflate(R.layout.fragment_storage_head, null, false);
    }

    @Override
    protected void initListeners() {

        mPresnter = new StoragePresenter(getContext());
        mPresnter.attachView(this);
        mPresnter.startScanTask();
        mPresnter.initAdapterData();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setAdapterData(List<MultiItemEntity> data) {
        mAdapter = new StorageAdapter(data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setHeaderView(mHeadView);
        mTvProgress = (TextView) mAdapter.getHeaderLayout().findViewById(R.id.tv_progress);
        mTvTotalSize = (TextView) mAdapter.getHeaderLayout().findViewById(R.id.tv_total_size);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDialog() {
        mAdapter.showItemDialog();
    }

    @Override
    public void dimissDialog(int index) {
        mAdapter.dismissItemDialog(index);
    }

    @Override
    public void setCurrenOverScanJunk(JunkInfo junk) {

        mTvProgress.setText(junk.getPath());
    }

    @Override
    public void setCurrenSysCacheScanJunk(JunkInfo junk) {

    }

    @Override
    public void setData(HashMap<Integer, ArrayList<JunkProcessInfo>> hashMap) {
        mAdapter.setData(hashMap);
        mCleanView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTotalJunk(String junkSize) {

        mTvTotalSize.setText(junkSize);
    }

    @Override
    public void setRunningAppData(ArrayList<AppProcessInfo> list) {

    }

    @Override
    public void setItemTotalJunk(int index, String JunkSize) {

        mAdapter.setItemTotalSize(index, JunkSize);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresnter.detachView();
    }

}
