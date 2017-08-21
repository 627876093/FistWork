package cn.com.zlct.diamondgo.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.com.zlct.diamondgo.AppContext;
import okhttp3.FormBody;

/**
 * 保存用户信息工具类
 */
public class PreferencesUtil {

    /**
     * 保存用户信息到本地
     */
    public static void saveUserInfo(Context context, String data){
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("UserInfo", data);
        editor.commit();
    }

    /**
     * 保存用户信息到本地
     */
    public static void saveData(Context context, String preferName, String dataName, String data){
        SharedPreferences dataInfo = context.getSharedPreferences(preferName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataInfo.edit();
        editor.putString(dataName, data);
        editor.commit();
    }

    /**
     * 保存支付信息到本地
     */
    public static void saveWeChatPay(Context context, String name, String type, float money){
        SharedPreferences payInfo = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = payInfo.edit();
        editor.putString("PayType", type);
        editor.putFloat("PayMoney", money);
        editor.commit();
    }


    /**
     * 清除本地保存数据
     */
    public static void clearData(Activity activity) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("user", activity.MODE_PRIVATE).edit();
        editor.putString("UserId", null);
        editor.putBoolean("IsCertify", false);
        try {
            editor.putLong("LoginTime", new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editor.commit();
        ((AppContext) activity.getApplication()).setCartCount(0);
        saveUserInfo(activity, null);
    }

    /**
     * 从本地获取UserId
     */
    public static String getUserId(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences user = context.getSharedPreferences("user", context.MODE_PRIVATE);
        try {
            long loginTime = user.getLong("LoginTime", sdf.parse("2016-01-01").getTime());
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
     * 获取本地的用户信息
     */
    /*public static UserInfoEntity.DataEntity getUserInfo(Context context){
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        String info = userInfo.getString("UserInfo", null);
        if (info != null) {
            try {
                info = DesUtil.decrypt(info, DesUtil.LOCAL_KEY);
                UserInfoEntity infoEntity = new Gson().fromJson(info, UserInfoEntity.class);
                return infoEntity.getData();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }*/
}
