package com.jiepier.filemanager.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;

import com.jiepier.filemanager.R;

/**
 * Created by panruijiesx on 2016/11/24.
 */

public class BitmapUtil {

    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
                .getWidth(), sourceImg.getHeight()); // 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {

            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);

        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
                .getHeight(), Bitmap.Config.ARGB_8888);

        return sourceImg;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            //drawable instanceof VectorDrawable
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromRes(Context context, int ResId) {

        return getBitmapFromDrawable(ActivityCompat.getDrawable(context, ResId));
    }

    public static Bitmap adjustOpacity(Bitmap bitmap, int opacity) {
        Bitmap mutableBitmap = bitmap.isMutable()
                ? bitmap
                : bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        int colour = (opacity & 0xFF) << 24;
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        return mutableBitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap oldbmp = drawableToBitmap(drawable);

        Matrix matrix = new Matrix();

        float sx = ((float) w / width);
        float sy = ((float) h / height);

        matrix.postScale(sx, sy);

        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(w, h, config);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);

        drawable.draw(canvas);
        return bitmap;

    }

    public static Drawable changeDrawableColor(int drawableRes, int colorRes, Context context) {
        //Convert drawable res to bitmap
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableRes);
        final Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        final Paint p = new Paint();
        final Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);

        //Create new drawable based on bitmap
        final Drawable drawable = new BitmapDrawable(context.getResources(), resultBitmap);
        drawable.setColorFilter(new
                PorterDuffColorFilter(context.getResources().getColor(colorRes), PorterDuff.Mode.MULTIPLY));
        return drawable;
    }

    public static Drawable changeDrawableColor(Drawable mydrawable, int colorRes, Context context) {
        //Convert drawable res to bitmap
        final Bitmap bitmap = getBitmapFromDrawable(mydrawable);
        final Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        final Paint p = new Paint();
        final Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);

        //Create new drawable based on bitmap
        final Drawable drawable = new BitmapDrawable(context.getResources(), resultBitmap);
        drawable.setColorFilter(new
                PorterDuffColorFilter(context.getResources().getColor(colorRes), PorterDuff.Mode.MULTIPLY));
        return drawable;
    }

    public static Bitmap getApkIcon(Context context, String path) {

        Bitmap mBitmap = null;
        PackageManager pm = context.getApplicationContext().getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path,
                PackageManager.GET_ACTIVITIES);

        if (packageInfo != null) {
            final ApplicationInfo appInfo = packageInfo.applicationInfo;

            if (appInfo != null) {
                appInfo.sourceDir = path;
                appInfo.publicSourceDir = path;
                final Drawable icon = appInfo.loadIcon(pm);

                if (icon != null) {
                    mBitmap = ((BitmapDrawable) icon).getBitmap();
                }
            }
        } else {
            // load apk icon from /res/drawable/..
            mBitmap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
                    R.drawable.type_apk);
        }

        return mBitmap;
    }
}
