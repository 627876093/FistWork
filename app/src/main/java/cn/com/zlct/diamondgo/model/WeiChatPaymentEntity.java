package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * 微信支付
 * Created by Administrator on 2017/7/8 0008.
 */
public class WeiChatPaymentEntity {
    /**
     * "appid":"wx74425f5021cc397b",
     * "noncestr":"GT2JQRCSHR5ZFJUTGNXG546J0IMQYKR7",
     * "package":"Sign=WXPay",
     * "partnerid":"1480907232",
     * "prepayid":"wx20170708163605c902b4beda0149332744",
     * "sign":"7F5783EA79A096BE5FD492FF77A1E725",
     * "timestamp":1499503034
     */


    String appid;
    String noncestr;
    String partnerid;
    String prepayid;
    String sign;
    String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
