package com.jiepier.filemanager.ui.common;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.event.AllChoiceEvent;
import com.jiepier.filemanager.event.CleanActionModeEvent;
import com.jiepier.filemanager.event.CleanChoiceEvent;
import com.jiepier.filemanager.event.MutipeChoiceEvent;
import com.jiepier.filemanager.event.RefreshEvent;
import com.jiepier.filemanager.util.RxBus;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SnackbarUtil;
import com.jiepier.filemanager.util.ToastUtil;
import com.jiepier.filemanager.widget.CreateFileDialog;
import com.jiepier.filemanager.widget.CreateFolderDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public static final String DIALOGTAG = "dialog_tag";
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

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        mAdapter = new BrowserListAdapter(getContext());
        mAdapter.addFiles(path);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListner(new BaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mPresenter.onItemClick(mAdapter.getData(position),path);
            }

            @Override
            public void onMultipeChoice(List<String> items) {
                RxBus.getDefault().post(new MutipeChoiceEvent(items));
            }

            @Override
            public void onMultipeChoiceStart() {
                //ToastUtil.showToast(getContext(),"进入多选模式");
            }

            @Override
            public void onMultipeChoiceCancel() {
                ToastUtil.showToast(getContext(),"退出多选模式");
            }
        });

        RxBus.getDefault()
                .toObservable(CleanChoiceEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event->{
                    mAdapter.isLongClick(false);
                    mAdapter.clearSelections();
                }, Throwable::printStackTrace);

        RxBus.getDefault()
                .toObservable(RefreshEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event->{
                    mAdapter.refresh();
                }, Throwable::printStackTrace);

        RxBus.getDefault()
                .toObservable(AllChoiceEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(event -> "/"+event.getPath())
                .subscribe(path->{
                    //如果全选了，就取消。否则全选
                    if (path.equals(this.path)) {
                        if (mAdapter.getItemCount()!=mAdapter.getSelectedItemCount())
                            mAdapter.setAllSelections();
                        else
                            mAdapter.clearSelections();
                    }
                }, Throwable::printStackTrace);

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
                DialogFragment fileDialog = CreateFileDialog.create(path);
                fileDialog.show(getActivity().getFragmentManager(),DIALOGTAG);
                break;
            case R.id.fab_create_floder:
                floatingMenu.close(true);
                DialogFragment folderDialog = CreateFolderDialog.create(path);
                folderDialog.show(getActivity().getFragmentManager(),DIALOGTAG);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            RxBus.getDefault().post(new CleanActionModeEvent());
            RxBus.getDefault().post(new CleanChoiceEvent());
        }
    }

}
