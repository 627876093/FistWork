package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;

/**
 * 添加银行卡
 */
public class BankCardAddActivity extends BaseActivity {

    @BindView(R.id.toolbar_text)
    Toolbar toolbarText;
    @BindView(R.id.et_cardNum)
    EditText etCardNum;

    @Override
    protected int getViewResId() {
        return R.layout.activity_bank_card_add;
    }

    @Override
    protected void init() {
        ToolBarUtil.initToolBar(toolbarText, "添加银行卡", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String cardNum = etCardNum.getText().toString();
        if (TextUtils.isEmpty(cardNum)||cardNum.length()<16){
            ToastUtil.initToast(this,"请输入正确的银行卡号");
        }else {
            Intent intent = new Intent(this, BankCardEditActivity.class);
            intent.putExtra("cardNum",cardNum);
            startActivity(intent);
            finish();
        }
    }
}
