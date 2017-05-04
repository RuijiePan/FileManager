package com.jiepier.filemanager.ui.category.memory.accessibility;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jiepier.filemanager.R;

/**
 * Created by panruijie on 2017/4/6.
 * Email : zquprj@gmail.com
 */

public class AccessibilityGuideFloatViewLayout extends RelativeLayout {

    private View mContentView;
    private ImageView mGuideCloseView;
    private Context mContext;
    private int mAlpha = 255;

    public AccessibilityGuideFloatViewLayout(Context context) {
        this(context, null);
    }

    public AccessibilityGuideFloatViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccessibilityGuideFloatViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context.getApplicationContext();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = findViewById(R.id.guide_float_window_layout);
        mGuideCloseView = (ImageView) findViewById(R.id.guide_float_window_close_image);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mAlpha < 255) {
            int saveLayerAlpha = canvas.saveLayerAlpha(getLeft(), getTop(),
                    getRight(), getBottom(), mAlpha, Canvas.ALL_SAVE_FLAG);
            super.draw(canvas);
            canvas.restoreToCount(saveLayerAlpha);
        } else {
            super.draw(canvas);
        }
    }

    public void fadeIn() {
//        ObjectAnimator anim = ObjectAnimator.ofFloat(getZoneView(), "alpha", 0, 1);
//        anim.setDuration(5000);
//        anim.start();
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(getContentView(), "scaleX", 0.13f, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(getContentView(), "scaleY", 0.15f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.playTogether(scaleXAnim, scaleYAnim);
        animatorSet.start();
    }

    public void fadeOut(Animator.AnimatorListener animatorListener) {
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(getContentView(), "scaleX", 0.13f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(getContentView(), "scaleY", 0.15f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(animatorListener);
        animatorSet.setDuration(400);
        animatorSet.playTogether(scaleXAnim, scaleYAnim);
        animatorSet.start();
    }

    public View getContentView() {
        return mContentView;
    }

    public View getCloseView() {
        return mGuideCloseView;
    }
}
