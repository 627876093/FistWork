package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import cn.com.zlct.diamondgo.MainActivity;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.NavView;
import cn.com.zlct.diamondgo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_guide)
    public ViewPager viewPagerGuide;
//    @BindView(R.id.nv_guide)
//    public NavView navView;

    private List<ImageView> imgData;

    @Override
    protected int getViewResId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init() {
        int[] imgId = new int[]{R.drawable.ic_guide0, R.drawable.ic_guide1, R.drawable.ic_guide2};
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imgData = new ArrayList<>();
        for (int i = 0; i < imgId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(layoutParams);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgId[i]);
            imgData.add(i, imageView);
        }
//        navView.setCount(imgId.length);
        viewPagerGuide.setAdapter(new GuideAdapter());
        viewPagerGuide.addOnPageChangeListener(this);
    }

    /**
     * viewPager监听
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (navView.getCount() > 0){
//            navView.setNavAddress(position, positionOffset);
//        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == imgData.size() - 1) {
//            llGuide.setVisibility(View.VISIBLE);
            intoMain(0);
        } else {
//            llGuide.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 从登录返回
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            intoMain(4);
        }
    }

    /**
     * 进入首页
     */
    private void intoMain(int page) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("page", page);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 导航页viewPager适配器
     */
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imgData.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imgData.get(position));
            return imgData.get(position);
        }
    }
}
