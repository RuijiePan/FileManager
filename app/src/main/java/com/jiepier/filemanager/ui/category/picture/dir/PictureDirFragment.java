package com.jiepier.filemanager.ui.category.picture.dir;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.widget.DividerGridItemDecoration;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by panruijie on 17/1/19.
 * Email : zquprj@gmail.com
 */

public class PictureDirFragment extends BaseFragment implements PictureDirContact.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MaterialDialog mDialog;
    private PictureDirPresenter mPresenter;
    private PictureDirAdapter mAdapter;
    private String dirPath;

    public static PictureDirFragment newInstance(String dirPath) {
        PictureDirFragment instance = new PictureDirFragment();
        Bundle args = new Bundle();
        args.putString("dirPath", dirPath);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dirPath = getArguments() != null ? getArguments().getString("dirPath") :
                Environment.getExternalStorageDirectory().getPath() + File.separator + "DCIM";
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

        mAdapter = new PictureDirAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        recyclerView.setAdapter(mAdapter);

        mPresenter = new PictureDirPresenter(dirPath);
        mPresenter.attachView(this);
        mPresenter.getData();
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void showDialog() {
        mDialog.show();
    }

    @Override
    public void dimissDialog() {
        mDialog.dismiss();
    }

    @Override
    public void setTotalCount(int count) {

    }

    @Override
    public void setDataUsingObservable(Observable<List<String>> observable) {

        observable.subscribe(list -> {
            mAdapter.setData(list);
        }, Throwable::printStackTrace);
    }

}
