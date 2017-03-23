package com.jiepier.filemanager.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.manager.ProcessManager;
import com.jiepier.filemanager.util.BitmapUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by panruijie on 2017/3/6.
 * Email : zquprj@gmail.com
 * junk清理完成之后显示的垃圾桶view
 */

public class DustbinView extends View {

    private static final int DEFAULT_FADE_TIME = 1000;
    private static final int DEFAULT_RECYCLER_TIME = 1000;
    private static final int DEFAULT_SHAKE_TIME = 300;
    private static final int DEFAULT_SHAKE_DEGREE = 10;
    private static final int DEFAULT_SHAKE_COUNT = 2;
    private int mFadeTime = DEFAULT_FADE_TIME;
    private int mRecyclerTime = DEFAULT_RECYCLER_TIME;
    private int mShakeDrgree = DEFAULT_SHAKE_DEGREE;
    private int mShakeTime = DEFAULT_SHAKE_TIME;
    private int mShakeCount = DEFAULT_SHAKE_COUNT;
    private int mWidth;
    private int mHeight;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private int mDy;
    private int mAlpha;
    private float mFirstAnimatorProcess;
    private float mSecondAnimatorProcess;
    private float mThirdAnimatorProcess;
    private float mFourAnimatorProcess;
    private boolean mIsFisrtAnimationFinish;
    private boolean mIsSecondAnimationFinish;
    private boolean mIsThirdAnimationFinish;
    private boolean mIsFourAnimationFinish;
    private long mAnimationStartTime;
    private Bitmap mDustbinBitmap;
    private IconInfo[] mIconInfos;
    private Matrix mMatrix;
    private Matrix mIconMatrix;
    private Paint mBitmapPaint;
    private Paint[] mProcessPaint;
    private AnimatorSet mAnimatorSetFirst;
    private AnimatorSet mAnimatorSetFour;
    private IAnimationListener mListener;
    private int[] mOtherDrawable = {
            R.drawable.type_unknown, R.drawable.type_note,
            R.drawable.type_config, R.drawable.type_html,
            R.drawable.type_xml, R.drawable.type_note,
            R.drawable.type_pdf, R.drawable.type_package,
            R.drawable.type_pic, R.drawable.type_music,
            R.drawable.type_video, R.drawable.type_apk
    };

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

        //获取垃圾桶参数
        mDustbinBitmap = BitmapUtil.getBitmapFromRes(context, R.drawable.icon_junk_dustbin);
        mBitmapWidth = mDustbinBitmap.getWidth();
        mBitmapHeight = mDustbinBitmap.getHeight();
        mMatrix = new Matrix();
        mIconMatrix = new Matrix();

        //画笔
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        //画垃圾桶
        canvas.translate(mWidth / 2 - mBitmapWidth / 2, mHeight / 2 - mBitmapHeight / 2);
        drawDustbin(canvas);
        canvas.restore();

