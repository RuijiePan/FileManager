package com.jiepier.filemanager.ui.category.music;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.constant.AppConstant;
import com.jiepier.filemanager.ui.actionmode.ActionModeActivity;

import butterknife.BindView;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class MusicActivity extends ActionModeActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int initContentView() {
        return R.layout.activity_music_manager;
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
    public void init() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,new MusicFragment())
                .commit();

        setTitle(AppConstant.MUSIC);
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
