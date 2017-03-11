package com.jiepier.filemanager.ui.category.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.bean.JunkGroup;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.entity.MultiItemEntity;
import com.jiepier.filemanager.widget.BoomView;
import com.jiepier.filemanager.widget.DustbinView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by panruijie on 2017/2/19.
 * Email : zquprj@gmail.com
 */

public class StorageFragment extends BaseFragment implements StorageContact.View {

    @BindView(R.id.junk_listview)
    ExpandableListView mListView;
    @BindView(R.id.cleanView)
    BoomView mCleanView;
    @BindView(R.id.dustbinView)
    DustbinView mDustbinView;
    private TextView mTvProgress;
    private TextView mTvTotalSize;
    private StoragePresenter mPresnter;
    private StorageExpandAdapter mAdapter;
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
    protected void initData() {
        mPresnter = new StoragePresenter(getContext());
        mPresnter.attachView(this);
        mPresnter.startScanTask();
        mPresnter.initAdapterData();

    }

    @Override
    protected void initListeners() {
        mCleanView.setViewClickListener(() -> {
            mCleanView.startAnimation();
            mPresnter.startCleanTask(mAdapter.getData());
        });

    }

    @Override
    public void setAdapterData(List<MultiItemEntity> data) {

        mAdapter = new StorageExpandAdapter(getContext(), data);
        mListView.setGroupIndicator(null);
        mListView.setChildIndicator(null);
        mListView.setDividerHeight(0);
        mListView.setAdapter(mAdapter);
        mListView.addHeaderView(mHeadView);
        mTvProgress = (TextView) mHeadView.findViewById(R.id.tv_progress);
        mTvTotalSize = (TextView) mHeadView.findViewById(R.id.tv_total_size);
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
        mTvProgress.setText(junk.getPath());
    }

    @Override
    public void setData(JunkGroup junkGroup) {
        mAdapter.setData(junkGroup);
        //刷新数据
        mCleanView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTotalJunk(String junkSize) {

        mTvTotalSize.setText(junkSize);
    }

    @Override
    public void groupClick(boolean isExpand, int position) {
        //如果原本没展开，那么展开
        if (!isExpand) {
            mListView.expandGroup(position);
        } else {
            mListView.collapseGroup(position);
        }
    }

    @Override
    public void setItemTotalJunk(int index, String JunkSize) {

        mAdapter.setItemTotalSize(index, JunkSize);
    }

    @Override
    public void cleanFinish() {
        mDustbinView.setVisibility(View.VISIBLE);
        mDustbinView.startAnimation();
    }

    @Override
    public void cleanFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresnter.detachView();
    }

}
