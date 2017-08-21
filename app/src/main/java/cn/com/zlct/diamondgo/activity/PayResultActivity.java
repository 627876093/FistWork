package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;


/**
 * 购买结果页面
 */
public class PayResultActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_payResultImg)
    public ImageView ivImg;
    @BindView(R.id.tv_payResultState)
    public TextView tvState;
    @BindView(R.id.tv_payResultCause)
    public TextView tvCause;
    @BindView(R.id.tv_paytext)
    public TextView tv_paytext;

    //    @BindView(R.id.tv_alipayTips)
    public TextView tvTips;
    private int type;//是支付还是充值, 0 -- 支付、 1 -- 充值
    private String payType;
    private String payMode;//支付方式
    private boolean payResult;//支付结果

    private int countTime;
    private Handler handler;
    @Override
    protected int getViewResId() {
        return R.layout.activity_payment_result;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        payType = type == 0 ? "支付" : "充值";
        String result = intent.getStringExtra("result");
        payMode = intent.getStringExtra("payMode");
        Log.e("loge", "result: " + result + " payMode: " + payMode);
        ActionBarUtil.initActionBar(getSupportActionBar(), "支付", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTime = 0;
            }
        });

        if ("ali".equals(payMode)) {
//            tvTips.setVisibility(View.VISIBLE);
            initAli(result);
        } else if ("weChat".equals(payMode)) {
//            tvTips.setVisibility(View.VISIBLE);
            initWeChat(result);
        } else if ("jd".equals(payMode)) {
//            tvTips.setVisibility(View.VISIBLE);
            initJDPay(result);
        } else {
            if ("success".equals(result)) {
                paySuccess();
            } else {
                payFail();
            }
        }

        countTime = 3;//初始5秒
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (handler != null) {
                    tv_paytext.setText(countTime + " 秒  后返回购物车");
                    if (countTime <= 0) {
                        onBackPressed();
                    } else {
                        countTime--;
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        });
    }

    public void initAli(String status) {
        if ("9000".equals(status)) {
            paySuccess();
        } else {
            payFail();
            switch (status) {
                case "8000"://正在处理中
                    tvCause.setText("正在处理中");
                    break;
                case "4000"://订单支付失败
                    tvCause.setText("支付失败");
                    break;
                case "6001"://用户中途取消
                    tvCause.setText("用户中途取消");
                    break;
                case "6002"://网络连接出错
                    tvCause.setText("网络连接出错");
                    break;
            }
        }
    }

    public void initWeChat(String status) {
        if ("0".equals(status)) {
            paySuccess();
        } else {
            payFail();
            if ("-1".equals(status)) {
                tvCause.setText("支付失败");
            } else if ("-2".equals(status)) {
                tvCause.setText("用户中途取消");
            }
        }
    }

    private void initJDPay(String status) {
        if ("success".equals(status)) {
            paySuccess();
        } else {
            payFail();
        }
    }

    private void paySuccess() {
        payResult = true;
        ivImg.setImageResource(R.drawable.pay_success);
        tvState.setText(payType + "成功");
    }

    private void payFail() {
        payResult = false;
        ivImg.setImageResource(R.drawable.pay_fail);
        tvState.setText(payType + "失败");
    }

    @Override
    public void onBackPressed() {
        handler = null;
        Intent intent = new Intent();
        intent.putExtra("result", payResult);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        countTime = 0;
    }
}