        drawIcon(canvas);
        if (!mIsFourAnimationFinish) {
            invalidate();
        }
    }

    private void drawDustbin(Canvas canvas) {
        calculatePointAndFade();
        canvas.drawBitmap(mDustbinBitmap, mMatrix, mBitmapPaint);
        mMatrix.reset();
    }

    private void calculatePointAndFade() {

        if (!mIsFisrtAnimationFinish) {
            mDy = (int) ((1.0f - mFirstAnimatorProcess) * mHeight / 2);
            mAlpha = (int) (mFirstAnimatorProcess * 255);
        } else if (!mIsSecondAnimationFinish) {
            mDy = 0;
            mAlpha = 255;
        } else if (!mIsThirdAnimationFinish) {
            mDy = 0;
            mAlpha = 255;
            mMatrix.postTranslate(-mBitmapWidth / 2, -mBitmapHeight * 2 / 3);
            mMatrix.postRotate(mThirdAnimatorProcess * mShakeDrgree);
            mMatrix.postTranslate(mBitmapWidth / 2, mBitmapHeight * 2 / 3);
        } else if (!mIsFourAnimationFinish) {
            mDy = (int) ((1.0f - mFourAnimatorProcess) * mHeight / 2);
            mAlpha = (int) (mFourAnimatorProcess * 255);
        }
        mBitmapPaint.setAlpha(mAlpha);
        mMatrix.postTranslate(0, mDy);
    }

    private void drawIcon(Canvas canvas) {

        if (mIsFisrtAnimationFinish && !mIsSecondAnimationFinish) {
            long currentTime = System.currentTimeMillis();
            int i = -1;
            for (IconInfo info : mIconInfos) {
                i++;
                long time = currentTime - mFadeTime - mAnimationStartTime - info.getDelayTime();
                if (time > 0 && time < mRecyclerTime) {
                    Path path = new Path();
                    path.moveTo(info.startX, info.startY);
                    path.quadTo(info.getControlPoint().x, info.getControlPoint().y, mWidth / 2 + info.getBitmap().getWidth() / 2, mHeight / 2);
                    PathMeasure pathMeasure = new PathMeasure(path, false);

                    info.alpha = 255 - (int) (time * 255 / mRecyclerTime);
                    float[] pos = new float[2];
                    float[] tan = new float[2];
                    pathMeasure.getPosTan(time * pathMeasure.getLength() / mRecyclerTime, pos, tan);
                    info.setX((int) pos[0]);
                    info.setY((int) pos[1]);
                    info.setRotateAngle(Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
                    mProcessPaint[i].setAlpha(info.getAlpha());

                    //先旋转再移动
                    mIconMatrix.postRotate((float) info.getRotateAngle());
                    mIconMatrix.postTranslate(info.getX(), info.getY());
                    canvas.drawBitmap(info.getBitmap(), mIconMatrix, mProcessPaint[i]);
                    //canvas.drawPath(path, mProcessPaint);
                    mIconMatrix.reset();
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void startAnimation() {
        upDateProcessInfo();
        mIsFisrtAnimationFinish = false;
        mIsSecondAnimationFinish = false;
        mIsThirdAnimationFinish = false;
        mIsFourAnimationFinish = false;
        mAnimationStartTime = System.currentTimeMillis();

        ValueAnimator fadeInAnimator = ObjectAnimator.ofFloat(0, 1f);
        ValueAnimator fadeOutAnimator = ObjectAnimator.ofFloat(1f, 0);
        ValueAnimator translateInAnimator = ObjectAnimator.ofFloat(1f, 0);
        ValueAnimator translateOutAnimator = ObjectAnimator.ofFloat(0, 1f);
        ValueAnimator recyclerAnimator = ObjectAnimator.ofFloat(0, 1f);
        ValueAnimator shakeAnimator = ObjectAnimator.ofFloat(0, -1f, 0, 1f, 0);

        fadeInAnimator.setDuration(mFadeTime);
        fadeOutAnimator.setDuration(mFadeTime);
        translateInAnimator.setDuration(mFadeTime);
        translateOutAnimator.setDuration(mFadeTime);
        recyclerAnimator.setDuration(2 * mRecyclerTime);
        shakeAnimator.setDuration(mShakeTime);

        //垃圾桶显示
        mAnimatorSetFirst = new AnimatorSet();
        mAnimatorSetFirst.play(fadeInAnimator)
                .with(translateInAnimator);

        mAnimatorSetFirst.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.onDustbinFadeIn();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsFisrtAnimationFinish = true;
                if (mListener != null) {
                    mListener.onProcessRecycler();
                }
                recyclerAnimator.start();
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

        //icon回收动画
        recyclerAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.onProcessRecycler();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsSecondAnimationFinish = true;
                shakeAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        recyclerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSecondAnimatorProcess = (float) animation.getAnimatedValue();
            }
        });

        //垃圾桶摇晃动画
        shakeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.onShakeDustbin();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsThirdAnimationFinish = true;
                mAnimatorSetFour.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        shakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mThirdAnimatorProcess = (float) animation.getAnimatedValue();
            }
        });
        shakeAnimator.setRepeatCount(mShakeCount);

        //垃圾桶淡出
        mAnimatorSetFour = new AnimatorSet();
        mAnimatorSetFour.play(fadeOutAnimator)
                .with(translateOutAnimator);

        mAnimatorSetFour.addListener(new Animator.AnimatorListener() {
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
                mIsFourAnimationFinish = true;
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
                mFourAnimatorProcess = (float) animation.getAnimatedValue();
            }
        });

        mAnimatorSetFirst.start();
    }

    private void upDateProcessInfo() {
        List<AppProcessInfo> mProcessList = ProcessManager.getInstance().getRunningAppList();
        mIconInfos = new IconInfo[mProcessList.size() + mOtherDrawable.length];
        for (int i = 0; i < mProcessList.size(); i++) {
            mIconInfos[i] = new IconInfo(BitmapUtil.getBitmapFromDrawable(
                    BitmapUtil.zoomDrawable(mProcessList.get(i).getIcon(), 100, 100)));
        }
        for (int i = 0; i < mOtherDrawable.length; i++) {
            mIconInfos[i + mProcessList.size()] = new IconInfo(BitmapUtil.getBitmapFromDrawable(
                    BitmapUtil.zoomDrawable(getResources().getDrawable(mOtherDrawable[i]), 100, 100)));
        }

        mProcessPaint = new Paint[mProcessList.size() + mOtherDrawable.length];
        for (int i = 0; i < mProcessPaint.length; i++) {
            mProcessPaint[i] = new Paint();
            mProcessPaint[i].setAntiAlias(true);
            mProcessPaint[i].setDither(true);
            mProcessPaint[i].setFilterBitmap(true);
        }
    }

    public DustbinView setFadeTime(int fadeTime) {
        this.mFadeTime = fadeTime;
        return this;
    }

    public DustbinView setRecyclerTime(int recyclerTime) {
        this.mRecyclerTime = recyclerTime;
        return this;
    }

    public DustbinView setShakeTime(int mShakeTime) {
        this.mShakeTime = mShakeTime;
        return this;
    }

    public DustbinView setShakeCount(int mShakeCount) {
        this.mShakeCount = mShakeCount;
        return this;
    }

    public DustbinView setShakeDegree(int shakeDegree) {
        this.mShakeDrgree = shakeDegree;
        return this;
    }

    public void setAnimationListener(IAnimationListener mListener) {
        this.mListener = mListener;
    }

    public interface IAnimationListener {

        void onDustbinFadeIn();

        void onProcessRecycler();

        void onShakeDustbin();

        void onDustbinFadeOut();

        void onAnimationFinish();
    }

    private class IconInfo {

        private int x;
        private int y;
        private int startX;
        private int startY;
        private Bitmap bitmap;
        private int alpha;
        private int delayTime;
        private int direction; //0代表从左到右
        private double rotateAngle;
        private Point controlPoint;

        public IconInfo(Bitmap bitmap) {
            this.bitmap = bitmap;
            Random random = new Random();
            int nextRandom = random.nextInt(mRecyclerTime);
            this.startY = 0;
            this.alpha = 255;
            this.rotateAngle = 0;
            this.delayTime = nextRandom;
            nextRandom = random.nextInt(2);
            if (nextRandom == 0) {
                direction = 0;
                startX = 0;
            } else {
                direction = 1;
                startX = App.sWidth;
            }
            controlPoint = new Point();
            controlPoint.x = App.sWidth / 2;
            controlPoint.y = (int) (random.nextFloat() * 0.5 * App.sHeight);
        }

        public Point getControlPoint() {
            return controlPoint;
        }

        public void setControlPoint(Point controlPoint) {
            this.controlPoint = controlPoint;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public int getDelayTime() {
            return delayTime;
        }

        public void setDelayTime(int delayTime) {
            this.delayTime = delayTime;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public double getRotateAngle() {
            return rotateAngle;
        }

        public void setRotateAngle(double rotateAngle) {
            this.rotateAngle = rotateAngle;
        }

    }
}
