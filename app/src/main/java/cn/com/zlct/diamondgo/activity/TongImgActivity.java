package cn.com.zlct.diamondgo.activity;

import android.view.View;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;

/**.
 *
 * Created by Administrator on 2017/6/19 0019.
 */
public class TongImgActivity extends BaseActivity {
    @Override
    protected int getViewResId() {
        return R.layout.activity_tongimg;
    }

    @Override
    protected void init() {
        ActionBarUtil.initActionBar(getSupportActionBar(), "通宝专区", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
