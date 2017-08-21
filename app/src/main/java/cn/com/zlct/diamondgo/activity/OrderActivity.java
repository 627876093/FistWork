package cn.com.zlct.diamondgo.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.AppContext;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.custom.CircleView;
import cn.com.zlct.diamondgo.fragment.PurRecordVPFragment;
import cn.com.zlct.diamondgo.model.GetUserOrder;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.AnimationUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;

/**
 * 商城订单
 * Created by Administrator on 2017/5/22 0022.
 */
public class OrderActivity extends BaseActivity
//        implements  BaseFragment.OnCartUpListener
{

    @BindView(R.id.order_TabLayout)
    public TabLayout tabLayout;
    @BindView(R.id.order_ViewPager)
    public ViewPager viewPager;

//    private CircleView cartRed;

    private String phone;
    private ViewGroup animLayout;//动画层
    private Handler handler = new Handler();
    int type;

    @Override
    protected int getViewResId() {
        return R.layout.activity_order;
    }

    @Override
    protected void init() {
        phone = SharedPreferencesUtil.getUserId(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

//        cartRed =
//        getSupportActionBar()
        ActionBarUtil.initActionBar(getSupportActionBar(), "订单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        , new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentCart = new Intent(this, CartActivity.class);
//                startActivity(intentCart);
//            }
//        }
//        PhoneUtil.upCartDot(getApplication(), cartRed);
        initViewPager();
    }

    private void initViewPager() {
        List<String> jsonList = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        for (int i = 0; i < 5; i++) {
            String jsonString = gson.toJson(new GetUserOrder(phone, i + ""));
            jsonList.add(i, jsonString);
        }
        UserOrderVPAdapter viewPagerAdapter = new UserOrderVPAdapter(getSupportFragmentManager(), jsonList);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(type);
        tabLayout.setupWithViewPager(viewPager);
    }

//    @Override
//    public void onCartAnim(View view, final String url) {
//        if (animLayout == null) {
//            animLayout = AnimationUtil.createAnimLayout(this);
//        }
//        int[] start_loc = new int[2];
//        view.getLocationInWindow(start_loc);
//        final SimpleDraweeView sdv = (SimpleDraweeView) AnimationUtil.addViewToAnimLayout(this, view, start_loc);
//        sdv.setBackgroundResource(R.drawable.img_loading);
//        animLayout.addView(sdv);
//        int[] end_loc = new int[2];
//        cartRed.getLocationInWindow(end_loc);
//        int endX = (end_loc[0] - cartRed.getWidth() / 3) - (start_loc[0] + view.getWidth() / 2);
//        int endY = (end_loc[1] + cartRed.getHeight()) - (start_loc[1] + view.getHeight() / 2);
//
//        AnimatorSet animatorSet = AnimationUtil.propertyAnimation(sdv, endX, endY);
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {}
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                sdv.setVisibility(View.GONE);
//            }
//            @Override
//            public void onAnimationCancel(Animator animation) {}
//            @Override
//            public void onAnimationRepeat(Animator animation) {}
//        });
//        animatorSet.start();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (!TextUtils.isEmpty(url)) {
//                    sdv.setImageURI(Uri.parse(Constant.URL.BaseImg + url));
//                } else {
//                    sdv.setBackgroundResource(R.drawable.img_fail);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onCartUp(int count) {
//        ((AppContext) getApplication()).setCartCount(count);
//        PhoneUtil.upCartDot(getApplication(), cartRed);
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        PhoneUtil.upCartDot(getApplication(), cartRed);
    }

    @Override
    protected void onStop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (animLayout != null && animLayout.getChildCount() > 0) {
                    animLayout.removeAllViews();
                }
            }
        });
        super.onStop();
    }


    /**
     * ViewPager适配器
     */
    class UserOrderVPAdapter extends FragmentPagerAdapter {

        private List<String> jsonList;

        public UserOrderVPAdapter(FragmentManager fm, List<String> jsonList) {
            super(fm);
            this.jsonList = jsonList;
        }

        @Override
        public Fragment getItem(int position) {
            PurRecordVPFragment recordFragment = PurRecordVPFragment.newInstance(jsonList.get(position), position);
//            recordFragment.setOnCartUpListener(OrderActivity.this);
            return recordFragment;
        }

        @Override
        public int getCount() {
            return jsonList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] FinanTabNames = {"全部", "待付款", "待发货", "待收货", "待退款"};
            return FinanTabNames[position];
        }
    }
}
