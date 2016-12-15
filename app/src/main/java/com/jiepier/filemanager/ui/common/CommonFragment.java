package com.jiepier.filemanager.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SnackbarUtil;
import com.jiepier.filemanager.util.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JiePier on 16/12/13.
 */

public class CommonFragment extends BaseFragment implements CommonContact.View{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab_create_file)
    FloatingActionButton fabCreateFile;
    @BindView(R.id.fab_create_floder)
    FloatingActionButton fabCreateFloder;
    @BindView(R.id.floating_menu)
    FloatingActionMenu floatingMenu;
    @BindView(R.id.fab_scoll_top)
    FloatingActionButton fabScollTop;
    private CommonPresenter mPresenter;
    private MaterialDialog mDialog;
    private BrowserListAdapter mAdapter;
    private String path;

    public static CommonFragment newInstance(String path){
        CommonFragment instance = new CommonFragment();
        Bundle args = new Bundle();
        args.putString("path",path);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments() != null ? getArguments().getString("path") : Settings.getDefaultDir();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mDialog = new MaterialDialog
                .Builder(getContext())
                .title("文件")
                .build();
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        mAdapter = new BrowserListAdapter();
        mAdapter.addFiles(path);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListner(position ->
            mPresenter.onItemClick(mAdapter.getData(position))
        );

        mPresenter = new CommonPresenter(getContext());
        mPresenter.attachView(this);
    }

    @OnClick({R.id.fab_scoll_top, R.id.fab_create_file, R.id.fab_create_floder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_scoll_top:
                recyclerView.smoothScrollToPosition(0);
                floatingMenu.close(true);
                break;
            case R.id.fab_create_file:
                floatingMenu.close(true);
                break;
            case R.id.fab_create_floder:
                floatingMenu.close(true);
                break;
        }
    }

    @Override
    public void showSnackbar(String content) {
        SnackbarUtil.show(recyclerView,content,0,null);
    }

    @Override
    public void showDialog() {
        mDialog.show();
    }

    @Override
    public void dismissDialog() {
        mDialog.dismiss();
    }

}
