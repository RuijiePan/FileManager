package com.jiepier.filemanager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.ColorUtil;

/**
 * Created by panruijie on 16/12/26.
 * Email : zquprj@gmail.com
 * 仿Power clean圆形进度条
 */

public class PowerProgressBar extends View {

    private int mCenter;
    private int mRoundRadius;//内半径
    private int mRoundWidth;//圆的宽度
    private int mRoundColor;//圆的颜色
    private int mRoundFillColor;//圆的填充颜色
    private int mStartAngle;//开始角度
    private float mCenterTopTextSize;
    private float mCenterTextSize;
    private float mCenterBottomTextSize;
    private float mProgress;//填充百分比
    private boolean mIsClockwise;//是否顺时针进度
    private String mCenterTopText;//显示所占百分比textview
    private String mCenterText;//显示具体数值textview
    private String mCenterBootomText;//进度条类型（memory或者storage）
    private RectF mOval;
    private Paint mRoundPaint;
    private Paint mRoundFillPaint;
    private Paint mCenterTopTextPaint;
    private Paint mCenterTextPaint;
    private Paint mCenterBottomTextPaint;
    private int DEFAULT_ROUND_COLOR;
    private int DEFAULT_ROUND_FILL_COLOR;
    private final static int DEFAULT_WIDTH = 10;
    private final static int DEFAULT_START_ANGLE = -90;
    private final static int DEFAULT_PROGRESS = 0;
    private final static int DEFAULT_RADIUS = 200;
    private final static double DEFAULT_TOP_TEXT_SIZE_PERCENT = 0.64;
    private final static double DEFAULT_CENTER_TEXT_SIZE_PERCENT = 0.24;
    private final static double DEFAULT_BOTTOM_TEXT_SIZE_PERCENT = 0.24;
    private final static boolean DEFAULT_CLOCK_WISE = true;//顺时针

    public PowerProgressBar(Context context) {
        this(context , null);
    }

