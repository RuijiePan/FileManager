package com.jiepier.filemanager.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.ColorUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by panruijie on 17/1/6.
 * Email : zquprj@gmail.com
 * 单词首字母的sidebarview。类似美团城市选择器，可用于recyclerview的首写单词快选
 */

public class WaveSideBarView extends View {

    //中间圆弧弧度
    private static final double ANGLE = Math.PI * 45 /180;
    //上下部分圆弧弧度
    private static final double ANGLE_B = Math.PI * 90 / 180;
    // 当前选中的位置
    private int mChoose = -1;
    // 渲染字母表
    private List<String> mLetters;
    // 字母列表画笔
    private Paint mLettersPaint = new Paint();
    // 提示字母画笔
    private Paint mTextPaint = new Paint();
    // 波浪画笔
    private Paint mWavePaint = new Paint();

    private float mTextSize;
    private float mWaveTextSize;
    private int mTextColor;
    private int mWaveColor;
    private int mTextColorChoose;
    private int mWidth;
    private int mHeight;
    private int mItemHeight;
    private int mPadding;

    // 波浪路径
    private Path mWavePath = new Path();
    // 中间圆形路径
    private Path mBallPath = new Path();
    //字体的边界
    private RectF mTextRectF = new RectF();
    //手指滑动的Y点作为中心点
    private int mCenterY;
    //贝塞尔曲线的分布半径
    private int mRadius;
    //圆形半径
    private int mBallRadius;
    // 用于过渡效果计算
    ValueAnimator mRatioAnimator;
    // 用于绘制贝塞尔曲线的比率
    private float mRatio;
    // 选中字体的坐标
    private float mPosX, mPosY;
    // 圆形中心点X
    private float mBallCenterX;
    private OnTouchLetterChangeListener mListener;

    public WaveSideBarView(Context context) {
        this(context , null);
    }

    public WaveSideBarView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public WaveSideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mLetters = Arrays.asList(context.getResources().getStringArray(R.array.waveSideBarLetters));

