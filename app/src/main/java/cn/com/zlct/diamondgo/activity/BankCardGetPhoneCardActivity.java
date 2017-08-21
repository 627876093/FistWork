package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.GoUpdatePasswordEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import okhttp3.FormBody;

public class BankCardGetPhoneCardActivity extends BaseActivity {


    @BindView(R.id.tv_phoneNum)
    TextView tvPhoneNum;
    @BindView(R.id.toolbar_text)
    Toolbar toolbarText;
    @BindView(R.id.phone_code)
    EditText phone_code;
    @BindView(R.id.btn)
    TextView btn;

    UserInfoEntity userInfoEntity;
    private final int retryLimit = Constant.Integers.CodeRetryTime;//重试秒数上限
    private int timeNum = retryLimit;//获取验证码倒计时
    Handler handler = new Handler();
    private LoadingDialog loadingDialog;
    String cardNum;
    String userName;
    String bankName;
    String id;
    String phone;
    String trueCode = "defult";

    @Override
    protected int getViewResId() {
        return R.layout.activity_bank_card_get_phone_card;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        ToolBarUtil.initToolBar(toolbarText, "填写银行卡信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardNum = getIntent().getStringExtra("cardNum");
        userName = getIntent().getStringExtra("userName");
        bankName = getIntent().getStringExtra("bankName");
        id = getIntent().getStringExtra("ID");
        phone = getIntent().getStringExtra("phone");
        if (phone.length() > 0) {
            btn.setSelected(true);
            tvPhoneNum.setText("验证码已发送至手机号：" + PhoneUtil.getMobile(phone));
        }
    }

    @Override
    protected void loadData() {
        //发送验证码到手机 tvPhoneNum
        if (btn.isSelected()) {
            btn.setSelected(false);
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("phone", phone);
            OkHttpUtil.postJson(Constant.URL.getVerificationCode,builder ,new OkHttpUtil.OnDataListener() {
                @Override
                public void onResponse(String url, String json) {
                    Log.e("loge", json);
                    Gson gson = new Gson();
                    GoUpdatePasswordEntity goUpdatePasswordEntity = gson.fromJson(json, GoUpdatePasswordEntity.class);
                    trueCode = goUpdatePasswordEntity.getCode();
                }

                @Override
                public void onFailure(String url, String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.initToast(BankCardGetPhoneCardActivity.this, "获取验证码失败，请重试");
                            if (phone.length() == 11) {btn.setSelected(true);}
                            btn.setText("发送验证码");
                        }
                    });
                }
            });
//                String jsonString = gson.toJson(new GetMobileCode(mobile));
//                Log.e("loge", "getCode: " + jsonString);
//                OkHttpUtil.postJson(Constant.URL.GetMobileCode, DesUtil.encrypt(jsonString), this);
            //设置XX秒后重试
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (btn != null) {
                        btn.setText("点击重新发送 (" + timeNum + ")");
                        if (timeNum > 0) {
                            handler.postDelayed(this, 1000);
                            timeNum--;
                        } else {
                            btn.setText("发送验证码");
                            timeNum = retryLimit;
                            if (phone.length() == 11) {
                                btn.setSelected(true);
                            }
                        }
                    }
                }
            });
        }

    }


    @OnClick(R.id.btn)
    public void onBtnClicked() {
        loadData();
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String code = phone_code.getText().toString().trim() + "";
        if (code.equals(trueCode)) {
            loadingDialog = LoadingDialog.newInstance("上传中...");
            loadingDialog.show(getFragmentManager());
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("phone", phone);
            builder.add("bankCardNumber", cardNum);
            builder.add("cardholder", userName);
            builder.add("card", id);
            builder.add("bankMine", bankName);
            builder.add("phoneId", userInfoEntity.getPhone());

            Log.e("loge", Constant.URL.appUserBankBinding + "   " + userInfoEntity.getPhone());
            OkHttpUtil.postJson(Constant.URL.appUserBankBinding, builder, new OkHttpUtil.OnDataListener() {
                @Override
                public void onResponse(String url, String json) {
                    Log.e("loge", json);
                    dismissLoading();
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String url, String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.initToast(BankCardGetPhoneCardActivity.this,"上传失败请重试");
                            dismissLoading();
                        }
                    });
                }
            });
        } else {
            ToastUtil.initToast(this, "请输入正确的验证码");
        }

    }


    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
