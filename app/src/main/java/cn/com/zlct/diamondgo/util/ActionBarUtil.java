package cn.com.zlct.diamondgo.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.custom.CircleView;

/**
 * ActionBar初始化
 */
public class ActionBarUtil {

    /**
     * 初始化ActionBar
     */
    public static void initActionBar(ActionBar actionBar, String title, View.OnClickListener onClickListener){
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_normal);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        ImageButton actionbarBack = (ImageButton) actionBar.getCustomView().findViewById(R.id.actionbar_back);
        TextView actionbarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_title);
        actionbarBack.setOnClickListener(onClickListener);
        actionbarTitle.setText(title);
    }

    /**
     * 初始化ActionBar
     */
    public static void initActionBar(ActionBar actionBar, String title, View.OnClickListener onClickListener, String next, View.OnClickListener onClickListenerNext){
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_normal);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fb4c34")));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        ImageButton actionbarBack = (ImageButton) actionBar.getCustomView().findViewById(R.id.actionbar_back);
        TextView actionbarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_title);
        TextView actionbarNext = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_next);
        actionbarBack.setOnClickListener(onClickListener);
        actionbarTitle.setText(title);
        actionbarNext.setVisibility(View.VISIBLE);
        actionbarNext.setText(next);
        actionbarNext.setOnClickListener(onClickListenerNext);
    }

    /**
     * 初始化商品详情页ActionBar
     */
    public static CircleView initActionBar(ActionBar actionBar, String title, View.OnClickListener onClickListener,
                                           View.OnClickListener onClickListenerNext){
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_cart_icon);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        ImageButton actionbarBack = (ImageButton) actionBar.getCustomView().findViewById(R.id.actionbar_detail_back);
        TextView actionbarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_detail_title);
        ImageButton addCart = (ImageButton) actionBar.getCustomView().findViewById(R.id.actionbar_detail_next);
        CircleView circleView = (CircleView) actionBar.getCustomView().findViewById(R.id.actionbar_cartRedDot);
        actionbarBack.setOnClickListener(onClickListener);
        actionbarTitle.setText(title);
        addCart.setOnClickListener(onClickListenerNext);
        circleView.setBackgroundColor(Color.RED);

        return circleView;
    }

    /**
     * 初始化带图标的ActionBar
     */
    public static TextView initActionBarWithIcon(ActionBar actionBar, String title, View.OnClickListener onClickListener, int iconId, View.OnClickListener onClickListenerNext){
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_cart_icon);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        ImageButton actionbarBack = (ImageButton) actionBar.getCustomView().findViewById(R.id.actionbar_detail_back);
        TextView actionbarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_detail_title);
        ImageButton next = (ImageButton) actionBar.getCustomView().findViewById(R.id.actionbar_detail_next);
        actionBar.getCustomView().findViewById(R.id.actionbar_cartRedDot).setVisibility(View.GONE);
        actionbarBack.setOnClickListener(onClickListener);
        actionbarTitle.setText(title);
        next.setImageResource(iconId);
        next.setOnClickListener(onClickListenerNext);
        return actionbarTitle;
    }
}
