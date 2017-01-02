package com.jiepier.filemanager.ui.category.categorybottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.manager.ApkManager;
import com.jiepier.filemanager.ui.common.CommonFragment;
import com.jiepier.filemanager.util.Settings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class CategoryBottomFragment extends BaseFragment implements CategoryBottomContact.View{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private CategoryBottomPresenter mPresenter;
    private CategoryBottomAdapter mAdapter;
    private int mIndex;

    public static BaseFragment newInstance(int index){
        BaseFragment instance = new CategoryBottomFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstant.INDEX,index);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndex = getArguments() != null ? getArguments().getInt(AppConstant.INDEX) : AppConstant.APK_INDEX;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_item;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

        mAdapter = new CategoryBottomAdapter(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        mPresenter = new CategoryBottomPresenter(getContext());
        mPresenter.attachView(this);
        mPresenter.setIndex(mIndex);
    }

    @Override
    protected void initListeners() {

        mAdapter.setItemClickListner(new BaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onMultipeChoice(List<String> items) {

            }

            @Override
            public void onMultipeChoiceStart() {

            }

            @Override
            public void onMultipeChoiceCancel() {

            }
        });
    }

    @Override
    public void setData(ArrayList<String> list) {
        mAdapter.setData(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
