package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.model.GoUpdatePasswordEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import cn.com.zlct.diamondgo.util.Util;
import okhttp3.FormBody;

public class RetrievePswByPhoneActivity extends BaseActivity implements OkHttpUtil.OnDataListener {

    /*
    * 手机找回
    *
    * */
    @BindView(R.id.toolbar_text)
    Toolbar toolbarText;
    @BindView(R.id.et_Mobile)
    EditText etMobile;
    @BindView(R.id.et_Code)
    EditText etCode;
    @BindView(R.id.tv_getCode)
    TextView tvGetCode;
    @BindView(R.id.ib_delMobile)
    ImageButton ibDelMobile;

    private final int retryLimit = Constant.Integers.CodeRetryTime;//重试秒数上限
    private int timeNum = retryLimit;//获取验证码倒计时
    private Handler handler = new Handler();
    private String mobile;//获取验证码的手机号
    private Gson gson = new GsonBuilder().create();
    private String Code;
    private String verification;

    @Override
    protected int getViewResId() {
        return R.layout.activity_retrieve_psw_by_phone;
    }

    @Override
    protected void init() {

        ToolBarUtil.initToolBar(toolbarText, "重置密码", "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.toolbar_back) {
                    onBackPressed();

                } else {//邮箱找回
//                    startActivity(new Intent(getBaseContext(), RetrievePswByEmailActivity.class));
//                    onBackPressed();
                }
            }
        });
    }

    //监听et文字变化
    @OnTextChanged(value = R.id.et_Mobile, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etMobileChanged(CharSequence text) {
        PhoneUtil.isEditError2WithDel(text.toString(), etMobile, ibDelMobile);
        if (text.length() == 11) {
            if (!tvGetCode.isSelected() && timeNum >= retryLimit) {
                tvGetCode.setSelected(true);
            }
        } else {
            if (tvGetCode.isSelected()) {
                tvGetCode.setSelected(false);
            }
        }
    }

    @OnClick({R.id.tv_getCode, R.id.tv_next,R.id.ib_delMobile})
    public void onViewClicked(View view) {
        mobile = etMobile.getText().toString();
        Code = etCode.getText().toString();
        switch (view.getId()) {

            case R.id.tv_getCode:
                getCode();
                break;
            case R.id.tv_next:
                if (mobile.length() == 0) {
                    ToastUtil.initToast(this, "请输入手机号");
                } else if (!mobile.matches(Constant.Strings.RegexMobile)) {
                    ToastUtil.initToast(this, "输入手机号错误");
                    etMobile.setText(null);
                    etMobile.requestFocus();
                } else if (Code.length() == 0) {
                    ToastUtil.initToast(this, "请输入验证码");
                } else {//下一步
                    if (Code.equals(verification)) {
                        Intent intent = new Intent(this, RetrievePswActivity.class);
                        intent.putExtra("phone",mobile);
                        startActivity(intent);
                        finish();
                    }else {
                        ToastUtil.initToast(this, "验证码输入错误，请重新获取");
                        upTimeNum();

                    }
                }
                break;
            case R.id.ib_delMobile:
                etMobile.setText(null);
                etMobile.requestFocus();
                upTimeNum();
                break;
        }
    }

    private void getCode() {
        if (tvGetCode.isSelected()) {
            mobile = etMobile.getText().toString();
            if (mobile.length() == 0) {
                ToastUtil.initToast(this, "手机号码不能为空");
            } else if (!mobile.matches(Constant.Strings.RegexMobile)) {
                ToastUtil.initToast(this, "请输入正确的手机号码");

            } else {
                tvGetCode.setSelected(false);
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("phone", mobile);
                OkHttpUtil.postJson(Constant.URL.getVerificationCode,builder ,this);
//                String jsonString = gson.toJson(new GetMobileCode(mobile));
//                Log.e("loge", "getCode: " + jsonString);
//                OkHttpUtil.postJson(Constant.URL.GetMobileCode, DesUtil.encrypt(jsonString), this);
                //设置XX秒后重试
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tvGetCode != null) {
                            tvGetCode.setText("点击重新发送 (" + timeNum + ")");
                            if (timeNum > 0) {
                                handler.postDelayed(this, 1000);
                                timeNum--;
                            } else {
                                tvGetCode.setText("发送验证码");
                                timeNum = retryLimit;
                                if (etMobile.getText().length() == 11) {
                                    tvGetCode.setSelected(true);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onResponse(String url, String json) {
        String decrypt = DesUtil.decrypt(json);
        Log.e("loge", "onResponse: "+json );
        if (url.startsWith(Util.transURL(Constant.URL.getVerificationCode))) {
            Gson gson = new Gson();
            GoUpdatePasswordEntity goUpdatePasswordEntity = gson.fromJson(json, GoUpdatePasswordEntity.class);
            if (!TextUtils.isEmpty(goUpdatePasswordEntity.getPhone())) {
                verification = goUpdatePasswordEntity.getCode();
            } else {
                upTimeNum();
                etMobile.setText(null);
                etMobile.requestFocus();
                ToastUtil.initToast(this, "账户错误，请重新输入");
            }
        }
    }

    @Override
    public void onFailure(String url, String error) {

    }

    private void upTimeNum() {
        if (timeNum < retryLimit) {//倒计时中
            timeNum = 0;
        }
    }
}
