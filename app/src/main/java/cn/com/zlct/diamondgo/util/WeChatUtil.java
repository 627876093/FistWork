package cn.com.zlct.diamondgo.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import cn.com.zlct.diamondgo.model.UserPaymentEntity;
import cn.com.zlct.diamondgo.model.WeiChatPaymentEntity;

/**
 * 微信支付工具类
 * Created by Administrator on 2017/7/6 0006.
 */
public class WeChatUtil {

    public static String weChatPay(Context context, WeiChatPaymentEntity userRecharge){
//        String payInfo = userRecharge.getPrivateKey();
        if (userRecharge==null) {
            ToastUtil.initToast(context, "服务器异常，请稍候再试");
            return null;
        }
//{"appid":"wx74425f5021cc397b",
// "noncestr":"CA7YC445WLQ5144OKXADRCH5JIOJMNYN",
// "package":"Sign=WXPay",
// "partnerid":"1480907232",
// "prepayid":"wx20170808180521181333f9fb0247155889",
// "sign":"4CF15A1FB8A6C83A306FA07BCB4244F7",
// "timestamp":1502186721}

//        Map<String, String> mapInfo = splitInfo(context, payInfo);

        String appId = userRecharge.getAppid();
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);
        if (!api.isWXAppInstalled()) {
            ToastUtil.initToast(context, "您未安装微信");
            return null;
        }
        if(!api.isWXAppSupportAPI()) {
            ToastUtil.initToast(context, "当前版本不支持支付功能");
            return null;
        }
        if (userRecharge != null) {
            PayReq req = new PayReq();
//            req.appId = Contants.IdString.WeChat_APP_ID;
            req.appId = appId;
//            req.partnerId = Contants.IdString.WeChatPartnerId;
            req.partnerId = userRecharge.getPartnerid();
            req.prepayId = userRecharge.getPrepayid();//预支付交易会话ID 统一下单时返回
            req.packageValue = "Sign=WXPay";
            req.nonceStr = userRecharge.getNoncestr();//随机字符串 统一下单时返回
            req.timeStamp = userRecharge.getTimestamp();//时间戳 统一下单时返回
            req.sign = userRecharge.getSign();//签名
            api.sendReq(req);
            return "suc";
        } else {
            ToastUtil.initToast(context, "服务器异常，请稍候再试");
            return null;
        }
    }

    /**
     * 处理支付信息
     */
    public static Map<String, String> splitInfo(Context context, String payInfo) {
        try{
            String[] info = payInfo.split("&");
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < info.length; i++) {
                String[] items = info[i].split("=");
                map.put(items[0], items[1]);
            }
            return map;
        } catch (Exception e){
            Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
