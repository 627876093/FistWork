package cn.com.zlct.diamondgo.custom;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseDialog;

/**
 * 修改头像对话框
 */
public class UploadImgDialog extends BaseDialog implements View.OnClickListener {

    public static UploadImgDialog newInstance() {
        return new UploadImgDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //取消标题显示
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_upload_img,
                ((ViewGroup) window.findViewById(android.R.id.content)), false);
        //必须设置，否则不能全屏
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //宽度撑满
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        //进入退出动画
        window.setWindowAnimations(R.style.animTranslateBottom);

        view.findViewById(R.id.btn_openAlbum).setOnClickListener(this);
        view.findViewById(R.id.btn_openCamera).setOnClickListener(this);
        view.findViewById(R.id.btn_cancelAmount).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClick(v);
        dismiss();
    }
}
