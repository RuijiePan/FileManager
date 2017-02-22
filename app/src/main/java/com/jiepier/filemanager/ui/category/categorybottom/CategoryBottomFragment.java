package com.jiepier.filemanager.ui.category.categorybottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.event.ActionMutipeChoiceEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 * category底部doc、zip、apk三个fragment总和类
 * viedo模块也包括
 */

public class CategoryBottomFragment extends BaseFragment implements CategoryBottomContact.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private CategoryBottomPresenter mPresenter;
    private CategoryBottomAdapter mAdapter;
    private MaterialDialog mDialog;
    private int mIndex;

    public static BaseFragment newInstance(int index) {
        BaseFragment instance = new CategoryBottomFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstant.INDEX, index);
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
        mDialog = new MaterialDialog.Builder(getContext())
                .content(R.string.loading)
                .progress(true, 0)
                .build();
    }

    @Override
    protected void initData() {

        mAdapter = new CategoryBottomAdapter(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new CategoryBottomPresenter(getContext());
        mPresenter.attachView(this);
        mPresenter.setIndex(mIndex);
    }

    @Override
    protected void initListeners() {

        mAdapter.setItemClickListner(new BaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mPresenter.onItemClick(mAdapter.getData(position));
            }

            @Override
            public void onMultipeChoice(List<String> items) {
                RxBus.getDefault().post(new ActionMutipeChoiceEvent(items));
            }

            @Override
            public void onMultipeChoiceStart() {
                //ToastUtil.showToast(getContext(),"进入多选模式");
            }

            @Override
            public void onMultipeChoiceCancel() {
                ToastUtil.showToast(getContext(), "退出多选模式");
            }
        });
    }

    @Override
    public void showDialog() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void dimissDialog() {
        if (mDialog.isShowing()) {
            mDialog.cancel();
        }
    }

    @Override
    public void setData(ArrayList<String> list) {
        mAdapter.setData(list);
    }

    @Override
    public void selectAll() {
        if (mAdapter.getSelectedItemCount() == mAdapter.getItemCount()) {
            mAdapter.clearSelections();
            mAdapter.isLongClick(false);
            RxBus.getDefault().post(new ActionMutipeChoiceEvent(mAdapter.getSelectedFilesPath()));
        } else {
            mAdapter.setAllSelections();
        }
    }

    @Override
    public void clearSelect() {
        mAdapter.clearSelections();
    }

    @Override
    public void setDataByObservable(Observable<ArrayList<String>> observable) {
        observable.subscribe(data -> {
            mAdapter.setData(data);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
