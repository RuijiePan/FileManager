package com.jiepier.filemanager.util;

/**
 * Created by JiePier on 16/12/6.
 */

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by prj on 2016/8/17.
 */

public class SnackbarUtil {
    // make()中的第一个参数，可以写当前界面中的任意一个view对象。
    private static Snackbar mSnackbar;

    public static void show(View view, String msg, int flag, final View.OnClickListener listener) {

        if (flag == 0) { // 短时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        } else { // 长时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        }

        mSnackbar.show();
//        mSnackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.deepskyblue));
        // Snackbar中有一个可点击的文字，这里设置点击所触发的操作
        // 若监听不为空则执行监听的内容。
        if(listener != null){
            mSnackbar.setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                    mSnackbar.dismiss();
                }
            });
        }

    }
}
