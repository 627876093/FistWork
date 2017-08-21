package cn.com.zlct.diamondgo.util;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.custom.CircleTextView;

/**
 * ToolBar工具类
 */
public class ToolBarUtil {

    /**
     * 初始化ToolBar -- 无字
     * @param title 中间的文字
     * @param clickListener 按钮回调监听
     */
    public static void initToolBar(Toolbar toolBar, String title, View.OnClickListener clickListener) {
        init(toolBar, title, clickListener);
    }

    /**
     * 初始化ToolBar -- text
     * @param title 中间的文字
     * @param next 右侧的文字
     * @param clickListener 按钮回调监听
     */
    public static void initToolBar(Toolbar toolBar, String title, String next, View.OnClickListener clickListener) {
        init(toolBar, title, next, clickListener);
    }

    /**
     * 初始化ToolBar -- icon
     * @param title 中间的文字
     * @param resId 右侧的图标
     * @param clickListener 按钮回调监听
     */
    public static CircleTextView initToolBar(Toolbar toolBar, String title, int resId,
                                             View.OnClickListener clickListener) {
        init(toolBar, title, resId, clickListener);
        CircleTextView circleView = (CircleTextView) toolBar.findViewById(R.id.toolbar_iconCartDot);
        circleView.setBackgroundColor(Color.WHITE);
        return circleView;
    }

    /**
     * 公共初始化方法 -- 无字
     */
    public static void init(Toolbar toolBar, String title, View.OnClickListener clickListener) {
        ImageView toolbarBack;
        TextView toolbarTitle;
        if (toolBar.getId() == R.id.toolbar_text) {
            toolbarBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
            toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        } else {
            toolbarBack = (ImageView) toolBar.findViewById(R.id.toolbar_iconBack);
            toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_iconTitle);
        }
        toolbarBack.setOnClickListener(clickListener);
        toolbarTitle.setText(title);
    }

    /**
     * 公共初始化方法 -- text
     */
    private static void init(Toolbar toolBar, String title, String next, View.OnClickListener clickListener) {
        ImageView toolbarBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
        TextView toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        TextView toolbarNext = (TextView) toolBar.findViewById(R.id.toolbar_next);
        toolbarBack.setOnClickListener(clickListener);
        toolbarTitle.setText(title);
        toolbarNext.setVisibility(View.VISIBLE);
        toolbarNext.setText(next);
        toolbarNext.setOnClickListener(clickListener);
    }

    /**
     * 公共初始化方法 -- icon
     */
    private static void init(Toolbar toolBar, String title, int resId, View.OnClickListener clickListener) {
        ImageView toolbarBack = (ImageView) toolBar.findViewById(R.id.toolbar_iconBack);
        TextView toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_iconTitle);
        ImageView toolbarNext = (ImageView) toolBar.findViewById(R.id.toolbar_iconNext);
        toolbarBack.setOnClickListener(clickListener);
        toolbarTitle.setText(title);
        toolbarNext.setVisibility(View.VISIBLE);
        if (resId != -1) {
            toolbarNext.setImageResource(resId);
        }
        toolbarNext.setOnClickListener(clickListener);
    }
}
