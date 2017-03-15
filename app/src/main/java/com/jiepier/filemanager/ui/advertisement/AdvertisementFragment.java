package com.jiepier.filemanager.ui.advertisement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.widget.DustbinDialog;

import butterknife.BindView;


/**
 * Created by JiePier on 16/12/7.
 */

public class AdvertisementFragment extends BaseFragment {

    DustbinDialog mDialog;
    @BindView(R.id.button)
    Button button;

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
        button.setOnClickListener(v -> {
            mDialog = new DustbinDialog(getContext());
            mDialog.show();
        });

    }

}
