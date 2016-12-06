package com.jiepier.filemanager.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.R;


/**
 * Created by sll on 2015/9/6 0006.
 */
public class ResourceUtil {

  public static int getThemeColor(@NonNull Context context) {
    return getThemeAttrColor(context, R.attr.colorPrimary);
  }

  public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
    TypedArray a = context.obtainStyledAttributes(null, new int[] { attr });
    try {
      return a.getColor(0, 0);
    } finally {
      a.recycle();
    }
  }

  public static int getStatusBarHeight(Context mContext) {
    int result = 0;
    int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = mContext.getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

}
