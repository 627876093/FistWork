package cn.com.zlct.diamondgo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

import cn.com.zlct.diamondgo.R;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class SharedPreferencesUtil {

    /**
     * 保存用户信息到本地
     * @param context 上下文对象
     * @param data    保存的数据
     */
    public static void saveUserInfo(Context context, String data) {
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("UserInfo", data);
        editor.commit();
    }

    /**
     * 保存用户账户信息到本地
     * @param context 上下文对象
     * @param data    保存的数据
     */
    public static void saveUserAccount(Context context, String data) {
        SharedPreferences accountInfo = context.getSharedPreferences("userAccount", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountInfo.edit();
        editor.putString("UserAccount", data);
        editor.commit();
    }

    /**
     * 保存用户信息到本地
     *
     * @param context 上下文对象
     * @param data    保存的数据
     */
    public static void saveData(Context context, String preferName, String dataName, String data) {
        SharedPreferences dataInfo = context.getSharedPreferences(preferName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataInfo.edit();
        editor.putString(dataName, data);
        editor.commit();
    }

    /**
     * 保存支付信息到本地
     *
     * @param context 上下文对象
     * @param name    SharedPreferences名字
     * @param mode    保存的数据
     */
    public static void saveWeChatPay(Context context, String name, String mode, String money) {
        SharedPreferences payInfo = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = payInfo.edit();
        editor.putString("PayMode", mode);
        editor.putString("PayMoney", money);
        editor.commit();
    }

//    /**
//     * 修改用户数据
//     * @param context 上下文对象
//     * @param name 修改类别
//     * @param data 修改值
//     * @param dataListener 回调监听
//     */
//    public static void SubmitUserInfo(Context context, String name, String data, OkHttpUtil.OnDataListener dataListener){
//        String userId = SharedPreferencesUtil.getUserId(context);
//        Gson gson = new GsonBuilder().create();
//        String jsonString = gson.toJson(new UpUserBasicInfo(userId, name, data));
//        OkHttpUtil.postJson(Constant.URL.UpdateUserBasicInformation, DesUtil.encrypt(jsonString), dataListener);
//    }

    public static String getUserType(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences user = context.getSharedPreferences("user", context.MODE_PRIVATE);
        try {
            long loginTime = user.getLong("LoginTime", sdf.parse("2016-01-01").getTime());
            //30天换算成毫秒数会溢出，用long型表示
            if ((System.currentTimeMillis() - loginTime) > (30 * 24 * 60 * 60 * 1000L)) {//距离上次登录已超30天
                return "default";
            } else {//距离上次登录未超30天
                String userId = user.getString("UserId", "default");
                if (!"default".equals(userId)) {
                    userId = DesUtil.decrypt(userId, DesUtil.LOCAL_KEY);
                }
                return userId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default";
    }
    /**
     * 从本地获取UserId
     */
    public static String getUserId(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences user = context.getSharedPreferences("user", context.MODE_PRIVATE);
        try {
            long loginTime = user.getLong("LoginTime", sdf.parse("2016-01-01").getTime());
            //30天换算成毫秒数会溢出，用long型表示
            if ((System.currentTimeMillis() - loginTime) > (30 * 24 * 60 * 60 * 1000L)) {//距离上次登录已超30天
                return "default";
            } else {//距离上次登录未超30天
                String userId = user.getString("UserId", "default");
                if (!"default".equals(userId)) {
                    userId = DesUtil.decrypt(userId, DesUtil.LOCAL_KEY);
                }
                return userId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default";
    }

    /**
     * 从本地获取phone
     */
    public static String getPhone(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences user = context.getSharedPreferences("user", context.MODE_PRIVATE);
        try {
            long loginTime = user.getLong("LoginTime", sdf.parse("2016-01-01").getTime());
            //30天换算成毫秒数会溢出，用long型表示
            if ((System.currentTimeMillis() - loginTime) > (30 * 24 * 60 * 60 * 1000L)) {//距离上次登录已超30天
                return "default";
            } else {//距离上次登录未超30天
                String userId = user.getString("UserId", "default");
                if (!"default".equals(userId)) {
                    userId = DesUtil.decrypt(userId, DesUtil.LOCAL_KEY);
                }
                return userId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default";
    }


    /**
     * 分享
     *
     * @param context
     * @param title
     * @param url
     * @param text
     * @param imgPath
     * @param listener
     */
    public static void showShare(Context context, String title, String url, String text, String imgPath,
                                 View.OnClickListener listener) {
        OnekeyShare oks = new OnekeyShare();
        //追加复制按钮
//        Bitmap copy = BitmapFactory.decodeResource(context.getResources(), R.drawable.copy_link);
//        oks.setCustomerLogo(copy, "复制链接", listener);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setTitle(title);// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setText(text); // text是分享文本，所有平台都需要这个字段
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imgPath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("大家快来注册！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(context);
    }

//    /**
//     * 从本地获取userCode、mobile
//     */
//    public static String getUserCodeAndMobile(Context context){
//        String userId = getUserId(context);
//        if ("default".equals(userId)) {
//            return "UserCodeAndMobile:null";
//        } else {
//            UserInfoEntity.DataEntity userInfo = PhoneUtil.getUserInfo(context);
//            if (userInfo != null) {
//                return "UserCode:" + userInfo.getUserCodeLong() + "   Mobile:" + userInfo.getMobile();
//            } else {
//                return "UserId:" + userId;
//            }
//        }
//    }
//
//    /**
//     * 拼接收货地址
//     */
//    public static String stitchShippingAddress(ShippingAddress.DataEntity.ShippingAddressEntity userShippingAddress){
//        String shippingAddress = userShippingAddress.getProvince() + "_" + userShippingAddress.getCity() + "_" +
//                userShippingAddress.getCounty() + "_" + userShippingAddress.getAddress();
//        return userShippingAddress.getRealName() + "|" + userShippingAddress.getMobile() + "|" +
//                userShippingAddress.getZipCode() + "|" + shippingAddress;
//    }
}
