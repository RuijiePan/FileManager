package com.jiepier.filemanager.ui;

import android.os.Bundle;
import android.util.Log;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.widget.WaveSideBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by panruijie on 17/1/6.
 * Email : zquprj@gmail.com
 */

public class WaveActivity extends BaseActivity {

    @BindView(R.id.waveSideBarView)
    WaveSideBarView waveSideBarView;

    @Override
    public int initContentView() {
        return R.layout.activity_wave;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    public void initUiAndListener() {
        waveSideBarView.setOnTouchLetterChangeListener(letter -> Log.w(TAG,letter));
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
