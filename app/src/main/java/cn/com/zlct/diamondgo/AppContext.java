package cn.com.zlct.diamondgo;

import android.app.Application;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wenming.library.LogReport;
import com.wenming.library.save.imp.CrashWriter;

import cn.com.zlct.diamondgo.util.ConfigConstants;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class AppContext extends Application {

    private int cartCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtil.initOkHttp();
        Fresco.initialize(this, ConfigConstants.getImagePipelineConfig(this));
        JPushInterface.init(this);
//        ActiveAndroid.initialize(this);
        initCrashReport();

    }

    private void initCrashReport() {
        LogReport.getInstance()
                    //定义路径为：Android/data/[PackageName]/cLog
                .setLogDir(getApplicationContext(), Environment.getExternalStorageDirectory() +
                        "/Android/data/" + this.getPackageName() + "/")
                .setLogSaver(new CrashWriter(getApplicationContext()))
                .init(getApplicationContext());
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    /**
     * app退出时调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
//        ActiveAndroid.dispose();
    }
}
