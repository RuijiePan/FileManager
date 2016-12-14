package com.jiepier.filemanager.ui.sdcard;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.ui.common.BrowserListAdapter;
import com.jiepier.filemanager.util.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JiePier on 16/12/13.
 */

public class ContentFragment extends BaseFragment {

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
    private BrowserListAdapter mAdapter;

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
        mAdapter = new BrowserListAdapter();
        //Log.w("haha",Settings.getDefaultDir());
        mAdapter.addFiles(Settings.getDefaultDir());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.fab_scoll_top, R.id.fab_create_file, R.id.fab_create_floder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_scoll_top:
                recyclerView.smoothScrollToPosition(0);
                floatingMenu.close(true);
                break;
            case R.id.fab_create_file:
                break;
            case R.id.fab_create_floder:
                break;
        }
    }
}
