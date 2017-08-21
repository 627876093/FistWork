package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.MainActivity;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;

/**
 * 启动页
 */
public class LogoActivity extends BaseActivity {

    @BindView(R.id.tv_skip)
    public TextView tvSkip;

    private boolean isFirst;//是否首次启动
    private int countTime;
    private Handler handler = new Handler();

    @Override
    protected int getViewResId() {
        return R.layout.activity_logo;
    }

    @Override
    protected void init() {
        SharedPreferences isFirstShared = getSharedPreferences("isFirst", MODE_PRIVATE);
        isFirst = isFirstShared.getBoolean("IsFirstOpen", true);

        countTime = 0;//初始秒数
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (handler != null) {
                    tvSkip.setText(countTime + "s  跳过>>");
                    if (countTime <= 0) {
                        intoNextPage();
                    } else {
                        countTime--;
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        });

        SharedPreferences.Editor editor = isFirstShared.edit();
        if (isFirst) {
            editor.putBoolean("IsFirstOpen", false);
        }
        editor.putBoolean("IsVerFirst", true);
        editor.commit();
    }

    public void intoNextPage() {
        startActivity(new Intent(this, GuideActivity.class));
        finish();
    }

    @OnClick(R.id.tv_skip)
    public void skip(){
        handler = null;
        countTime = 0;
        intoNextPage();
    }
}
