package com.jiepier.filemanager.ui.category.storage;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by panruijie on 2017/2/19.
 * Email : zquprj@gmail.com
 */

public class StorageActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int initContentView() {
        return R.layout.activity_storage;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.junk);
    }

    @Override
    public void initUiAndListener() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new StorageFragment())
                .commit();
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
