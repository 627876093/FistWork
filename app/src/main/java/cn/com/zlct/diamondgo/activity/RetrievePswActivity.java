package cn.com.zlct.diamondgo.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.model.updatePasswodEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import okhttp3.FormBody;

public class RetrievePswActivity extends BaseActivity implements OkHttpUtil.OnDataListener {
    /*
    * 密码找回
    *
    * */
    @BindView(R.id.toolbar_text)
    Toolbar toolbarText;
    @BindView(R.id.et_Password1)
    EditText etPassword1;
    @BindView(R.id.ib_delPassword1)
    ImageButton ibDelPassword1;
    @BindView(R.id.et_Password2)
    EditText etPassword2;
    @BindView(R.id.ib_delPassword2)
    ImageButton ibDelPassword2;
    private String phone;

    @Override
    protected int getViewResId() {
        return R.layout.activity_retrieve_psw;

    }

    @OnTextChanged(value = R.id.et_Password1, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etPasswordChanged1(CharSequence text) {
        PhoneUtil.isEditError2WithDel(text.toString(), etPassword1, ibDelPassword1);

    }

    @OnTextChanged(value = R.id.et_Password2, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etPasswordChanged2(CharSequence text) {
        PhoneUtil.isEditError2WithDel(text.toString(), etPassword2, ibDelPassword2);

    }

    @Override
    protected void init() {
        ToolBarUtil.initToolBar(toolbarText, "重置密码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        phone = getIntent().getStringExtra("phone");

    }

    @OnClick({R.id.ib_delPassword1, R.id.tv_Retrieve, R.id.ib_delPassword2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_delPassword1:
                etPassword1.setText(null);
                etPassword1.requestFocus();
                break;
            case R.id.ib_delPassword2:
                etPassword2.setText(null);
                etPassword2.requestFocus();
                break;
            case R.id.tv_Retrieve:
                String psw1 = etPassword1.getText().toString();
                String psw2 = etPassword2.getText().toString();
                if (TextUtils.isEmpty(psw1) || TextUtils.isEmpty(psw2)) {
                    ToastUtil.initToast(this, "请输入密码");
                } else if (psw1.length() < 6) {
                    ToastUtil.initToast(this, "请输入6位以上密码");
                } else if (!psw1.equals(psw2)) {
                    ToastUtil.initToast(this, "两次输入密码不匹配");

                } else {
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("phone", phone);
                    builder.add("password", psw1);
                    OkHttpUtil.postJson(Constant.URL.updatePasswod,builder, this);
                }
                break;
        }
    }

    @Override
    public void onResponse(String url, String json) {
        Log.e("loge", "onResponse: " + json);
        String decrypt = DesUtil.decrypt(json);
        Gson gson = new Gson();
        updatePasswodEntity updatePasswodEntity = gson.fromJson(json, updatePasswodEntity.class);
        if ("success".equals(updatePasswodEntity.getMsg())) {
            ToastUtil.initToast(this, "修改成功");
            finish();
        } else {
            ToastUtil.initToast(this, "修改失败");

        }
    }

    @Override
    public void onFailure(String url, String error) {

    }
}
