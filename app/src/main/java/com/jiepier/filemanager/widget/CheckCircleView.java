package com.jiepier.filemanager.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.BitmapUtil;
import com.jiepier.filemanager.util.DisplayUtil;

/**
 * Created by JiePier on 16/12/15.
 */

public class CheckCircleView extends View {

    private Paint mPaint;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private int mWidth;
    private int mHeight;
    private Matrix mMatrix;

    public CheckCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initBitmap();
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
        mBitmapPaint.setStrokeWidth(5);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
    }

    private void initBitmap() {
        mBitmap = BitmapUtil.getBitmapFromRes(getContext(),R.drawable.ic_check_white_36dp);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //将画布转移到右边中间，因为gravity失效了
        canvas.translate(mWidth-mBitmapWidth-DisplayUtil.px2dip(getContext(),40),mHeight/2-mBitmapHeight/2);
        canvas.drawCircle(mBitmapWidth/2,mBitmapHeight/2,mBitmapWidth/2,mPaint);
        canvas.drawBitmap(mBitmap,mMatrix,mBitmapPaint);
        canvas.restore();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
