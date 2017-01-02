package com.jiepier.filemanager.ui.category.categorybottom;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class CategoryBottomActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int initContentView() {
        return R.layout.activity_apk_manager;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void initUiAndListener() {

        int index = getIntent().getIntExtra(AppConstant.INDEX,AppConstant.APK_INDEX);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,CategoryBottomFragment.newInstance(index))
                .commit();


        switch (index){
            case AppConstant.DOC_INDEX:
                setTitle(AppConstant.DOC);
                break;
            case AppConstant.ZIP_INDEX:
                setTitle(AppConstant.ZIP);
                break;
            case AppConstant.APK_INDEX:
                setTitle(AppConstant.APK);
                break;
        }
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
