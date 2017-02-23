package com.jiepier.filemanager.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 仿Go Speed清理快捷方式：火箭上升带流星动画view
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
    private int mMeteorDx;
    private int mMeteorDy;
    private int mCircleRadius;
    private boolean isTouchView;
    private boolean isAnimation;
    private Paint mPaint;
    private Paint mMeteorPaint;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    //private Path mCirclePath;
    private List<Meteor> mMeteorList;
    private OnViewClickListener mListener;
    private OnAnimatorListener mAnimatorListener;

    public BoomView(Context context) {
        this(context, null);
    }

    public BoomView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public BoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initBitmap(context, attrs);
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);

        //动态获取?attr/colorPrimary
        //在xml获取需要api>21以上
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        mPaint.setColor(typedValue.data);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
        //圆形背景色作为dest，火箭作为src，src的绘画范围不超过dest
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        mMeteorPaint = new Paint();
        mMeteorPaint.setAntiAlias(true);
        mMeteorPaint.setDither(true);
        mMeteorPaint.setColor(Color.WHITE);
        mMeteorPaint.setFilterBitmap(true);
        mMeteorPaint.setStrokeWidth(5);
        mMeteorPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    }

    private void initBitmap(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ThemeColorIconView);
        Drawable icon = ta.getDrawable(R.styleable.ThemeColorIconView_icon);
        ta.recycle();

        if (icon == null) {
            mBitmap = BitmapUtil.getBitmapFromRes(getContext(), R.drawable.airplane);
        } else {
            mBitmap = BitmapUtil.getBitmapFromDrawable(icon);
        }

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        mMatrix = new Matrix();
        mCircleRadius = mBitmapWidth / 2 + ROUND_WIDTH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //将画布转移到底部中间，因为gravity失效了
        canvas.translate(mWidth / 2 - mBitmapWidth / 2 - ROUND_WIDTH, mHeight / 10 * 9 - mBitmapHeight / 2);

        //创建圆形、火箭、流星雨混合bitmap。圆形作为dest，在此基础上叠加蒙板
        canvas.drawBitmap(cretaeComBitmap(), 0, 0, mPaint);

        //以下是裁剪出区域再进行绘图，但是圆形会有锯齿。故不采用
        //裁剪出圆形区域,
        //canvas.clipPath(mCirclePath);
        /*canvas.save();
        canvas.translate(-ROUND_WIDTH,-ROUND_WIDTH);
        canvas.drawBitmap(mCircleBitamp,0,0,mPaint);
        canvas.restore();
        //画圆
        canvas.drawCircle(mBitmapWidth/2,mBitmapHeight/2,mCircleRadius,mPaint);*/

        //在动画的时候，画流星
        /*if (isAnimation) {
            canvas.save();
            canvas.translate(mMeteorDx, mMeteorDy);
            for (Meteor mMeteor : mMeteorList) {
                canvas.drawLine(mMeteor.getPointStart().x
                        , mMeteor.getPointStart().y
                        , mMeteor.getPointEnd().x
                        , mMeteor.getPointEnd().y
                        , mMeteorPaint);
            }
            canvas.restore();
        }*/

        /*canvas.save();
        //画火箭
        //因为画布不会影响前面的操作，移动matrix的话，绘画完还要移动回去。故直接移动画布
        canvas.translate(mDx,mDy);
        canvas.drawBitmap(mBitmap,mMatrix,mBitmapPaint);
        canvas.restore();*/

    }

    public Bitmap cretaeComBitmap() {

        Bitmap output = Bitmap.createBitmap(mCircleRadius * 2, mCircleRadius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.translate(ROUND_WIDTH, ROUND_WIDTH);
        //画圆
        canvas.drawCircle(mBitmapWidth / 2, mBitmapHeight / 2, mCircleRadius, mPaint);

        //画火箭
        mMatrix.postTranslate(mDx, mDy);
        canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
        mMatrix.postTranslate(-mDx, -mDy);

        //画流星
        if (isAnimation) {
            canvas.save();
            canvas.translate(mMeteorDx, mMeteorDy);
            for (Meteor mMeteor : mMeteorList) {
                canvas.drawLine(mMeteor.getPointStart().x
                        , mMeteor.getPointStart().y
                        , mMeteor.getPointEnd().x
                        , mMeteor.getPointEnd().y
                        , mMeteorPaint);
            }
            canvas.restore();
        }

        return output;
    }

    class Meteor{
        PointF pointStart;
        PointF pointEnd;

        public PointF getPointStart() {
            return pointStart;
        }

        public Meteor setPointStart(PointF pointStart) {
            this.pointStart = pointStart;
            return this;
        }

        public PointF getPointEnd() {
            return pointEnd;
        }

        public Meteor setPointEnd(PointF pointEnd) {
            this.pointEnd = pointEnd;
            return this;
        }
    }

    private void createMeteors() {
        Random randRom = new Random();

        mMeteorList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int dy = -randRom.nextInt(mCircleRadius * 2);
            int dx = randRom.nextInt(50) + 50;
            int startX = randRom.nextInt(mCircleRadius * 25);
            int startY = - (startX + dy);
            int endX = startX + dx;
            int endY = - (endX + dy);
            PointF pointStart = new PointF(startX, startY);
            PointF pointEnd = new PointF(endX, endY);

            /*Log.w(TAG,"=================");
            Log.w(TAG,"dy="+dy);
            Log.w(TAG,"sX="+startX+",sY="+startY);
            Log.w(TAG,"eX="+endX+",eY="+endY);
            Log.w(TAG,"=================");*/

            Meteor meteor = new Meteor();
            meteor.setPointStart(pointStart)
                    .setPointEnd(pointEnd);
            mMeteorList.add(meteor);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        //用于裁剪出圆形区域
/*        mCirclePath = new Path();
        mCirclePath.addCircle(mBitmapWidth/2, mBitmapHeight/2, mCircleRadius, Path.Direction.CW);*/

        //创建流星雨
        createMeteors();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //Log.w(TAG,"DOWN");
                isTouchView = checkPoint(x, y);
                return isTouchView;
            case MotionEvent.ACTION_MOVE:
                //Log.w(TAG,"MOVE");
                if (!isTouchView) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null && isTouchView && checkPoint(x, y)) {
                    mListener.onClick();
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    private boolean checkPoint(int x, int y) {

        if (x < mWidth / 2 - mBitmapWidth / 2) {
            return false;
        } else if (x > mWidth / 2 + mBitmapWidth / 2) {
            return false;
        } else if (y < mHeight / 10 * 9 - mBitmapHeight / 2 - ROUND_WIDTH) {
            return false;
        } else if (y > mHeight / 10 * 9 + mBitmapHeight / 2 + ROUND_WIDTH) {
            return false;
        }
        return true;
    }

    public void setViewClickListener(OnViewClickListener listener) {
        this.mListener = listener;
    }

    public void setAnimatorListener(OnAnimatorListener listener) {
        this.mAnimatorListener = listener;
    }

    public interface OnViewClickListener {

        void onClick();
    }

    public interface OnAnimatorListener {

        void onAnimationEnd();
    }

    public void startAnimation() {

        mMeteorDx = 0;
        mMeteorDy = 0;
        isAnimation = true;

        //上下振动动画
        ObjectAnimator repeatAnimator = ObjectAnimator.ofFloat(this, "amplitudeUp", -0.05f, 0.05f);
        repeatAnimator.setInterpolator(new LinearInterpolator());
        repeatAnimator.setRepeatCount(20);
        repeatAnimator.setDuration(100);
        repeatAnimator.addUpdateListener(animation -> {
            mDx = (int) ((Float) animation.getAnimatedValue() * mBitmapWidth);
            mDy = (int) ((Float) animation.getAnimatedValue() * mBitmapHeight);

            mMeteorDx -= (int) (((Float) animation.getAnimatedValue() + 0.05f) * 4 * mCircleRadius);
            mMeteorDy += (int) (((Float) animation.getAnimatedValue() + 0.05f) * 4 * mCircleRadius);

            /*Log.w(TAG,"====================");
            Log.w(TAG,mMeteorDx + "");
            Log.w(TAG,mMeteorDy + "");
            Log.w(TAG,"====================");*/
            invalidate();
        });

        //飞出动画
        ObjectAnimator flyUpAnimator = ObjectAnimator.ofFloat(this, "fly", 2f);
        flyUpAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        flyUpAnimator.setDuration(500);
        flyUpAnimator.addUpdateListener(animation -> {
            mDx = (int) ((Float) animation.getAnimatedValue() * mBitmapWidth);
            mDy = -(int) ((Float) animation.getAnimatedValue() * mBitmapHeight);
            invalidate();
        });

        //飞回动画，添加动画结束回调
        ObjectAnimator flyDownAnimator = ObjectAnimator.ofFloat(this, "fly", 2f, 0);
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
                if (mAnimatorListener != null) {
                    mAnimatorListener.onAnimationEnd();
                }
                isAnimation = false;
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
