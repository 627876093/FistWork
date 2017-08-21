package cn.com.zlct.diamondgo.activity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnTextChanged;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.PreferencesUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import cn.com.zlct.diamondgo.util.Util;
import okhttp3.FormBody;

/**
 * 昵称
 */
public class EditAliasActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_text)
    public Toolbar toolbar;
    @BindView(R.id.et_editAlias)
    public EditText etAlias;

    private String oldAlias;
    private LoadingDialog loadingDialog;
    UserInfoEntity userInfoEntity;

    @Override
    protected int getViewResId() {
        return R.layout.activity_edit_alias;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        ToolBarUtil.initToolBar(toolbar, "昵称", "保存", this);

        oldAlias = getIntent().getStringExtra("old");
        if (oldAlias.equals("点击修改")){
            etAlias.setText("");
        }else {
            etAlias.setText(oldAlias);
        }
        etAlias.setSelection(etAlias.getText().length());
    }

    @OnTextChanged(value = R.id.et_editAlias, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etTextChanged(CharSequence text) {
        Util.isEditError2(text.toString(), etAlias);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                onBackPressed();
                break;
            case R.id.toolbar_next://保存
                String alias = etAlias.getText().toString();
                if (etAlias.getText().length() == 0) {
                    ToastUtil.initToast(this, "昵称不能为空");
                } else if (alias.contains("\"") || alias.contains("\\")) {
                    etAlias.setText(alias.replace("\"", " ").replace("\\", " ").trim());
                    onClick(v);
                } else if (alias.equals(oldAlias)) {
                    ToastUtil.initToast(this, "昵称未改变 无需提交");
                } else {
                    Util.hideKeyboard(v);
                    loadingDialog = LoadingDialog.newInstance("修改昵称中...");
                    loadingDialog.show(getFragmentManager());
                    OkHttpUtil.postJson(Constant.URL.updateUserInfo, userInfoEntity.getPhone(), alias,
                            userInfoEntity.getSex(), userInfoEntity.getHeadURL(),
                            new OkHttpUtil.OnDataListener() {
                        @Override
                        public void onResponse(String url, String json) {
                            Log.e("loge", "修改昵称" + json);

                            loadingDialog.dismiss();
                            if(true){
                                getUserInfo();
                            }
                        }

                        @Override
                        public void onFailure(String url, String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.initToast(EditAliasActivity.this, "修改失败，请再次上传");
                                    loadingDialog.dismiss();
                                }
                            });
                        }
                    });
                }
                break;
        }
    }

    protected void getUserInfo(){
        //查询个人信息
        String phone= SharedPreferencesUtil.getPhone(this);
        OkHttpUtil.postJson(Constant.URL.getUserInfo, "phone", phone, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                UserInfoEntity userInfoEntity = new Gson().fromJson(json, UserInfoEntity.class);
//                setUIView(userInfoEntity);//设置UI
                SharedPreferencesUtil.saveUserInfo(EditAliasActivity.this, DesUtil.encrypt(json, DesUtil.LOCAL_KEY));

                finish();
            }

            @Override
            public void onFailure(String url, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getUserInfo();
                    }
                });
            }
        });
    }

}
