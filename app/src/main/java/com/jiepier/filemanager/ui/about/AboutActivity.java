package com.jiepier.filemanager.ui.about;

import android.os.Bundle;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;

/**
 * Created by JiePier on 16/12/14.
 */

public class AboutActivity extends BaseActivity {

    @Override
    public int initContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    public void initUiAndListener() {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }
}
