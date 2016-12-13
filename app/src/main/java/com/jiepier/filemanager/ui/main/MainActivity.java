package com.jiepier.filemanager.ui.main;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.base.BaseDrawerActivity;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.ui.category.CategoryFragment;
import com.jiepier.filemanager.util.SnackbarUtil;
import com.jiepier.filemanager.util.ToastUtils;

public class MainActivity extends BaseDrawerActivity {

    @Override
    protected BaseFragment setFragment() {
        return new CategoryFragment();
    }

    @Override
    public void initUiAndListener() {
        super.initUiAndListener();

        setNavigationClickListener(new NavigationClickListener() {
            @Override
            public void onClickSDCard() {

            }

            @Override
            public void onClickRoot() {

            }

            @Override
            public void onClickSystem() {

            }

            @Override
            public void onClickSetting() {

            }

            @Override
            public void onClickAbout() {

            }
        });
    }
}
