package cn.com.zlct.diamondgo.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.fragment.IntegralVPFragment;
import cn.com.zlct.diamondgo.util.ActionBarUtil;

/**
 * 我的积分
 * Created by Administrator on 2017/5/23 0023.
 */
public class MyIntegralActivity extends BaseActivity {


    @BindView(R.id.order_TabLayout)
    public TabLayout tabLayout;
    @BindView(R.id.order_ViewPager)
    public ViewPager viewPager;

    @Override
    protected int getViewResId() {
        return R.layout.activity_order;
    }

    @Override
    protected void init() {
        ActionBarUtil.initActionBar(getSupportActionBar(), "我的积分", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String[] types = {"1", "1,2,3,4,5,6,7,8"};
        DetailVPAdapter viewPagerAdapter = new DetailVPAdapter(getSupportFragmentManager(), types);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * ViewPager适配器
     */
    class DetailVPAdapter extends FragmentPagerAdapter {

        private String[] types;

        public DetailVPAdapter(FragmentManager fm, String[] types) {
            super(fm);
            this.types = types;
        }

        @Override
        public Fragment getItem(int position) {
            IntegralVPFragment instance = IntegralVPFragment.newInstance(types[position], position);
            return instance;
        }

        @Override
        public int getCount() {
            return types.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] FinanTabNames = {"通宝积分", "现金积分"};
            return FinanTabNames[position];
        }
    }
}
