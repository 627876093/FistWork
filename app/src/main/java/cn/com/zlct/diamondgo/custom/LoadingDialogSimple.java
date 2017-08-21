package cn.com.zlct.diamondgo.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import cn.com.zlct.diamondgo.R;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
public class LoadingDialogSimple  extends DialogFragment {

    public static LoadingDialogSimple newInstance() {
        LoadingDialogSimple loadingDialog = new LoadingDialogSimple();
        return loadingDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_loading_simple, null);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.TransDialogStyle)
                .setView(view)
                .create();
        ImageView ivLoading = (ImageView) view.findViewById(R.id.iv_loadingSimple);
        RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        ivLoading.startAnimation(rotate);
        //点击对话框外不可取消
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
