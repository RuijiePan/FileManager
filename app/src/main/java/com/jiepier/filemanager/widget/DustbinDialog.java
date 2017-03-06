package com.jiepier.filemanager.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jiepier.filemanager.R;

/**
 * Created by panruijie on 2017/3/6.
 * Email : zquprj@gmail.com
 */

public class DustbinDialog extends Dialog {

    private static final int ANIMATION_TIME = 2000;
    private static AnimationListener sListener;

    private Context mContext;

    public DustbinDialog(@NonNull Context context) {
        this(context, R.style.junk_finish_dialog);
    }

    public DustbinDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        sListener = null;
    }

    public void setAnimationListener(AnimationListener mListener) {
        this.sListener = mListener;
    }

    public static DustbinDialog createDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_dustbin, null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.rl_root);
        ImageView imageView = (ImageView) v.findViewById(R.id.iv_junk_finish);

        Dialog dialog = new DustbinDialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        TranslateAnimation translateUpAnimation = new TranslateAnimation(0, 0, -imageView.getHeight(), 0);
        translateUpAnimation.setDuration(ANIMATION_TIME);
        TranslateAnimation translateDownAnimation = new TranslateAnimation(0, 0, 0, imageView.getHeight() / 2);
        translateDownAnimation.setDuration(ANIMATION_TIME);

        Animation fadeInAnmation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        Animation fadeOutAnmation = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        //结束动画
        AnimationSet animationSetThird = new AnimationSet(true);
        animationSetThird.addAnimation(translateDownAnimation);
        animationSetThird.addAnimation(fadeOutAnmation);
        animationSetThird.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (sListener != null) {
                    sListener.onThirdAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (sListener != null) {
                    sListener.onAnimationFinish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //第一个动画
        AnimationSet animationSetFirst = new AnimationSet(true);
        animationSetFirst.addAnimation(translateUpAnimation);
        animationSetFirst.addAnimation(fadeInAnmation);

        animationSetFirst.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (sListener != null) {
                    sListener.onFirstAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (sListener != null) {
                    sListener.onSecondAnimationStart();
                }
                imageView.startAnimation(animationSetThird);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(animationSetFirst);
        return (DustbinDialog) dialog;
    }

    public interface AnimationListener {

        void onFirstAnimationStart();

        void onSecondAnimationStart();

        void onThirdAnimationStart();

        void onAnimationFinish();

    }
}
