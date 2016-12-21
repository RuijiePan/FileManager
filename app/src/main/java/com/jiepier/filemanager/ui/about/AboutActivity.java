package com.jiepier.filemanager.ui.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/12/14.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int initContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.settings);
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
