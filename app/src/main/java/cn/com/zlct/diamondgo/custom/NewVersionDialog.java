package cn.com.zlct.diamondgo.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import cn.com.zlct.diamondgo.R;


/**
 * 发现新版本 对话框
 */
public class NewVersionDialog extends DialogFragment implements View.OnClickListener, DialogInterface.OnKeyListener {

    private TextView btnCancel;
    private TextView btnConfirm;
    private OnBtnClickListener onBtnClickListener;

    public static NewVersionDialog newInstance(String title, String content, String cancel, String confirm) {
        NewVersionDialog confirmDialog = new NewVersionDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("cancel", cancel);
        bundle.putString("confirm", confirm);
        confirmDialog.setArguments(bundle);
        return confirmDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_version, null);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_versionTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_versionContent);
        btnCancel = (TextView) view.findViewById(R.id.btn_cancelVersion);
        btnConfirm = (TextView) view.findViewById(R.id.btn_confirmVersion);
        //点击对话框外不可取消
        alertDialog.setCanceledOnTouchOutside(false);
        tvTitle.setText(getArguments().getString("title"));
        tvContent.setText(getArguments().getString("content"));
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
        if (v.getId() == R.id.btn_confirmVersion) {//确认
            onBtnClickListener.onBtnClick(v);
        }
        dismiss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH) {
            System.exit(0);
            return true;
        } else {
            return false;
        }
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener){
        this.onBtnClickListener = onBtnClickListener;
    }

    public interface OnBtnClickListener {
        void onBtnClick(View view);
    }
}
