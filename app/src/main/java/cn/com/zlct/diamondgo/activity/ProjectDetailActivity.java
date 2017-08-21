package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.ProjectDetailAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.custom.AutoScrollViewPager;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.custom.NavView;
import cn.com.zlct.diamondgo.model.ProjectCommodityEntity;
import cn.com.zlct.diamondgo.model.ProjectDataEntity;
import cn.com.zlct.diamondgo.model.ProjectImgEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.BitMapUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.PreferencesUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * 商品详情
 * Created by Administrator on 2017/5/18 0018.
 */

public class ProjectDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener, AbsRecyclerViewAdapter.OnItemClickListener, OnAdapterCallbackListener {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_project)
    RecyclerView recyclerView;

    @BindView(R.id.iv_gwc)
    ImageView iv_gwc;
    @BindView(R.id.iv_sc)
    ImageView iv_sc;
    @BindView(R.id.tv_addCart)
    TextView tv_addCart;
    @BindView(R.id.tv_addpay)
    TextView tv_addpay;


    @BindView(R.id.ll_HeadFloating)
    public LinearLayout floatingHead;//悬浮的头部
    @BindView(R.id.tL_TabLayout)
    public TabLayout tabLayout;//悬浮的TabLayout

    private TabLayout tabLayoutHead;//第二个头部中的TabLayout

    AutoScrollViewPager lbtViewPager;
    TextView tv_xjf;
    NavView navView;
    TextView tv_nameProject, tv_textProject, tv_numProject;
    TextView tv_moneyProject;
    TextView tv_oneRebate, tv_twoRebate;
    ProjectDetailAdapter adapter;//评论，参数,图片Adapter
    ProjectDetailAdAdapter projectDetailAdAdapter;//轮播图Adapter
    String[] strView;//轮播图数组
    String projectID;
    String userId;
    Gson gson = new GsonBuilder().create();

    private LoadingDialog loadingDialog;
    List<ProjectCommodityEntity> list;
    ProjectCommodityEntity projectEntity;

    String payIntegral;
    UserInfoEntity u;
    double dMoney = 0;

    private boolean booCollection;//是否收藏
    private boolean booCheckShop;//是否加入购物车
    String commodity_name, sort_name, commodity_price, commodity_type, commodity_url;

    @Override
    protected int getViewResId() {
        return R.layout.activity_projectdetail;
    }

    @Override
    protected void init() {
        u = PhoneUtil.getUserInfo(this);
        ActionBarUtil.initActionBarWithIcon(getSupportActionBar(), "商品详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }, R.drawable.icon_fx, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()) {//当前未登录
                    startActivityForResult(new Intent(ProjectDetailActivity.this, LoginActivity.class), 0);
                } else {
                String filePath = Environment.getExternalStorageDirectory() + "/Android/data/" +
                        getPackageName() + "/cache/logo.jpg";
                if (!new File(filePath).exists()) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sharelogo);
                    BitMapUtil.saveBitmap2File(bitmap, filePath);
                }
                String url = Constant.URL.BaseUrl+"userLogin/recommendUrl.do?recommPhone="+SharedPreferencesUtil.getPhone(ProjectDetailActivity.this);
                SharedPreferencesUtil.showShare(ProjectDetailActivity.this, "钻购商城", url,
                        "让世界发现您", filePath, null);
            }}
        });
