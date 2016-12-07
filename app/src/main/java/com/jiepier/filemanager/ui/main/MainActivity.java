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
            public void onClickLocal() {
                ToastUtils.show(MainActivity.this,"local",1000);
            }

            @Override
            public void onClickStar() {
                ToastUtils.show(MainActivity.this,"star",1000);
            }

            @Override
            public void onClickSetting() {
                ToastUtils.show(MainActivity.this,"setting",1000);
            }

            @Override
            public void onClickAbout() {
                ToastUtils.show(MainActivity.this,"about",1000);
            }
        });
    }
}
