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
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * 克拉提现
 * Created by Administrator on 2017/6/14 0014.
 */

public class MyWithdrawalsActivity extends BaseActivity{
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

    double money=0.0;

    @Override
    protected int getViewResId() {
        return R.layout.activity_withdrawals;
    }

    @Override
    protected void init() {
        //初始化ActionBar
        ActionBarUtil.initActionBar(getSupportActionBar(), "克拉提现", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        money=getIntent().getDoubleExtra("money",0);
        tv_myYJ.setText("可提克拉:"+money);

        btn_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(et_yj.getText().toString().trim()+"")%100==0){
                    if (Integer.parseInt(et_yj.getText().toString().trim()+"")>=100){
                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("phone", SharedPreferencesUtil.getPhone(MyWithdrawalsActivity.this));
                        builder.add("money", et_yj.getText().toString().trim()+"");
                        OkHttpUtil.postJson(Constant.URL.userWithdrawals, builder, new OkHttpUtil.OnDataListener() {
                            @Override
                            public void onResponse(String url, String json) {
                                Log.e("loge",json);
                                SingleWordEntity singleWordEntity=new GsonBuilder().create().fromJson(json,SingleWordEntity.class);
                                if (singleWordEntity.getMsg().equals("success")){
                                    ToastUtil.initToast(MyWithdrawalsActivity.this,"提现成功");
                                    setResult(RESULT_OK,new Intent().putExtra("money",et_yj.getText().toString().trim()+""));
                                    finish();
                                }else {
                                    ToastUtil.initToast(MyWithdrawalsActivity.this,"提现失败");
                                }
                            }

                            @Override
                            public void onFailure(String url, String error) {

                            }
                        });
                    }else {
                        ToastUtil.initToast(MyWithdrawalsActivity.this,"请输入100的倍数");
                    }
                }else {
                    ToastUtil.initToast(MyWithdrawalsActivity.this,"请输入100的倍数");
                }
            }
        });
    }
}
