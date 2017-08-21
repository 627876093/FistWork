package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * 余额提现
 * Created by Administrator on 2017/7/4 0004.
 */
public class MoneyWithdrawalsActivity extends BaseActivity {
    @BindView(R.id.layout1)
    public LinearLayout layout1;
    @BindView(R.id.layout2)
    public LinearLayout layout2;
    @BindView(R.id.et_yj)
    EditText et_yj;
    @BindView(R.id.btn_tj)
    Button btn_tj;
    @BindView(R.id.tv_myYJ)
    TextView tv_myYJ;
    @BindView(R.id.et_ali)
    EditText et_ali;
    @BindView(R.id.et_name)
    EditText et_name;

    double money=0.0;
    private LoadingDialog loadingDialog;

    @Override
    protected int getViewResId() {
        return R.layout.activity_moneywithdrawals;
    }

    @Override
    protected void init() {
        //初始化ActionBar
        ActionBarUtil.initActionBar(getSupportActionBar(), "余额提现", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        money=getIntent().getDoubleExtra("money",0);
        tv_myYJ.setText("可提余额:"+money);

        btn_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(et_yj.getText().toString().trim()+"")%100==0){
                    if (Integer.parseInt(et_yj.getText().toString().trim()+"")>=100){
                        if ((et_ali.getText().toString().trim()+"").length()==0){
                            ToastUtil.initToast(MoneyWithdrawalsActivity.this,"请输入支付宝账号");
                        }else if ((et_name.getText().toString().trim()+"").length()==0){
                            ToastUtil.initToast(MoneyWithdrawalsActivity.this,"请输入支付宝账号实名姓名");
                        }else {

                            loadingDialog = LoadingDialog.newInstance("加载中...");
                            loadingDialog.show(getFragmentManager());

                            FormBody.Builder builder = new FormBody.Builder();
                            builder.add("phone", SharedPreferencesUtil.getPhone(MoneyWithdrawalsActivity.this));
                            builder.add("money", et_yj.getText().toString().trim()+"");
                            builder.add("zfbId", et_ali.getText().toString().trim()+"");
                            builder.add("name", et_name.getText().toString().trim()+"");
                            OkHttpUtil.postJson(Constant.URL.addWithdrawals, builder, new OkHttpUtil.OnDataListener() {
                                @Override
                                public void onResponse(String url, String json) {
                                    Log.e("loge",json);
                                    dismissLoading();
                                    SingleWordEntity singleWordEntity=new GsonBuilder().create().fromJson(json,SingleWordEntity.class);
                                    if (singleWordEntity.getMsg().equals("success")){
                                        ToastUtil.initToast(MoneyWithdrawalsActivity.this,"提现成功");
                                        setResult(RESULT_OK,new Intent().putExtra("money",et_yj.getText().toString().trim()+""));
                                        finish();
                                    }else {
                                        ToastUtil.initToast(MoneyWithdrawalsActivity.this,"提现失败");
                                    }
                                }
                                @Override
                                public void onFailure(String url, String error) {}
                            });
                        }
                    }else {
                        ToastUtil.initToast(MoneyWithdrawalsActivity.this,"请输入100的倍数");
                    }
                }else {
                    ToastUtil.initToast(MoneyWithdrawalsActivity.this,"请输入100的倍数");
                }
            }
        });
    }


    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
