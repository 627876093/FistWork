package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import cn.com.zlct.diamondgo.util.Util;

/**
 * 添加银行卡
 */
public class BankCardEditActivity extends BaseActivity {


    @BindView(R.id.toolbar_text)
    Toolbar toolbarText;
    @BindView(R.id.et_addCardNum)
    EditText etAddCardNum;
    @BindView(R.id.et_addCardUserName)
    EditText etAddCardUserName;
    @BindView(R.id.et_addCardBank)
    EditText etAddCardBank;
    @BindView(R.id.et_addCarduserID)
    EditText etAddCarduserID;
    @BindView(R.id.et_phone)
    EditText etPhone;

    @Override
    protected int getViewResId() {
        return R.layout.activity_bank_card_edit;
    }

    @Override
    protected void init() {
        ToolBarUtil.initToolBar(toolbarText, "填写银行卡信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    @OnTextChanged(value = R.id.et_addCardUserName, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etNameChanged(CharSequence text) {
        Util.isEditError2(text.toString(), etAddCardUserName);
    }

    @OnTextChanged(value = R.id.et_addCardBank, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etBankChanged(CharSequence text) {
        Util.isEditError2(text.toString(), etAddCardBank);
    }


    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String cardNum=etAddCardNum.getText().toString().trim();
        String userName = etAddCardUserName.getText().toString().trim();
        String bankName = etAddCardBank.getText().toString().trim();
        String ID = etAddCarduserID.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(cardNum)){
            ToastUtil.initToast(this, "请输入卡号");
        } else if (TextUtils.isEmpty(userName)) {
            ToastUtil.initToast(this, "请填写持卡人姓名");
        } else if (TextUtils.isEmpty(bankName)) {
            ToastUtil.initToast(this, "请填写开卡银行");
        } else if (TextUtils.isEmpty(ID) || !ID.matches(Constant.Strings.RegexIdNum)) {
            ToastUtil.initToast(this, "请填写正确的身份证号码");
        } else if (TextUtils.isEmpty(phone) || !phone.matches(Constant.Strings.RegexMobile)) {
            ToastUtil.initToast(this, "请填写正确的手机号码");

        }else {
            Intent intent = new Intent(this, BankCardGetPhoneCardActivity.class);
            intent.putExtra("cardNum",cardNum);
            intent.putExtra("userName",userName);
            intent.putExtra("bankName",bankName);
            intent.putExtra("ID",ID);
            intent.putExtra("phone",phone);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

}
