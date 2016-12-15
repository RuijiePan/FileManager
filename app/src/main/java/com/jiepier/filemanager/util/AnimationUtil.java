package com.jiepier.filemanager.util;


import android.animation.Animator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by prj on 2016/8/26.
 * CircularReveal动画效果，类似水波纹扩散
 */

public class AnimationUtil {

    /**
     * 默认从左上角往右下角开始拓展，MultipleRadius建议值为2，为什么为2呢，自己联系prj。。。
     * @param view 展开动画的view
     * @param MultipleRadius 半径倍数
     * @param Duration 动画持续时间
     * @return 动画效果
     */
    public static Animator getCircularReveal(View view,int MultipleRadius,int Duration){

        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(view, 0, 0, 0, finalRadius*MultipleRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(Duration);
        return animator;
    }

    /**
     * @param view 展开动画的view
     * @param centerX 从具体某个点的X坐标开始扩散
     * @param centerY 从具体某个点的Y坐标开始扩散
     * @param MultipleRadius 半径倍数
     * @param Duration 动画时间
     * @return
     */
    public static Animator getCircularReveal(View view,int centerX,int centerY,int MultipleRadius,int Duration){

        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius*MultipleRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(Duration);
        return animator;
    }

    public static void showCircularReveal(View view,int MultipleRadius,int Duration){

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                Animator animator = AnimationUtil.getCircularReveal(view, MultipleRadius, Duration);
                animator.start();
                return true;
            }
        });

    }

    public static void showCircularReveal(View view,int centerX,int centerY,int MultipleRadius,int Duration){

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                Animator animator = AnimationUtil.getCircularReveal(view, centerX, centerY, MultipleRadius, Duration);
                animator.start();
                return true;
            }
        });
    }
}