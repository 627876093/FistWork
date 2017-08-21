package cn.com.zlct.diamondgo.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

/**
 * 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    protected OnCartUpListener cartListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle(savedInstanceState);
        setContentView(getViewResId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//手机竖屏
        unbinder = ButterKnife.bind(this);
        init();
        loadData();
    }

    protected void getBundle(Bundle state){}

    protected abstract int getViewResId();

    protected void init(){}

    protected void loadData(){}

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
    public void setOnCartUpListener(OnCartUpListener cartListener) {
        this.cartListener = cartListener;
    }

    public interface OnCartUpListener {
        void addCartUp();
    }
}
