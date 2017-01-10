package com.jiepier.filemanager.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.BitmapUtil;
import com.jiepier.filemanager.util.ThemeUtil;

/**
 * Created by JiePier on 16/12/15.
 */

public class BoomView extends View {

    private String TAG = getClass().getSimpleName();
    private static final int ROUND_WIDTH = 30;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private int mWidth;
    private int mHeight;
    private int mDx;
    private int mDy;
    private boolean isTouchView;
    private Paint mPaint;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private OnViewClickListener mListener;
    private OnAnimatorListener mAnimatorListener;

    public BoomView(Context context) {
        this(context,null);
    }

    public BoomView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public BoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initBitmap(context,attrs);
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);

        //动态获取?attr/colorPrimary
        //在xml获取需要api>21以上
        int themeColor = ThemeUtil.getThemeColor(getContext());
        mPaint.setColor(themeColor);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
        //圆形背景色作为dest，火箭作为src，src的绘画范围不超过dest
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    }

    private void initBitmap(Context context,AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ThemeColorIconView);
        Drawable icon = ta.getDrawable(R.styleable.ThemeColorIconView_icon);
        ta.recycle();

        if (icon == null)
            mBitmap = BitmapUtil.getBitmapFromRes(getContext(),R.drawable.airplane);
        else
            mBitmap = BitmapUtil.getBitmapFromDrawable(icon);

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //将画布转移到右边中间，因为gravity失效了
        canvas.translate(mWidth/2-mBitmapWidth/2,mHeight/10*9 - mBitmapHeight/2 - ROUND_WIDTH);
        canvas.drawCircle(mBitmapWidth/2,mBitmapHeight/2,mBitmapWidth/2 + ROUND_WIDTH,mPaint);
        //因为画布不会影响前面的操作，移动matrix的话，绘画完还要移动回去。故直接移动画布
        canvas.translate(mDx,mDy);
        //mMatrix.postTranslate(mDx,mDy);
        canvas.drawBitmap(mBitmap,mMatrix,mBitmapPaint);
        //mMatrix.postTranslate(-mDx,-mDy);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                //Log.w(TAG,"DOWN");
                isTouchView = checkPoint(x, y);
                return isTouchView;
            case MotionEvent.ACTION_MOVE:
                //Log.w(TAG,"MOVE");
                if (!isTouchView)
                    return true;
                break;
            case MotionEvent.ACTION_UP:
                if (mListener!=null && isTouchView && checkPoint(x,y))
                    mListener.onClick();
                return true;
        }

        return super.onTouchEvent(event);
    }

    private boolean checkPoint(int x, int y) {

        if (x < mWidth/2-mBitmapWidth/2)
            return false;
        else if (x > mWidth/2+mBitmapWidth/2)
            return false;
        else if (y < mHeight/10*9 - mBitmapHeight/2 - ROUND_WIDTH)
            return false;
        else if (y > mHeight/10*9 + mBitmapHeight/2 + ROUND_WIDTH)
            return false;
        return true;
    }

    public void setViewClickListener(OnViewClickListener listener) {
        this.mListener = listener;
    }

    public void setAnimatorListener(OnAnimatorListener listener) {
        this.mAnimatorListener = listener;
    }

    public interface OnViewClickListener{

        void onClick();
    }

    public interface OnAnimatorListener{

        void onAnimationEnd();
    }

    public void startAnimation(){

        //上下振动动画
        ObjectAnimator repeatAnimator = ObjectAnimator.ofFloat(this,"amplitudeUp",0.05f,-0.05f);
        repeatAnimator.setInterpolator(new LinearInterpolator());
        repeatAnimator.setRepeatCount(20);
        repeatAnimator.setDuration(100);
        repeatAnimator.addUpdateListener(animation -> {
            mDx = (int) ((Float) animation.getAnimatedValue() * mBitmapWidth);
            mDy = -(int) ((Float) animation.getAnimatedValue() * mBitmapHeight);
            invalidate();
        });

        //飞出动画
        ObjectAnimator flyUpAnimator = ObjectAnimator.ofFloat(this,"fly",1f);
        flyUpAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        flyUpAnimator.setDuration(500);
        flyUpAnimator.addUpdateListener(animation -> {
            mDx = (int) ((Float) animation.getAnimatedValue() * mBitmapWidth);
            mDy = -(int) ((Float) animation.getAnimatedValue() * mBitmapHeight);
            invalidate();
        });

        //飞回动画，添加动画结束回调
        ObjectAnimator flyDownAnimator = ObjectAnimator.ofFloat(this,"fly",1f,0);
        flyDownAnimator.setInterpolator(new DecelerateInterpolator());
        flyDownAnimator.setDuration(500);
        flyDownAnimator.addUpdateListener(animation -> {
            mDx = (int) ((Float) animation.getAnimatedValue() * mBitmapWidth);
            mDy = -(int) ((Float) animation.getAnimatedValue() * mBitmapHeight);
            invalidate();
        });
        flyDownAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mAnimatorListener != null)
                    mAnimatorListener.onAnimationEnd();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(repeatAnimator)
                .before(flyUpAnimator)
                .before(flyDownAnimator);
        animatorSet.start();
    }

}
