package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;

public class RetrievePswByEmailActivity extends BaseActivity implements OkHttpUtil.OnDataListener {

/*
* 邮箱找回
*
* */
    @BindView(R.id.toolbar_text)
    Toolbar toolbarText;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.ib_delEmail)
    ImageButton ibDelEmail;
    @BindView(R.id.et_Code)
    EditText etCode;
    @BindView(R.id.tv_getCode)
    TextView tvGetCode;
    private String email;
    private Handler handler = new Handler();
    private Gson gson = new GsonBuilder().create();
    private final int retryLimit = Constant.Integers.CodeRetryTime;//重试秒数上限
    private int timeNum = retryLimit;//获取验证码倒计时
    @Override
    protected int getViewResId() {
        return R.layout.activity_retrieve_psw_by_email;
    }

    @Override
    protected void init() {
        ToolBarUtil.initToolBar(toolbarText, "重置密码", "手机找回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.toolbar_back) {
                    onBackPressed();

                } else {//手机找回
                    startActivity(new Intent(getBaseContext(), RetrievePswByPhoneActivity.class));
                    onBackPressed();
                }
            }
        });
    }

    @OnClick({R.id.tv_getCode, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getCode:
                getCode();
                break;
            case R.id.tv_next:
                break;
        }
    }

    private void getCode() {
        if (tvGetCode.isSelected()){
            email = etEmail.getText().toString();
            if (etEmail.length() == 0) {
                ToastUtil.initToast(this, "邮箱不能为空");
            } else if (!email.matches(Constant.Strings.RegexEmail)) {
                ToastUtil.initToast(this, "请输入正确的邮箱");

            } else {
                tvGetCode.setSelected(false);
//                String jsonString = gson.toJson(new GetMobileCode(email));
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
                                if (etEmail.getText().toString().matches(Constant.Strings.RegexEmail)) {
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

    }

    @Override
    public void onFailure(String url, String error) {

    }
}
