package cn.com.zlct.diamondgo.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseDialog;

/**
 * 确认 对话框
 */
public class ConfirmDialog extends BaseDialog implements View.OnClickListener , DialogInterface.OnKeyListener{

    private TextView btnCancel;
    private TextView btnConfirm;
    private OnBtnClickListener onBtnClickListener;

    public static ConfirmDialog newInstance(String title, String cancel, String confirm) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("cancel", cancel);
        bundle.putString("confirm", confirm);
        confirmDialog.setArguments(bundle);
        return confirmDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        TextView deleteTitle = (TextView) view.findViewById(R.id.tv_delete);
        btnCancel = (TextView) view.findViewById(R.id.btn_cancelDialog);
        btnConfirm = (TextView) view.findViewById(R.id.btn_confirmDialog);
        //点击对话框外不可取消
        alertDialog.setCanceledOnTouchOutside(false);
        deleteTitle.setText(getArguments().getString("title"));
        //特定字符情况下隐藏取消按钮
        if ("force".equals(getArguments().getString("cancel"))) {
            alertDialog.setOnKeyListener(this);
            btnCancel.setVisibility(View.GONE);
        } else {
            btnCancel.setText(getArguments().getString("cancel"));
        }
        btnConfirm.setText(getArguments().getString("confirm"));
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        return alertDialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirmDialog) {//确认
            try {
                onBtnClickListener.onBtnClick(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dismiss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        } else {
            return false;
        }
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener){
        this.onBtnClickListener = onBtnClickListener;
    }

    public interface OnBtnClickListener {
        void onBtnClick(View view) throws Exception;
    }
}
