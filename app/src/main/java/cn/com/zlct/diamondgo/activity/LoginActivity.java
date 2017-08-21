package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.LoginEntity;
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.Util;
import okhttp3.FormBody;

/*
* 登陆
* */
public class LoginActivity extends BaseActivity implements OkHttpUtil.OnDataListener {

    @BindView(R.id.et_logMobile)
    public EditText etMobile;
    @BindView(R.id.et_logPassword)
    public EditText etPassword;
    private LoadingDialog loadingDialog;
    private Gson gson = new GsonBuilder().create();

    @Override
    protected int getViewResId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.iv_logBack, R.id.tv_logRegister, R.id.tv_login, R.id.tv_logForget,R.id.tv_loginByTB})
    public void login(View v) {
        switch (v.getId()) {
            case R.id.iv_logBack:
                onBackPressed();
                break;
            case R.id.tv_logRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_login:
                String mobile = etMobile.getText().toString();
                if (etMobile.getText().length() == 0) {
                    ToastUtil.initToast(this, "账号不能为空");
                } else if (etPassword.getText().length() == 0) {
                    ToastUtil.initToast(this, "请输入密码");
                    etPassword.requestFocus();
                } else if (etPassword.getText().length() < 6) {//密码太短
                    ToastUtil.initToast(this, "密码太短了");
                    etPassword.setHint("请输入6位以上密码");
                    etPassword.setText(null);
                } else {
                    if(mobile.matches(Constant.Strings.RegexMobile)){
                        Util.hideKeyboard(v);
                        loadingDialog = LoadingDialog.newInstance("登录中...");
                        loadingDialog.show(getFragmentManager());

                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("phone", mobile);
                        builder.add("password", etPassword.getText().toString());
                        OkHttpUtil.postJson(Constant.URL.userLoginApp,builder, this);
                    }else {ToastUtil.initToast(this, "请输入正确的手机号码");}
                }
                break;
            case R.id.tv_loginByTB:
                String mobiles = etMobile.getText().toString();
                if (etMobile.getText().length() == 0) {
                    ToastUtil.initToast(this, "账号不能为空");
                } else if (etPassword.getText().length() == 0) {
                    ToastUtil.initToast(this, "请输入密码");
                    etPassword.requestFocus();
                } else if (etPassword.getText().length() < 6) {//密码太短
                    ToastUtil.initToast(this, "密码太短了");
                    etPassword.setHint("请输入6位以上密码");
                    etPassword.setText(null);
                } else {
                    Util.hideKeyboard(v);
                    loadingDialog = LoadingDialog.newInstance("登录中...");
                    loadingDialog.show(getFragmentManager());
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("phone", mobiles);
                    builder.add("password", etPassword.getText().toString());
                    OkHttpUtil.postJson(Constant.URL.getAppCurrencyUser,builder, this);

                }
                break;
            case R.id.tv_logForget://忘记密码
                startActivity(new Intent(this, RetrievePswByPhoneActivity.class));
                break;
        }
    }

    @Override
    public void onResponse(String url, String json) {
        if (url.startsWith(Util.transURL(Constant.URL.userLoginApp))) {//登录
            Log.e("loge", "登录:" + json);
            dismissLoading();
            Gson gson = new GsonBuilder().create();
            LoginEntity loginEntity = gson.fromJson(json, LoginEntity.class);
            if ("success".equals(loginEntity.getMsg())){
                ToastUtil.initToast(this, "登陆成功");
                afterLogin(loginEntity.getPhone());
            }else {
                ToastUtil.initToast(this, loginEntity.getDate());
            }

//            SingleWordEntity login = new Gson().fromJson(decrypt, SingleWordEntity.class);
//            ToastUtil.initToast(this, login.getMessage());
//            if (login.getCode()+"" == Constant.Integers.SUC) {
//                afterLogin(login.getData().getUserId());
//            }

        } else if (url.startsWith(Util.transURL(Constant.URL.getAppCurrencyUser))) {//通宝登录
            dismissLoading();
            Log.e("loge","通宝登录:"+json);
            Gson gson = new GsonBuilder().create();
            LoginEntity loginEntity = gson.fromJson(json, LoginEntity.class);
            if ("success".equals(loginEntity.getMsg())){
                ToastUtil.initToast(this, "登陆成功");
                afterLogin(loginEntity.getPhone());
            }else {
                ToastUtil.initToast(this, loginEntity.getDate());
            }

            /*SingleWordEntity login = new Gson().fromJson(decrypt, SingleWordEntity.class);
            ToastUtil.initToast(this, login.getMessage());
            if (login.getCode()+"" == Constant.Integers.SUC) {
                afterLogin(login.getData().getUserId());
            }*/

        }
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onFailure(String url, String error) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 登录之后
     */
    private void afterLogin(String userId) {
        //用户信息加密后保存到本地
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("UserId", DesUtil.encrypt(userId, DesUtil.LOCAL_KEY));
        editor.putLong("LoginTime", System.currentTimeMillis());
        editor.commit();
        setResult(RESULT_OK);
        finish();
    }
}
