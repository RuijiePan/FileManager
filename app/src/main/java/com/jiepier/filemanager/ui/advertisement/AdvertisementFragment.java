package com.jiepier.filemanager.ui.advertisement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;

import butterknife.BindView;


/**
 * Created by JiePier on 16/12/7.
 */

public class AdvertisementFragment extends BaseFragment {

    @BindView(R.id.native_ad_icon)
    ImageView nativeAdIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {

    }

}
