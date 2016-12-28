package com.jiepier.filemanager.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseLazyFragment;
import com.jiepier.filemanager.widget.PowerProgressBar;
import com.jiepier.filemanager.widget.ThemeColorIconView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JiePier on 16/12/7.
 */

public class FileCategoryFragment extends BaseLazyFragment {

    @BindView(R.id.memoryProgressbar)
    PowerProgressBar memoryProgressbar;
    @BindView(R.id.storageProgressbar)
    PowerProgressBar storageProgressbar;
    @BindView(R.id.item_music)
    ThemeColorIconView itemMusic;
    @BindView(R.id.item_video)
    ThemeColorIconView itemVideo;
    @BindView(R.id.item_picture)
    ThemeColorIconView itemPicture;
    @BindView(R.id.item_document)
    ThemeColorIconView itemDocument;
    @BindView(R.id.item_zip)
    ThemeColorIconView itemZip;
    @BindView(R.id.item_download)
    ThemeColorIconView itemDownload;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file_manager;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

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

    @OnClick({R.id.memoryProgressbar, R.id.storageProgressbar, R.id.item_music, R.id.item_video, R.id.item_picture, R.id.item_document, R.id.item_zip, R.id.item_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.memoryProgressbar:
                break;
            case R.id.storageProgressbar:
                break;
            case R.id.item_music:
                break;
            case R.id.item_video:
                break;
            case R.id.item_picture:
                break;
            case R.id.item_document:
                break;
            case R.id.item_zip:
                break;
            case R.id.item_download:
                break;
        }
    }
}