        mTextColor = Color.parseColor("#969696");
        mWaveColor = ColorUtil.getColorPrimary(context);
        mTextColorChoose = Color.WHITE;
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize_sidebar);
        mWaveTextSize = context.getResources().getDimensionPixelSize(R.dimen.wave_textSize_sidebar);
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.textSize_sidebar_padding);

        if (attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WaveSideBarView);
            mTextColor = a.getColor(R.styleable.WaveSideBarView_sidebarTextColor, mTextColor);
            mTextColorChoose = a.getColor(R.styleable.WaveSideBarView_sidebarChooseTextColor, mTextColorChoose);
            mTextSize = a.getFloat(R.styleable.WaveSideBarView_sidebarTextSize, mTextSize);
            mWaveTextSize = a.getFloat(R.styleable.WaveSideBarView_sidebarWaveTextSize, mWaveTextSize);
            mWaveColor = a.getColor(R.styleable.WaveSideBarView_sidebarBackgroundColor, mWaveColor);
            mRadius = a.getInt(R.styleable.WaveSideBarView_sidebarRadius, context.getResources().getDimensionPixelSize(R.dimen.radius_sidebar));
            mBallRadius = a.getInt(R.styleable.WaveSideBarView_sidebarBallRadius, context.getResources().getDimensionPixelSize(R.dimen.ball_radius_sidebar));
            a.recycle();
        }

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(mWaveColor);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColorChoose);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mWaveTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制字母列表
        drawLetters(canvas);

        //绘制波浪
        drawWavePath(canvas);

        //绘制圆
        drawBallPath(canvas);

        //绘制选中的字体
        drawChooseText(canvas);
    }

    private void drawLetters(Canvas canvas) {

        mTextRectF.left = mPosX - mTextSize;
        mTextRectF.right = mPosX + mTextSize;
        mTextRectF.top = mTextSize/2;
        mTextRectF.bottom = mHeight - mTextSize/2;

        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.FILL);
        mLettersPaint.setColor(Color.parseColor("#F9F9F9"));
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(mTextRectF, mTextSize, mTextSize, mLettersPaint);

        for (int i = 0; i < mLetters.size(); i++) {
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColor);
            mLettersPaint.setAntiAlias(true);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics fontMetrics = mLettersPaint.getFontMetrics();
            float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);

            float posY = mItemHeight * i + baseline / 2 + mPadding;

            if (i == mChoose) {
                mPosY = posY;
            } else {
                canvas.drawText(mLetters.get(i), mPosX, posY, mLettersPaint);
            }
        }
    }

    private void drawWavePath(Canvas canvas) {

        mWavePath.reset();
        mWavePath.moveTo(mWidth,mCenterY - 3 * mRadius);

        int controlTopY = mCenterY - 2 * mRadius;
        int endTopX = (int) (mWidth - mRadius * Math.cos(ANGLE) * mRatio);
        int endTopY = (int) (controlTopY + mRadius * Math.sin(ANGLE));

        int controlCenterX = (int) (mWidth - 1.8 * mRadius * Math.sin(ANGLE_B) * mRatio);
        int controlCenterY = mCenterY;

        int controlBottomY = mCenterY + 2 * mRadius;
        int endBottomY = (int) (controlBottomY - mRadius * Math.cos(ANGLE));

        mWavePath.quadTo(mWidth, controlTopY, endTopX, endTopY);
        mWavePath.quadTo(controlCenterX, controlCenterY, endTopX, endBottomY);
        mWavePath.quadTo(mWidth, controlBottomY, mWidth, controlBottomY + mRadius);
        mWavePath.close();

        canvas.drawPath(mWavePath,mWavePaint);
    }

    private void drawBallPath(Canvas canvas) {

        //x轴的移动路径
        mBallCenterX = (mWidth + mBallRadius) - (2.0f * mRadius + 2.0f * mBallRadius) * mRatio;

        mBallPath.reset();
        mBallPath.addCircle(mBallCenterX, mCenterY, mBallRadius, Path.Direction.CW);

        //op操作符，api需要19
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            mBallPath.op(mWavePath, Path.Op.DIFFERENCE);
        }

        mBallPath.close();
        canvas.drawPath(mBallPath, mWavePaint);
    }

    private void drawChooseText(Canvas canvas) {
        if (mChoose != -1) {
            // 绘制右侧选中字符
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColorChoose);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mLetters.get(mChoose), mPosX, mPosY, mLettersPaint);

            // 绘制提示字符
            if (mRatio >= 0.9f) {
                String target = mLetters.get(mChoose);
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);
                float x = mBallCenterX;
                float y = mCenterY + baseline / 2;
                canvas.drawText(target, x, y, mTextPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mItemHeight = (mHeight - mPadding)/mLetters.size();
        mPosX = mWidth - 1.6f * mTextSize;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        int oldChoose = mChoose;
        int newChoose = (int) (y / mHeight * mLetters.size());

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (x < mWidth - 2 * mRadius)
                    return false;
                //当落点与之前的选择不一样时，要修改当前的落点字母。否则在没移动之前字母选择不变
                if (oldChoose != newChoose) {
                    if (newChoose >= 0 && newChoose < mLetters.size()) {
                        mChoose = newChoose;
                        if (mListener != null) {
                            mListener.onLetterChange(mLetters.get(newChoose));
                        }
                    }
                    mCenterY = (int) y;
                    invalidate();
                }
                startAnimator(mRatio, 1.0f);
                break;
            case MotionEvent.ACTION_MOVE:
                mCenterY = (int) y;
                if (oldChoose != newChoose){
                    if (newChoose > 0 && newChoose < mLetters.size()){
                        mChoose = newChoose;
                        if (mListener != null)
                            mListener.onLetterChange(mLetters.get(newChoose));
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAnimator(mRatio, 0f);
                mChoose = -1;
                break;
        }
        return true;
    }

    private void startAnimator(float...value) {
        if (mRatioAnimator == null) {
            mRatioAnimator = new ValueAnimator();
        }
        mRatioAnimator.cancel();
        mRatioAnimator.setFloatValues(value);
        mRatioAnimator.addUpdateListener(value1 -> {
            mRatio = (float) value1.getAnimatedValue();
            invalidate();
        });
        mRatioAnimator.start();
    }

    public void setOnTouchLetterChangeListener(OnTouchLetterChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnTouchLetterChangeListener {
        void onLetterChange(String letter);
    }
}
