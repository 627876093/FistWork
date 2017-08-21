package cn.com.zlct.diamondgo.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 属性动画
 */
public class AnimationUtil {

    /**
     * 转换的工具
     */
    public static AnimatorSet propertyAnimation(SimpleDraweeView sdv, int x, int y) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(sdv, "rotation", 0.0F, 360.0F);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(sdv, "alpha", 1f, 0.5f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(sdv, "scaleX", 1f, 0.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(sdv, "scaleY", 1f, 0.2f);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(sdv, "TranslationX", 0, x);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(sdv, "TranslationY", 0, y);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(Constant.Integers.CartAnimDuration);
        animSet.setInterpolator(new LinearInterpolator());
        //四个动画同时执行
        animSet.playTogether(rotation, alpha, translationX, translationY, scaleX, scaleY);
        return animSet;
    }

    /**
     * 创建动画层
     */
    public static ViewGroup createAnimLayout(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * 添加控件到动画层
     */
    public static View addViewToAnimLayout(Context context, View view, int[] start_loca) {
        final SimpleDraweeView sdv = new SimpleDraweeView(context);
        sdv.setAspectRatio(1.0f);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(view.getWidth(), view.getHeight());
        lp.setMargins(start_loca[0], start_loca[1], 0, 0);
        sdv.setLayoutParams(lp);
        sdv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return sdv;
    }

    /**
     * 创建加载View
     */
    public static View createLoadingView(Context context, ImageView view){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return view;
    }
}
