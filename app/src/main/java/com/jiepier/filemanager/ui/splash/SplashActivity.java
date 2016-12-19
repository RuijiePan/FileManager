package com.jiepier.filemanager.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.ui.main.MainActivity;
import com.jiepier.filemanager.util.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/12/19.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    public int initContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    public void initUiAndListener() {

        Animation scale = AnimationUtils.loadAnimation(this,R.anim.scale_small);
        imageView.startAnimation(scale);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        ToastUtil.showToast(this, "请给予读写权限，否则无法使用");
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        ToastUtil.showToast(this, "您需要去APP设置手动给予权限");
                    }
                });

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

}
