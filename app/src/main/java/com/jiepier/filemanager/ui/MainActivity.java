package com.jiepier.filemanager.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initUiAndListener() {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }
}
