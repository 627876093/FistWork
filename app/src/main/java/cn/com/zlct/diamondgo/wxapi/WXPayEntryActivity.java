package cn.com.zlct.diamondgo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.PayResultActivity;
import cn.com.zlct.diamondgo.util.Constant;


/**
 * 微信支付回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);

        api = WXAPIFactory.createWXAPI(this, Constant.IdString.WeChat_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("loge", "onPayFinish,Code=" + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            SharedPreferences weChatPay = getSharedPreferences("weChat", MODE_PRIVATE);
            String mode = weChatPay.getString("PayMode", null);
            Intent intent = new Intent(this, PayResultActivity.class);
            intent.putExtra("payMode", "weChat");
            intent.putExtra("result", String.valueOf(resp.errCode));
            if ("pay".equals(mode)) {
                intent.putExtra("type", 0);
            } else {
                intent.putExtra("type", 1);
            }
            startActivity(intent);
            finish();
        }
    }
}