    public PowerProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public PowerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        init(context ,attrs);
    }

    private void initPaint() {

        mRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRoundFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterTopTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterBottomTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRoundPaint.setStyle(Paint.Style.STROKE);
        mRoundFillPaint.setStyle(Paint.Style.STROKE);

        mCenterTopTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mCenterTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mCenterBottomTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void init(Context context ,AttributeSet attrs ) {

        DEFAULT_ROUND_COLOR = ColorUtil.getBackgroundColor(context);
        DEFAULT_ROUND_FILL_COLOR = ColorUtil.getColorPrimary(context);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PowerProgressBar);
        mRoundRadius = ta.getInteger(R.styleable.PowerProgressBar_roundRadius,DEFAULT_RADIUS);
        mRoundWidth = ta.getInteger(R.styleable.PowerProgressBar_roundWith,DEFAULT_WIDTH);
        mRoundColor = ta.getColor(R.styleable.PowerProgressBar_roundColor,DEFAULT_ROUND_COLOR);
        mRoundFillColor = ta.getColor(R.styleable.PowerProgressBar_roundFillColor,DEFAULT_ROUND_FILL_COLOR);
        mStartAngle = ta.getInteger(R.styleable.PowerProgressBar_startAngle,DEFAULT_START_ANGLE);
        mProgress = ta.getInteger(R.styleable.PowerProgressBar_progress,DEFAULT_PROGRESS);
        mIsClockwise = ta.getBoolean(R.styleable.PowerProgressBar_isClockwise,DEFAULT_CLOCK_WISE);
        mCenterTopText = ta.getString(R.styleable.PowerProgressBar_centerTopText);
        mCenterText = ta.getString(R.styleable.PowerProgressBar_centerTexts);
        mCenterBootomText = ta.getString(R.styleable.PowerProgressBar_centerBottomText);
        mCenterTopTextSize = ta.getDimension(
                R.styleable.PowerProgressBar_centerTopTextSizePercent,
                (float) DEFAULT_TOP_TEXT_SIZE_PERCENT) * mRoundRadius;
        mCenterTextSize = ta.getDimension(
                R.styleable.PowerProgressBar_centerTextSizePercent ,
                (float) DEFAULT_CENTER_TEXT_SIZE_PERCENT) * mRoundRadius;
        mCenterBottomTextSize = ta.getDimension(
                R.styleable.PowerProgressBar_centerBottomTextSizePercent,
                (float) DEFAULT_BOTTOM_TEXT_SIZE_PERCENT) * mRoundRadius;


        //根据主题色设置圆形进度条的颜色
        mRoundPaint.setColor(mRoundColor);
        mRoundFillPaint.setColor(mRoundFillColor);
        mRoundPaint.setStrokeWidth(mRoundWidth);
        mRoundFillPaint.setStrokeWidth(mRoundWidth);

        mCenterTopTextPaint.setTextSize(mCenterTopTextSize);
        mCenterTextPaint.setTextSize(mCenterTextSize);
        mCenterBottomTextPaint.setTextSize(mCenterBottomTextSize);

        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenter = w/2;
        mOval = new RectF(mCenter - mRoundRadius, mCenter - mRoundRadius, mCenter
                + mRoundRadius, mCenter + mRoundRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画出圆环
        canvas.drawCircle(mCenter,mCenter,mRoundRadius,mRoundPaint);
        //画出填充的弧
        if (mIsClockwise) {
            canvas.drawArc(mOval, mStartAngle, mStartAngle - (float) (3.6 * mProgress), false, mRoundFillPaint);
        }else {
            canvas.drawArc(mOval, mStartAngle, mStartAngle + (float) (3.6 * mProgress), false, mRoundFillPaint);
        }

        //画出圆环中间的字体
        float topTextWidth = mCenterTopTextPaint.measureText(mCenterTopText);
        float centerTextWidth = mCenterTextPaint.measureText(mCenterText);
        float bottomTextWidth = mCenterBottomTextPaint.measureText(mCenterBootomText);

        canvas.drawText(mCenterTopText
                ,mCenter - topTextWidth/2
                ,mCenter + mCenterTopTextSize/2 - mRoundRadius/3
                ,mCenterTopTextPaint);

        canvas.drawText(mCenterText
                ,mCenter - centerTextWidth/2
                ,mCenter + mCenterTopTextSize/2
                ,mCenterTextPaint);

        canvas.drawText(mCenterBootomText
                ,mCenter - bottomTextWidth/2
                ,mCenter + mCenterTopTextSize/2 + mRoundRadius/3
                ,mCenterBottomTextPaint);
    }

    public void setmRoundRadius(int mRoundRadius) {
        this.mRoundRadius = mRoundRadius;
    }

    public void setmRoundWidth(int mRoundWidth) {
        this.mRoundWidth = mRoundWidth;
    }

    public void setmRoundColor(int mRoundColor) {
        this.mRoundColor = mRoundColor;
    }

    public void setmRoundFillColor(int mRoundFillColor) {
        this.mRoundFillColor = mRoundFillColor;
    }

    public void setmStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
    }

    public void setmCenterTopTextSize(float mCenterTopTextSize) {
        this.mCenterTopTextSize = mCenterTopTextSize;
    }

    public void setmCenterTextSize(float mCenterTextSize) {
        this.mCenterTextSize = mCenterTextSize;
    }

    public void setmCenterBottomTextSize(float mCenterBottomTextSize) {
        this.mCenterBottomTextSize = mCenterBottomTextSize;
    }

    public void setmProgress(float mProgress) {
        this.mProgress = mProgress;
    }

    public void setmIsClockwise(boolean mIsClockwise) {
        this.mIsClockwise = mIsClockwise;
    }

    public void setmCenterTopText(String mCenterTopText) {
        this.mCenterTopText = mCenterTopText;
    }

    public void setmCenterText(String mCenterText) {
        this.mCenterText = mCenterText;
    }

    public void setmCenterBootomText(String mCenterBootomText) {
        this.mCenterBootomText = mCenterBootomText;
    }
}
