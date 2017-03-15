package com.jiepier.filemanager.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiepier.filemanager.R;

/**
 * Created by panruijie on 2017/3/6.
 * Email : zquprj@gmail.com
 */

public class DustbinDialog extends Dialog {

    private DustbinView mDustbinView;
    private View mContentView;

    public DustbinDialog(@NonNull Context context) {
        this(context, R.style.junk_finish_dialog);
    }

    public DustbinDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        mContentView = LayoutInflater.from(context)
                .inflate(R.layout.layout_dustbin, null);
        mDustbinView = (DustbinView) mContentView.findViewById(R.id.dustbinView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setContentView(mContentView);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mDustbinView.startAnimation();
        mDustbinView.setAnimationListener(new DustbinView.IAnimationListener() {
            @Override
            public void onDustbinFadeIn() {

            }

            @Override
            public void onProcessRecycler() {

            }

            @Override
            public void onShakeDustbin() {

            }

            @Override
            public void onDustbinFadeOut() {

            }

            @Override
            public void onAnimationFinish() {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }

}
