package cn.com.zlct.diamondgo.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.MobileCodeEntity;
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.Util;
import okhttp3.FormBody;

/*
* 注册
* */

public class RegisterActivity extends BaseActivity implements OkHttpUtil.OnDataListener {

    @BindView(R.id.et_regMobile)
    public EditText etMobile;
    @BindView(R.id.et_regCode)
    public EditText etCode;
    @BindView(R.id.tv_getRegCode)
    public TextView tvCode;
    @BindView(R.id.et_regInvite)
    public EditText etInvite;
    @BindView(R.id.et_regPassword)
    public EditText etPassword;

    private final int retryLimit = Constant.Integers.CodeRetryTime;//重试秒数上限
    private int timeNum = retryLimit;//获取验证码倒计时
    private long getCodeTime;//获取到验证码的时间
    private boolean isRegister;//是否正在注册
    private String correctCode;//正确的验证码
    private String mobile;//获取验证码的手机号
    private LoadingDialog loadingDialog;
    private Handler handler = new Handler();
    private Gson gson = new GsonBuilder().create();

    @Override
    protected int getViewResId() {
        return R.layout.activity_register;
    }

    @OnTextChanged(value = R.id.et_regMobile, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etMobileChanged(CharSequence text) {
        if (text.length() == 11) {
            if (!tvCode.isSelected() && timeNum >= retryLimit) {
                tvCode.setSelected(true);
            }
        } else {
            if (tvCode.isSelected()) {
                tvCode.setSelected(false);
            }
        }
    }

    @OnClick({R.id.iv_regBack, R.id.tv_regLogin, R.id.tv_getRegCode, R.id.tv_register})
    public void reg(View v) {
        switch (v.getId()) {
            case R.id.iv_regBack:
            case R.id.tv_regLogin:
                onBackPressed();
                break;
            case R.id.tv_getRegCode://获取验证码
                getCode();
                break;
            case R.id.tv_register://注册
                if (isRegister) {
                    ToastUtil.initToast(this, "请不要重复操作");
                    break;
                }
                String code = etCode.getText().toString();
                if (TextUtils.isEmpty(mobile) && etMobile.getText().length() == 0) {
                    ToastUtil.initToast(this, "手机号码不能为空");
                } else if (TextUtils.isEmpty(correctCode)) {
                    ToastUtil.initToast(this, "请获取验证码");
                } else if (etCode.getText().length() == 0) {
                    ToastUtil.initToast(this, "请输入验证码");
                } else if (!etMobile.getText().toString().equals(mobile)) {
                    ToastUtil.initToast(this, "请填写获取验证码的手机号 或重新获取验证码");
                } else if (System.currentTimeMillis() - getCodeTime >= (retryLimit + 10) * 1000) {//验证码失效
                    ToastUtil.initToast(this, "验证码已失效 请重新获取");
                } else if (!code.equals(correctCode)) {//验证码输入错误
                    ToastUtil.initToast(this, "验证码错误");
                } else if (etPassword.getText().length() == 0) {
                    ToastUtil.initToast(this, "请输入密码");
                    etPassword.requestFocus();
                } else if (etPassword.getText().length() < 6) {//密码太短
                    ToastUtil.initToast(this, "请输入6位以上密码");
                    etPassword.setHint("请输入6位以上密码");
                    etPassword.setText(null);
                } else {//输入正确，注册
                    Util.hideKeyboard(v);
                    isRegister = true;
                    loadingDialog = LoadingDialog.newInstance("注册中...");
                    loadingDialog.show(getFragmentManager());
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("phone", mobile);
                    builder.add("recommPhone", etInvite.getText().toString()+"");
                    builder.add("password", etPassword.getText().toString()+"");
                    OkHttpUtil.postJson(Constant.URL.userRegist3,builder, this);
                }
                break;
        }
    }

    private void getCode() {
        if (tvCode.isSelected()) {
            mobile = etMobile.getText().toString().trim() + "";
            if (etMobile.getText().length() == 0) {
                ToastUtil.initToast(this, "手机号码不能为空");

            } else if (!mobile.matches(Constant.Strings.RegexMobile)) {
                ToastUtil.initToast(this, "请输入正确的手机号码");
            } else {
                tvCode.setSelected(false);
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("phone", mobile);
                OkHttpUtil.postJson(Constant.URL.userRegist,builder, this);
                //设置XX秒后重试
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tvCode != null) {
                            tvCode.setText("点击重新发送 (" + timeNum + ")");
                            if (timeNum > 0) {
                                handler.postDelayed(this, 1000);
                                timeNum--;
                            } else {
                                resetGetCode();
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onResponse(String url, String json) {
        if (!TextUtils.isEmpty(json)) {
            String decrypt =json;
            Log.e("loge", json);
            if (url.startsWith(Util.transURL(Constant.URL.userRegist))) {//获取手机验证码
                Log.e("loge", "获取验证码: " + decrypt);
                MobileCodeEntity mobileCode = gson.fromJson(decrypt, MobileCodeEntity.class);
                if (Constant.Strings.SUC.equals(mobileCode.getMsg())) {
                    etCode.requestFocus();
                    getCodeTime = System.currentTimeMillis();
                    correctCode = mobileCode.getCode();
                } else {
                    upTimeNum();
                    ToastUtil.initToast(this, mobileCode.getMsg());
                }
            } else if (url.startsWith(Util.transURL(Constant.URL.userRegist3))) {//注册
                Log.e("loge", "注册: " + decrypt);
                SingleWordEntity reg = gson.fromJson(decrypt, SingleWordEntity.class);
                isRegister = false;
                if (Constant.Strings.SUC.equals(reg.getMsg())) {
                    afterRegister(mobile);
                    ToastUtil.initToast(this, "注册成功");
                } else {
                    upTimeNum();
                    ToastUtil.initToast(this, "注册失败");
                }
            }
        }
    }

    @Override
    public void onFailure(String url, String error) {
    }

    private void resetGetCode() {
        tvCode.setText("发送验证码");
        timeNum = retryLimit;
        if (etMobile.getText().length() == 11) {
            tvCode.setSelected(true);
        }
    }

    private void upTimeNum() {
        if (timeNum < retryLimit) {//倒计时中
            timeNum = 0;
        }
    }

    /**
     * 注册之后
     */
    private void afterRegister(String userId) {
        //用户信息加密后保存到本地
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("UserId", DesUtil.encrypt(userId, DesUtil.LOCAL_KEY));
        editor.putLong("LoginTime", System.currentTimeMillis());
        editor.commit();
        setResult(RESULT_OK);
        finish();
    }
}