//        ActionBarUtil.initActionBar(getSupportActionBar(), "商品详情", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        projectID = getIntent().getStringExtra("projectId");
//        loadingDialog = LoadingDialog.newInstance("加载中");
//        loadingDialog.show(getFragmentManager(), "loading");

        floatingHead.setVisibility(View.GONE);
        LinearLayout llHead = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.item_projecthead, null, false);
        LinearLayout tabHead = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.item_tab, null, false);
        initViewPager(llHead, tabHead);
        initTabLayout();
        initRecyclerView(llHead, tabHead);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void loadData() {
        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getFragmentManager(), "loading");
        list = new ArrayList<>();
        getProjectData();
    }

    private void getProjectData() {
        //商品详情
        if (projectID != null) {
            OkHttpUtil.postJson(Constant.URL.appgetCommoditybyId, "id", projectID, new OkHttpUtil.OnDataListener() {
                @Override
                public void onResponse(String url, String json) {
                    Log.e("loge", "商品详情" + json);
//{"manufacturerAddress":"广州市白云区嘉禾街长红村仙师街坑背工业区自编8号一楼","produceDate":"2017年6月1日",
// "modelCode":"","commodity_parameter":"组合套装","desc_address":"/shoppingManagementSystem/CommodityUpload/1496474910274.jpg",
// "normsType":"组合套装","approvalCode":"GD·FDA(2014)","goodsCode":"","msg":"success","brand_name":"溙谷堂",
// "commodity_name":"溙谷堂护肤组合套装","commodity_integral":"100","sort_name":"个护美肤","netWeight":"","color":"",
// "commodity_price":"7000.0","quality":"3年","cdy_description":"应激疗法 真正让皮肤健康起来","barCode":"6928098009924",
// "generateCode":"卫妆准字29-XK-3855号","manufacturerName":"广州薏莉雅生物技术开发有限公司",
// "picture_address":"/shoppingManagementSystem/CommodityUpload/1496474910273.jpg;/shoppingManagementSystem/CommodityUpload/1496474910273.jpg"}


                    ProjectImgEntity p = gson.fromJson(json, ProjectImgEntity.class);
                    projectEntity = new ProjectCommodityEntity();
                    projectEntity.setImgList(p);

//                    projectEntity=gson.fromJson(json, ProjectCommodityEntity.class);
                    getProjectComment();
                    if (isLogin()) {
                        getCommodityCollection();//获取是否收藏
                        getCheckShopping();//获取是否加入购物车
                    }
                    try {
                        //设置轮播图
                        setTopViewPage(p.getPicture_address());
                        //设置UI
                        setProjectView(new JSONObject(json));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String url, String error) {

                }
            });
        }
    }

    private void getProjectComment() {
        //商品评论
        OkHttpUtil.postJson(Constant.URL.appQuery, "commodity_id", projectID, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", "商品评论" + json);
                dismissLoading();
                ProjectDataEntity p = gson.fromJson(json, ProjectDataEntity.class);
                projectEntity.setDataList(p);
                projectEntity.setType(tabLayout.getSelectedTabPosition());

//                projectEntity=gson.fromJson(json, ProjectCommodityEntity.class);
//                projectEntity.setType(tabLayout.getSelectedTabPosition());

                list.add(projectEntity);
                adapter.setData(list);
            }

            @Override
            public void onFailure(String url, String error) {
            }
        });
    }

    private void getCommodityCollection() {
        //获取是否收藏
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("commodity_id", projectID);
        builder.add("phone", userId);
        OkHttpUtil.postJson(Constant.URL.getCommodityCollection, builder, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", "是否收藏" + json);
                //{"msg":"error"}
                try {
                    JSONObject object = new JSONObject(json);
                    if (object.getString("msg").equals("error")) {
                        booCollection = true;
                        iv_sc.setImageResource(R.drawable.icon_sc_gray);
                    } else {
                        booCollection = false;
                        iv_sc.setImageResource(R.drawable.icon_sc_red);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String url, String error) {

            }
        });
    }

    private void getCheckShopping() {
        //获取是否加入购物车
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("commodityId", projectID);
        builder.add("phone", userId);
        OkHttpUtil.postJson(Constant.URL.checkShopping, builder, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", "是否加入购物车" + json);
                //{"phone":"13705038428","commodityId":"66","msg":"success"}
                try {
                    JSONObject object = new JSONObject(json);
                    if (object.getString("msg").equals("error")) {
                        booCheckShop = false;
                        iv_gwc.setImageResource(R.drawable.icon_gwc_red);
                    } else {
                        booCheckShop = true;
                        iv_gwc.setImageResource(R.drawable.icon_gwc_gray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String url, String error) {

            }
        });

    }


    @OnClick({R.id.iv_gwc, R.id.iv_sc, R.id.tv_addCart, R.id.tv_addpay})
    public void setViewOnClick(View v) {
        if (!isLogin()) {//当前未登录
            startActivityForResult(new Intent(this, LoginActivity.class), 0);
        } else {

            if (commodity_name == null) {
                ToastUtil.initToast(ProjectDetailActivity.this, "数据加载中，请稍后再试");
                return;
            }
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("commodityId", projectID);
            builder.add("phone", userId);
            builder.add("commodity_name", commodity_name);
            builder.add("sort_name", sort_name);
            builder.add("commodity_price", commodity_price);


            loadingDialog = LoadingDialog.newInstance("加载中");
            loadingDialog.show(getFragmentManager(), "loading");


            switch (v.getId()) {
                case R.id.iv_gwc:
                case R.id.tv_addCart:
                    if (booCheckShop) {
                        //加入购物车
                        builder.add("count", "1");
                        OkHttpUtil.postJson(Constant.URL.shoppingCartAdd, builder, new OkHttpUtil.OnDataListener() {
                            @Override
                            public void onResponse(String url, String json) {
                                dismissLoading();
                                Log.e("loge", "加入购物车" + json);
                                //{"msg":"success"}
                                try {
                                    JSONObject object = new JSONObject(json);
                                    if (object.getString("msg").equals("success")) {
                                        booCheckShop = false;
                                        iv_gwc.setImageResource(R.drawable.icon_gwc_red);
                                        ToastUtil.initToast(ProjectDetailActivity.this, "加入购物车成功");
                                        cartListener.addCartUp();

                                    } else {
                                        booCheckShop = true;
                                        iv_gwc.setImageResource(R.drawable.icon_gwc_gray);
                                        ToastUtil.initToast(ProjectDetailActivity.this, "加入购物车失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String url, String error) {

                            }
                        });
                    } else {
                        //已加入购物车
                        ToastUtil.initToast(ProjectDetailActivity.this, "已存在购物车");
                        dismissLoading();
                    }
                    break;
                case R.id.iv_sc:
                    if (booCollection) {
                        //收藏
                        OkHttpUtil.postJson(Constant.URL.collectionAdd, builder, new OkHttpUtil.OnDataListener() {
                            @Override
                            public void onResponse(String url, String json) {
                                dismissLoading();
                                Log.e("loge", "加入收藏" + json);
                                try {
                                    JSONObject object = new JSONObject(json);
                                    if (object.getString("msg").equals("success")) {
                                        booCollection = false;
                                        iv_sc.setImageResource(R.drawable.icon_sc_red);
                                        ToastUtil.initToast(ProjectDetailActivity.this, "加入收藏成功");
                                    } else {
                                        booCollection = true;
                                        iv_sc.setImageResource(R.drawable.icon_sc_gray);
                                        ToastUtil.initToast(ProjectDetailActivity.this, "加入收藏失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String url, String error) {

                            }
                        });
                    } else {
                        //取消收藏
                        FormBody.Builder builder2 = new FormBody.Builder();
                        builder2.add("commodityId", projectID);
                        builder2.add("phone", userId);
                        OkHttpUtil.postJson(Constant.URL.deleteCollection, builder2, new OkHttpUtil.OnDataListener() {
                            @Override
                            public void onResponse(String url, String json) {
                                dismissLoading();
                                Log.e("loge", "删除收藏" + json);
                                try {
                                    JSONObject object = new JSONObject(json);
                                    if (object.getString("msg").equals("success")) {
                                        booCollection = true;
                                        ToastUtil.initToast(ProjectDetailActivity.this, "删除收藏成功");
                                        iv_sc.setImageResource(R.drawable.icon_sc_gray);
                                    } else {
                                        booCollection = false;
                                        iv_sc.setImageResource(R.drawable.icon_sc_red);
                                        ToastUtil.initToast(ProjectDetailActivity.this, "删除收藏失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String url, String error) {

                            }
                        });
                    }
                    break;
                case R.id.tv_addpay:
                    dismissLoading();
                    //立即购买
                    Intent intentPayment = new Intent(ProjectDetailActivity.this, PaymentActivity.class);
                    intentPayment.putExtra("type", 0);
                    intentPayment.putExtra("value", projectID + "");//商品id
                    intentPayment.putExtra("money", commodity_price + "");
                    intentPayment.putExtra("name", commodity_name + "");
                    intentPayment.putExtra("count", 1 + "");
                    intentPayment.putExtra("sort_name", commodity_type + "");
                    intentPayment.putExtra("commodity_url", commodity_url);
                    startActivityForResult(intentPayment, 0);
                    break;
            }
        }
    }


    private void setProjectView(JSONObject object) throws JSONException {
        commodity_name = object.getString("commodity_name");
        commodity_price = object.getString("commodity_price");
        sort_name = object.getString("sort_name");
        commodity_type = object.getString("sort_name");
        commodity_url = object.getString("picture_address");

        tv_nameProject.setText(commodity_name);
        tv_textProject.setText(object.getString("cdy_description"));
        tv_moneyProject.setText("¥" + commodity_price);

        tv_numProject.setText("剩余:" + object.getString("commodity_price") + "件");
        tv_numProject.setVisibility(View.GONE);

        tv_oneRebate.setText("一级返利: " + object.getString("rebate") + "克拉");
        tv_twoRebate.setText("二级返利: " + object.getString("twoRebate") + "克拉");


//        可用现金分抵现：××× 现金分
        payIntegral = u.getCurrency();
        if (payIntegral.equals("0")) {
//            tv_xjf.setText("您暂无现金分可抵扣");
            tv_xjf.setText("可用现金分抵现：0现金分");
            dMoney = 0;
        } else {
            dMoney = Double.parseDouble(object.getString("commodity_price")) / 2;
            double dpayIntegral = Double.parseDouble(payIntegral);
            Log.i("aaa", dpayIntegral + "   " + dMoney);
            if (dpayIntegral >= dMoney) {
//                tv_xjf.setText("您有" + dpayIntegral + "分，可抵¥" + dMoney);
                tv_xjf.setText("可用现金分抵现：" + dMoney + "现金分");
            } else {
                dMoney = dpayIntegral;
//                tv_xjf.setText("您有" + dpayIntegral + "分，可抵¥" + dMoney);
                tv_xjf.setText("可用现金分抵现：" + dpayIntegral + "现金分");
            }
        }
    }

    private void setTopViewPage(String json) {
        if (json.contains(";")) {
            strView = json.split(";");
        } else {
            strView = new String[]{json};
        }
//        Log.e("loge",strView.length+"  "+json);
        projectDetailAdAdapter.setData(strView);
        if (strView.length > 0) {
            lbtViewPager.setCurrentItem(1);
            lbtViewPager.startAutoScroll();
        }
    }

    //设置头部轮播图
    private void initViewPager(LinearLayout llHead, LinearLayout tabHead) {
        lbtViewPager = (AutoScrollViewPager) llHead.findViewById(R.id.lbt_ViewPager);
        navView = (NavView) llHead.findViewById(R.id.nv_NavView);
        tv_nameProject = (TextView) llHead.findViewById(R.id.tv_nameProject);
        tv_textProject = (TextView) llHead.findViewById(R.id.tv_textProject);
        tv_moneyProject = (TextView) llHead.findViewById(R.id.tv_moneyProject);
        tv_numProject = (TextView) llHead.findViewById(R.id.tv_numProject);
        tv_oneRebate = (TextView) llHead.findViewById(R.id.tv_oneRebate);
        tv_twoRebate = (TextView) llHead.findViewById(R.id.tv_twoRebate);
        tabLayoutHead = (TabLayout) tabHead.findViewById(R.id.tL_TabLayout);
        tv_xjf = (TextView) llHead.findViewById(R.id.tv_xjf);
        projectDetailAdAdapter = new ProjectDetailAdAdapter();
        lbtViewPager.setLayoutParams(new RelativeLayout.LayoutParams(PhoneUtil.getPhoneWidth(this),
                PhoneUtil.getPhoneWidth(this) / 2));
        lbtViewPager.setAdapter(projectDetailAdAdapter);
        lbtViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        lbtViewPager.setInterval(2000);
        lbtViewPager.setAutoScrollDurationFactor(4);
        lbtViewPager.addOnPageChangeListener(this);
        lbtViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

    private void initTabLayout() {
        String[] TabNames = {"图文详情", "商品参数", "评价"};
        for (int i = 0; i < TabNames.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(TabNames[i]));
            tabLayoutHead.addTab(tabLayoutHead.newTab().setText(TabNames[i]));
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                page = 1;
//                nextPage = 1;
                loadData();
                tabLayoutHead.getTabAt(tab.getPosition()).select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                recyclerView.smoothScrollToPosition(adapter.getHeaderCount() - 1);
            }
        });
        tabLayoutHead.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (floatingHead.getVisibility() != View.VISIBLE) {
                    tabLayout.getTabAt(tab.getPosition()).select();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
    }


    private void initRecyclerView(LinearLayout llHead, LinearLayout tabHead) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjectDetailAdapter(this, new recyclerViewOnClick(), this);
        adapter.addHeaderView(llHead);
        adapter.addHeaderView(tabHead);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, adapter.getHeaderCount()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();
                if (firstVisibleItem >= 1 && floatingHead.getVisibility() != View.VISIBLE) {
                    floatingHead.setVisibility(View.VISIBLE);
                } else if (firstVisibleItem == 0 && floatingHead.getVisibility() != View.GONE) {
                    floatingHead.setVisibility(View.GONE);
                }
            }
        });
    }

    public boolean isLogin() {
        userId = PreferencesUtil.getUserId(this);
        if ("default".equals(userId)) {
            return false;
        } else {
            return true;
        }
    }


    //----头部ViewPage start
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (navView == null) {
            return;
        }
        if (position == 0) {
            navView.setCurrentItem(strView.length - 1);
        } else if (position == strView.length + 1) {
            navView.setCurrentItem(0);
        } else {
            navView.setCurrentItem(position - 1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (lbtViewPager == null) {
            return;
        }
        if (state == 0) {
            if (lbtViewPager.getCurrentItem() == strView.length + 1) {
                lbtViewPager.setCurrentItem(1, false);
            } else if (lbtViewPager.getCurrentItem() == 0) {
                lbtViewPager.setCurrentItem(strView.length, false);
            }
        }
    }//----头部ViewPage end

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_homeAd://首页轮播图
                int posCarousel = (int) v.getTag();
                ToastUtil.initToast(this, "点击了" + posCarousel);
//                String links = carouselList.get(posCarousel).getPush_url();
//                if (!TextUtils.isEmpty(links)) {
//                    Intent intentDetail = new Intent(ProjectDetailActivity.this, H5Activity.class);
//                    intentDetail.putExtra("url", links);
//                    intentDetail.putExtra("title", carouselList.get(posCarousel).getPush_title());
//                    startActivity(intentDetail);
//                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 上拉加载
     */
    @Override
    public void onCallback() {
//        if (nextPage == page + 1) {
//            page++;
//            getData();
//        }
    }

    /**
     * recyclerView内空间点击
     */
    class recyclerViewOnClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    /**
     * recyclerViewitem点击事件
     *
     * @param v
     * @param position
     */
    @Override
    public void onItemClick(View v, int position) {

    }

    /**
     * 导航页viewPager适配器
     */
    class ProjectDetailAdAdapter extends PagerAdapter {

        private List<SimpleDraweeView> imgData;

        public void setData(String[] data) {
            if (imgData == null) {
                imgData = new ArrayList<>();
            } else {
                imgData.clear();
            }
            if (navView == null) {
                return;
            }
            navView.setCount(data.length);
            if (data.length > 1) {
                imgData.add(initSimpleDraweeView(data.length - 1, data[(data.length - 1)]));
            }
            for (int i = 0; i < data.length; i++) {
                imgData.add(initSimpleDraweeView(i, data[i]));
            }
            if (data.length > 1) {
                imgData.add(initSimpleDraweeView(0, data[0]));
            }
            this.notifyDataSetChanged();
        }

        public SimpleDraweeView initSimpleDraweeView(int index, String url) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) LayoutInflater.from(ProjectDetailActivity.this)
                    .inflate(R.layout.sdv_home, null, false);
            simpleDraweeView.setImageURI(Uri.parse(Constant.URL.BaseImg + url));
            simpleDraweeView.setTag(index);
            simpleDraweeView.setOnClickListener(ProjectDetailActivity.this);
            return simpleDraweeView;
        }

        @Override
        public int getCount() {
            if (imgData == null) {
                return 0;
            }
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
