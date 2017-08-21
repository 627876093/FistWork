package cn.com.zlct.diamondgo.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseDialog;

/**
 * 加载等待对话框
 */
public class LoadingDialog extends BaseDialog {

    public static LoadingDialog newInstance(String tips) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("tips", tips);
        loadingDialog.setArguments(bundle);
        return loadingDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_loading, container);
        ImageView ivLoading = (ImageView) v.findViewById(R.id.iv_loading);
        TextView tvLoading = (TextView) v.findViewById(R.id.tv_loading);
        RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        ivLoading.startAnimation(rotate);
        if (getArguments().getString("tips").equals("")){
            tvLoading.setVisibility(View.GONE);
        }

        tvLoading.setText(getArguments().getString("tips"));
        //点击对话框外不可取消
        getDialog().setCanceledOnTouchOutside(false);
        //设置对话框背景
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.sp_solid_trans);
        //取消标题显示
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return v;
    }
}
