package com.jiepier.filemanager.ui.category.memory.accessibility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.DeviceUtils;
import com.jiepier.filemanager.R;

/**
 * Created by panruijie on 2017/4/6.
 * Email : zquprj@gmail.com
 * 辅助功能引导视图
 */

public class AccessibilityGuideFloatView implements View.OnClickListener {

    private Context mContext;
    private boolean mIsShowFloatView = false;
    private RelativeLayout mContentView;
    private AccessibilityGuideFloatViewLayout mFloatViewLayout;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public AccessibilityGuideFloatView(Context context) {
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        initWindowLayout();
        initWindowView();
    }

    private void initWindowLayout() {
        int windowType = DeviceUtils.getSDKVersion() >= 19 ?
                WindowManager.LayoutParams.TYPE_TOAST :
                WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, windowType,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        mLayoutParams.gravity = Gravity.CENTER;
    }

    private void initWindowView() {
        mContentView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.accessibility_guide_float_window_layout, null);
        mContentView.setFocusableInTouchMode(true);
        mFloatViewLayout = (AccessibilityGuideFloatViewLayout) mContentView.findViewById(R.id.guide_float_window_layout);
        mFloatViewLayout.getCloseView().setOnClickListener(this);
        mContentView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dimiss();
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        dimiss();
    }

    public void show() {
        if (!mIsShowFloatView) {
            mIsShowFloatView = true;
            mWindowManager.addView(mContentView, mLayoutParams);
            mFloatViewLayout.fadeIn();
        }
    }

    private void dimiss() {
        if (mIsShowFloatView) {
            mIsShowFloatView = false;
            mFloatViewLayout.fadeOut(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mWindowManager.removeView(mContentView);
                }
            });
        }
    }

}
