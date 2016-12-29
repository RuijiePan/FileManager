package com.jiepier.filemanager.ui.category;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseLazyFragment;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.util.SortUtil;
import com.jiepier.filemanager.widget.PowerProgressBar;
import com.jiepier.filemanager.widget.ThemeColorIconView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JiePier on 16/12/7.
 */

public class FileCategoryFragment extends BaseLazyFragment {

    private ScannerReceiver mScannerReceiver;
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
    @BindView(R.id.item_apk)
    ThemeColorIconView itemApk;
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

        mScannerReceiver = new ScannerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addDataScheme("file");
        getActivity().registerReceiver(mScannerReceiver, intentFilter);

        CategoryManager.init(getContext());
        CategoryManager.getInstance()
                .setSortMethod(SortUtil.SortMethod.SIZE);

        List<String> list = CategoryManager.getInstance().getApkList();
        Log.w("haha",list.toString());
        Log.w("haha","================================");

        List<String> list2 = CategoryManager.getInstance().getDocList();
        Log.w("haha",list2.toString());
        Log.w("haha","================================");

        List<String> list3 = CategoryManager.getInstance().getZipList();
        Log.w("haha",list3.toString());
    }

    @Override
    protected void onShow() {
        memoryProgressbar.startAnimation();
        storageProgressbar.startAnimation();
    }

    @Override
    protected void onHide() {

    }

    @OnClick({R.id.memoryProgressbar, R.id.storageProgressbar, R.id.item_music, R.id.item_video, R.id.item_picture, R.id.item_document, R.id.item_zip, R.id.item_apk})
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
            case R.id.item_apk:
                break;
        }
    }

    private class ScannerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.v(LOG_TAG, "received broadcast: " + action.toString());
            // handle intents related to external storage
            if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED) || action.equals(Intent.ACTION_MEDIA_MOUNTED)
                    || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
                //notifyFileChanged();
            }
        }
    }
}
