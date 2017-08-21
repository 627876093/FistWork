package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.AppContext;
import cn.com.zlct.diamondgo.MainActivity;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.BaseDialog;
import cn.com.zlct.diamondgo.custom.ConfirmDialog;
import cn.com.zlct.diamondgo.util.CacheUtil;
import cn.com.zlct.diamondgo.util.ConfigConstants;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.PreferencesUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar_text)
    public Toolbar toolbar;
    @BindView(R.id.tv_setCacheSize)
    public TextView tvCache;

    @Override
    protected int getViewResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        ToolBarUtil.initToolBar(toolbar, "设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        upCacheSize();
    }

    private void upCacheSize() {
        DecimalFormat decimal = new DecimalFormat("0.0");
        float cache = (CacheUtil.cacheSize() + ConfigConstants.cacheSize()) / 1024.0F;
        String cacheSize;
        if (cache < 1024) {
            if (cache <= 0) {
                cacheSize = "0K";
            } else {
                cacheSize = decimal.format(cache) + "K";
            }
        } else {
            cacheSize = decimal.format(cache / 1024) + "M";
        }
        tvCache.setText(cacheSize);
    }

    @OnClick({R.id.ll_setCache, R.id.tv_setAboutUs, R.id.tv_setQuestion, R.id.tv_setProtocol,
            R.id.tv_setLogout})
    public void setting(View v) {
        Intent intent=new Intent(SettingActivity.this,H5Activity.class);
        switch (v.getId()) {
            case R.id.ll_setCache://清理缓存
                CacheUtil.clearCache();
                ConfigConstants.clearCache();
                upCacheSize();
                ToastUtil.initToast(this, "缓存清理完成");
                break;
            case R.id.tv_setAboutUs://关于我们
                intent.putExtra("url", Constant.URL.Synopsis);
                intent.putExtra("title","关于我们");
                startActivity(intent);
                break;
            case R.id.tv_setQuestion://常见问题
                intent.putExtra("url", Constant.URL.Replicate);
                intent.putExtra("title","常见问题");
                startActivity(intent);
                break;
            case R.id.tv_setProtocol://服务协议
                intent.putExtra("url", Constant.URL.ServiceAgreement);
                intent.putExtra("title","服务协议");
                startActivity(intent);
                break;
            case R.id.tv_setLogout://退出登录
                ConfirmDialog confirmDialog = ConfirmDialog.newInstance("确认退出登录?", "取消", "确认");
                confirmDialog.setOnBtnClickListener(new ConfirmDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(View view) throws Exception {
                        SharedPreferences userSP = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userSP.edit();
                        editor.putString("UserId", null);
                        editor.putLong("LoginTime", new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01").getTime());
                        editor.commit();
                        ((AppContext) getApplication()).setCartCount(0);
                        SharedPreferencesUtil.saveUserInfo(SettingActivity.this, null);
                        SharedPreferencesUtil.saveUserAccount(SettingActivity.this, null);
                        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                confirmDialog.show(getFragmentManager(), "logout");
                break;
        }
    }

    private void clearData() {
        PreferencesUtil.clearData(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
