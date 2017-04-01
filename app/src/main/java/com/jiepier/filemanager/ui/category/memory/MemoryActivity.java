package com.jiepier.filemanager.ui.category.memory;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.accessibility.AccessibilityManager;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryActivity extends BaseActivity {

    private AccessibilityManager mAccessibilityManager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int initContentView() {
        return R.layout.activity_memory_manager;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.memory);
    }

    @Override
    public void initUiAndListener() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new MemoryFragment())
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
