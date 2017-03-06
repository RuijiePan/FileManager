package com.jiepier.filemanager.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.manager.ProcessManager;
import com.jiepier.filemanager.util.BitmapUtil;

import java.util.List;

/**
 * Created by panruijie on 2017/3/6.
 * Email : zquprj@gmail.com
 * junk清理完成之后显示的垃圾桶view
 */

public class DustbinView extends View {

    private Bitmap mDustbinBitmap;
    private static final int DEFAULT_FADE_TIME = 2000;
    private int mWidth;
    private int mHeight;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private int mDx;
    private int mDy;
    private int mFadeTime = DEFAULT_FADE_TIME;
    private float mFadeIn;
    private float mFadeOut;
    private float mTranslateIn;
    private float mTranslateOut;
    private float mFirstAnimatorProcess;
    private float mSecondAnimatorProcess;
    private Bitmap[] mProcessBitmaps;
    private Matrix mMatirx;
    private AnimatorSet mAnimatorSetF;
    private AnimatorSet mAnimatorSetS;
    private IAnimationListener mListener;

    public DustbinView(Context context) {
        this(context, null);
    }

    public DustbinView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DustbinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDustbinBitmap = BitmapUtil.getBitmapFromRes(context, R.drawable.icon_junk_dustbin);
        mMatirx = new Matrix();
        List<AppProcessInfo> mProcessList = ProcessManager.getInstance().getRunningAppList();
        for (int i = 0; i < mProcessList.size(); i++) {
            mProcessBitmaps[i] = BitmapUtil.getBitmapFromDrawable(
                    BitmapUtil.zoomDrawable(mProcessList.get(i).getIcon(), 30, 30));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.restore();
        canvas.translate(mWidth / 2 - mBitmapWidth / 2, mHeight / 2 - mBitmapHeight / 2);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void startAnimation() {
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(this, "fadeIn", 0, 1f);
        ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(this, "fadeOut", 1f, 0);
        ObjectAnimator translateInAnimator = ObjectAnimator.ofFloat(this, "translateIn", 1f, 0.5f);
        ObjectAnimator translateOutAnimator = ObjectAnimator.ofFloat(this, "translateOut", 0.5, 1f);
        fadeInAnimator.setDuration(mFadeTime);
        fadeOutAnimator.setDuration(mFadeTime);
        translateInAnimator.setDuration(mFadeTime);
        translateOutAnimator.setDuration(mFadeTime);

        //垃圾桶显示
        mAnimatorSetF = new AnimatorSet();
        mAnimatorSetF.play(fadeInAnimator)
                .with(translateInAnimator);
        mAnimatorSetF.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.onDustbinFadeIn();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorSetS.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fadeInAnimator.addUpdateListener(animation -> {
            mFirstAnimatorProcess = (float) animation.getAnimatedValue();
        });
        mAnimatorSetF.start();

        //垃圾桶淡出
        mAnimatorSetS = new AnimatorSet();
        mAnimatorSetS.play(fadeOutAnimator)
                .with(translateOutAnimator);
        mAnimatorSetS.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.onDustbinFadeOut();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fadeOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSecondAnimatorProcess = (float) animation.getAnimatedValue();
            }
        });

    }

    public void setFadeTime(int fadeTime) {
        this.mFadeTime = fadeTime;
    }

    public void setAnimationListener(IAnimationListener mListener) {
        this.mListener = mListener;
    }

    public interface IAnimationListener {

        void onDustbinFadeIn();

        void onProcessRecycler();

        void onDustbinFadeOut();

        void onAnimationFinish();
    }
}
