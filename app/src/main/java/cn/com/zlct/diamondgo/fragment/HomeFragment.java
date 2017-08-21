package cn.com.zlct.diamondgo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.H5Activity;
import cn.com.zlct.diamondgo.activity.MyRecommendActivity;
import cn.com.zlct.diamondgo.activity.ProjectDetailActivity;
import cn.com.zlct.diamondgo.activity.ProjectListActivity;
import cn.com.zlct.diamondgo.activity.TongImgActivity;
import cn.com.zlct.diamondgo.adapter.AllGoodsRVAdapter;
import cn.com.zlct.diamondgo.adapter.NewRVAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.custom.AutoScrollViewPager;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.custom.NavView;
import cn.com.zlct.diamondgo.model.ImageCarouselEntity;
import cn.com.zlct.diamondgo.model.SellingCommodityEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * 首页 Fragment
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, OkHttpUtil.OnDataListener,
    OnAdapterCallbackListener, AbsRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.et_home_search)
    EditText etSearch;
    @BindView(R.id.img_home_searchByVoice)
    ImageView imgSearch;
    @BindView(R.id.srl_mainHome)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_home)
    RecyclerView recyclerViewGoods;
    @BindView(R.id.iv_homefl)
    ImageView iv_homefl;


    RecyclerView rv_homeHead;//新品专区

    private AllGoodsRVAdapter goodsAdapter;
    private AutoScrollViewPager viewPager;
    private NavView navView;

    private HomeAdAdapter homeAdAdapter;//轮播图Adapter
    private NewRVAdapter newRVAdapter;
    private List<ImageCarouselEntity.DataEntity> carouselList;//轮播图数据
    private List<SellingCommodityEntity.DataEntity> newList;//新品专区
    private List<SellingCommodityEntity.DataEntity> goodsList;//热销产品


    private String phone;
    private Gson gson = new GsonBuilder().create();
    private LoadingDialog loadingDialog;

    private OnItemClickListener clickListener;

    @Override
    protected int getViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(final View view) {
        phone = SharedPreferencesUtil.getUserId(getActivity());
        iv_homefl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v);
            }
        });

        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getActivity().getFragmentManager(), "loading");
        LinearLayout llHead = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.item_home_head, null, false);
        findWidget(llHead);
        initViewPager();
        initRecyclerView(llHead);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectListActivity.class);
                intent.putExtra("id", "");
                intent.putExtra("commodity_name", etSearch.getText().toString().trim() + "");
                startActivity(intent);
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setHintEt(etSearch, hasFocus);
            }
        });

    }

    private void setHintEt(EditText et, boolean hasFocus) {
        String hint;
        if (hasFocus) {
            hint = et.getHint().toString();
            et.setTag(hint);
            et.setHint("");
        } else {
            hint = et.getTag().toString();
            et.setHint(hint);
        }
    }

    @Override
    protected void loadData() {
        //获取轮播图片
        String url = String.format(Constant.URL.GetRotateImageList);
        OkHttpUtil.getJSON(url, this);
        //获取用户私信是否未读
//        if (isLogin()) {
//            String jsonLogs = gson.toJson(new GetUserBasicInfo(userId));
//            OkHttpUtil.postJson(Constant.URL.DetectUserLogs, DesUtil.encrypt(jsonLogs), this);//获取用户私信是否未读
//        }

        //通宝理财
//        OkHttpUtil.getJSON(Constant.URL.getAppQueryfin, this);
        //活动专区
//        OkHttpUtil.getJSON(Constant.URL.appgetCommodityClassList, this);


        //查询个人信息
        String phone = SharedPreferencesUtil.getPhone(getActivity());
        OkHttpUtil.postJson(Constant.URL.getUserInfo, "phone", phone, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                UserInfoEntity userInfoEntity = new Gson().fromJson(json, UserInfoEntity.class);
                dismissLoading();
                SharedPreferencesUtil.saveUserInfo(getActivity(), DesUtil.encrypt(json, DesUtil.LOCAL_KEY));
            }

            @Override
            public void onFailure(String url, String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(getContext(), "刷新失败，请重试");
                        dismissLoading();
                    }
                });
            }
        });

        getNewData();
        getData();
    }

    /**
     * 新品专区
     */
    private void getNewData() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("page", "1");
        builder.add("pageSize", "10");
        builder.add("commodity_name", "");
        builder.add("sort_id", "");
        builder.add("brand_id", "");
        builder.add("xlkind", "5");
        OkHttpUtil.postJson(Constant.URL.appgetCommodity, builder, this);
    }

    /**
     * 热销产品
     */
    private void getData() {
        OkHttpUtil.postJson(Constant.URL.getSellingCommodity, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                SellingCommodityEntity projectListEntity = new Gson().fromJson(json, SellingCommodityEntity.class);

                goodsList = new ArrayList<>();
                goodsList.addAll(projectListEntity.getList());
                //热销产品
                goodsAdapter.setData(goodsList);
            }

            @Override
            public void onFailure(String url, String error) {

            }
        });
    }

    /**
     * 找回头部控件
     */
    private void findWidget(LinearLayout llHead) {
        viewPager = (AutoScrollViewPager) llHead.findViewById(R.id.vp_home_Auto);
        navView = (NavView) llHead.findViewById(R.id.nv_home_Auto);


        rv_homeHead = (RecyclerView) llHead.findViewById(R.id.rv_homeHead);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        rv_homeHead.setLayoutManager(manager);
        newRVAdapter = new NewRVAdapter(getContext());
        rv_homeHead.setAdapter(newRVAdapter);


        llHead.findViewById(R.id.ll_hdzq).setOnClickListener(this);
        llHead.findViewById(R.id.ll_cnxh).setOnClickListener(this);

        llHead.findViewById(R.id.tv_homeShortcut1).setOnClickListener(this);
        llHead.findViewById(R.id.tv_homeShortcut2).setOnClickListener(this);
        TextView ss= (TextView) llHead.findViewById(R.id.tv_homeShortcut2);
        ss.setText("客服&售后");
        llHead.findViewById(R.id.tv_homeShortcut3).setOnClickListener(this);
        llHead.findViewById(R.id.tv_homeShortcut4).setOnClickListener(this);

        newRVAdapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (goodsList != null && goodsList.size() > 0) {
                    //进入商品详情页
                    Intent intent = new Intent(getContext(), ProjectDetailActivity.class);
                    intent.putExtra("projectId", goodsList.get(position).getId() + "");
                    startActivity(intent);
                }
            }
        });
    }

    //设置头部轮播图
    private void initViewPager() {
        homeAdAdapter = new HomeAdAdapter();
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(PhoneUtil.getPhoneWidth(getActivity()),
                PhoneUtil.getPhoneWidth(getActivity()) / 2));
        viewPager.setAdapter(homeAdAdapter);
        viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        viewPager.setInterval(2000);
        viewPager.setAutoScrollDurationFactor(2);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
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

    private void initRecyclerView(LinearLayout llHead) {
        recyclerViewGoods.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        goodsAdapter = new AllGoodsRVAdapter(getActivity(), this);
        goodsAdapter.addHeaderView(llHead);
        goodsAdapter.setOnItemClickListener(this);
        recyclerViewGoods.setAdapter(goodsAdapter);
//        recyclerViewGoods.addItemDecoration(new DividerGridItemDecoration(getActivity(), goodsAdapter.getHeaderCount()));

//        recyclerViewGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
//                int firstVisibleItem = manager.findFirstVisibleItemPosition();
//                if (firstVisibleItem >= 1 && floatingHead.getVisibility() != View.VISIBLE) {
//                    floatingHead.setVisibility(View.VISIBLE);
//                } else if (firstVisibleItem == 0 && floatingHead.getVisibility() != View.GONE) {
//                    floatingHead.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (navView == null) {
            return;
        }
        if (position == 0) {
            navView.setCurrentItem(carouselList.size() - 1);
        } else if (position == carouselList.size() + 1) {
            navView.setCurrentItem(0);
        } else {
            navView.setCurrentItem(position - 1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (viewPager == null) {
            return;
        }
        if (state == 0) {
            if (viewPager.getCurrentItem() == carouselList.size() + 1) {
                viewPager.setCurrentItem(1, false);
            } else if (viewPager.getCurrentItem() == 0) {
                viewPager.setCurrentItem(carouselList.size(), false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_homeAd://首页轮播图
                int posCarousel = (int) v.getTag();
                String links = carouselList.get(posCarousel).getPush_url();
                if (!TextUtils.isEmpty(links)) {
                    Intent intentDetail = new Intent(getActivity(), ProjectDetailActivity.class);
//                    intentDetail.putExtra("url", links);
//                    intentDetail.putExtra("title", carouselList.get(posCarousel).getPush_title());
//                    startActivity(intentDetail);
                    intentDetail.putExtra("projectId", links);
                    startActivity(intentDetail);
                }
                break;
            case R.id.tv_homeShortcut1://我的二维码
                startActivity(new Intent(getActivity(), MyRecommendActivity.class));
//                Intent intentSun0 = new Intent(getActivity(), HomeZZJFActivity.class);
//                intentSun0.putExtra("title", "增值积分");
//                startActivity(intentSun0);
                break;
            case R.id.tv_homeShortcut2://售后服务
//                clickListener.onItemClick(v);

                Intent intentUser = new Intent(getContext(), H5Activity.class);
                intentUser.putExtra("url", Constant.URL.UserVip);
                intentUser.putExtra("title", "客服&售后");
                startActivity(intentUser);

//                Intent intentSun1 = new Intent(getActivity(), AreaActivity.class);
//                intentSun1.putExtra("title", "一元专区");
//                intentSun1.putExtra("userId", "default");
//                intentSun1.putExtra("basicInformationId", "default");
//                intentSun1.putExtra("pageSize", "default");
//                intentSun1.putExtra("tag",1);
//                startActivity(intentSun1);
                break;
            case R.id.tv_homeShortcut3://通宝专区
                    Intent intentSun3=new Intent(getContext(),TongImgActivity.class);
                startActivity(intentSun3);
//                Intent intentSun3 = new Intent(getActivity(), AreaActivity.class);
//                intentSun3.putExtra("title", "五元专区");
//                intentSun3.putExtra("userId", "default");
//                intentSun3.putExtra("basicInformationId", "default");
//                intentSun3.putExtra("pageSize", "default");
//                intentSun3.putExtra("tag",2);
//                startActivity(intentSun3);
                break;
            case R.id.tv_homeShortcut4://常见问答
                Intent intent = new Intent(getContext(), H5Activity.class);
                intent.putExtra("url", Constant.URL.Replicate);
                intent.putExtra("title", "常见问题");
                startActivity(intent);
                break;
            case R.id.ll_hdzq://新品专区》全部
            case R.id.ll_cnxh:
                //全部商品
                Intent all = new Intent(getActivity(), ProjectListActivity.class);
                all.putExtra("id", "");
                all.putExtra("commodity_name", "");
                startActivity(all);
                break;
//            case R.id.rl_homesimple0://天天特价
//                Intent intentSimp0 = new Intent(getActivity(), ProjectListActivity.class);
//                intentSimp0.putExtra("id", simple0Id);
//                intentSimp0.putExtra("commodity_name", "");
//                startActivity(intentSimp0);
//                break;
//            case R.id.rl_homesimple1://奢品汇
//                Intent intentSimp1 = new Intent(getActivity(), ProjectListActivity.class);
//                intentSimp1.putExtra("id", simple1Id);
//                intentSimp1.putExtra("commodity_name", "");
//                startActivity(intentSimp1);
//                break;
//            case R.id.rl_homesimple2://潮店街
//                Intent intentSimp2 = new Intent(getActivity(), ProjectListActivity.class);
//                intentSimp2.putExtra("id", simple2Id);
//                intentSimp2.putExtra("commodity_name", "");
//                startActivity(intentSimp2);
//                break;
//            case R.id.rl_homesimple3://好生鲜
//                Intent intentSimp3 = new Intent(getActivity(), ProjectListActivity.class);
//                intentSimp3.putExtra("id", simple3Id);
//                intentSimp3.putExtra("commodity_name", "");
//                startActivity(intentSimp3);
//                break;
//            case R.id.rl_homesimple4://游戏动漫
//                Intent intentSimp4 = new Intent(getActivity(), ProjectListActivity.class);
//                intentSimp4.putExtra("id", simple4Id);
//                intentSimp4.putExtra("commodity_name", "");
//                startActivity(intentSimp4);
//                break;
        }
    }

    public boolean isLogin() {
        phone = SharedPreferencesUtil.getUserId(getActivity());
        if ("default".equals(phone)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onResponse(String url, String json) {
        dismissLoading();
        try {
            if (!TextUtils.isEmpty(json)) {
                String decrypt = json;
//                String decrypt = DesUtil.decrypt(json);
                if (Constant.URL.GetRotateImageList.equals(url)) {//获取轮播图片
                    Log.e("loge", "获取轮播图片:" + decrypt);
                    ImageCarouselEntity imageCarousel = new Gson().fromJson(decrypt, ImageCarouselEntity.class);
                    if (imageCarousel.getList().size() > 0) {
                        carouselList = imageCarousel.getList();
                        homeAdAdapter.setData(carouselList);
                        if (carouselList.size() > 1) {
                            viewPager.setCurrentItem(1);
                            viewPager.startAutoScroll();
                        }
                    }
//                } else if (Constant.URL.DetectUserLogs.equals(url)) {//检测私信
//                    Log.e("loge",  "检测私信:" + decrypt);
//                    SingleWordEntity userLogs = new Gson().fromJson(decrypt, SingleWordEntity.class);
//                    if ("0".equals(userLogs.getErrNum())) {
//                        if ("true".equals(userLogs.getData().get(0).getStatus())) {
//                            ibInbox.setSelected(true);
//                        } else {
//                            ibInbox.setSelected(false);
//                        }
//                    }
                } else if (Constant.URL.getSellingCommodity.equals(url)) {//热销专区
                    Log.e("loge", "热销专区:" + decrypt);
                    SellingCommodityEntity projectListEntity = new Gson().fromJson(decrypt, SellingCommodityEntity.class);
                    goodsList = new ArrayList<>();
                    goodsList.addAll(projectListEntity.getList());
                    goodsAdapter.setData(goodsList);
//                }else if (Constant.URL.getAppQueryfin.equals(url)){//通宝理财
//                    Log.e("loge", "通宝理财:" + decrypt);
////{"list":[
//// {"name":"服务积分","url":"https://www.sogou.com/tx?ie=utf-8&hdq=sogou-clse-f507783927f2ec27&query=baidu%2Ecom","id":1,"listCount":0,"pageCount":0,"page":0,"pageSize":0,"tup_url":"/shoppingManagementSystem/financialInger/IMG_0702.JPG"}
//// {"name":"中字一号","url":"https://www.sogou.com/tx?ie=utf-8&hdq=sogou-clse-f507783927f2ec27&query=baidu%2Ecom","id":2,"listCount":0,"pageCount":0,"page":0,"pageSize":0,"tup_url":"/shoppingManagementSystem/financialInger/IMG_0703.JPG"}
//// {"name":"待定项目","url":"https://www.sogou.com/tx?ie=utf-8&hdq=sogou-clse-f507783927f2ec27&query=baidu%2Ecom","id":3,"listCount":0,"pageCount":0,"page":0,"pageSize":0,"tup_url":"/shoppingManagementSystem/financialInger/IMG_0702.JPG"}
//
//                    JSONArray array=new JSONObject(decrypt).getJSONArray("list");
//                    if (array.length()>=3){
//                        Shortcut0URL=((JSONObject)array.get(0)).getString("url");
//                        Shortcut1URL=((JSONObject)array.get(1)).getString("url");
//                        Shortcut2URL=((JSONObject)array.get(2)).getString("url");
//                    }

//                } else if (Constant.URL.appgetCommodityClassList.equals(url)){//活动专区
//                    Log.e("loge", "活动专区:" + decrypt);
////{"list":[
//// {"id":11,"className":"食品部","listCount":0,"pageCount":0,"page":0,"pageSize":0,"classURL":"/shoppingManagementSystem/commodity/IMG_0703.JPG","classDesc":"零食"}
//// {"id":12,"className":"手机数码","listCount":0,"pageCount":0,"page":0,"pageSize":0,"classURL":"/shoppingManagementSystem/commodity/5924f8adN6a14df65.jpg","classDesc":"手机数码"}
//// {"id":13,"className":"书籍","listCount":0,"pageCount":0,"page":0,"pageSize":0,"classURL":"/shoppingManagementSystem/commodity/IMG_0702.JPG","classDesc":"书籍"}
//// {"id":14,"className":"文具","listCount":0,"pageCount":0,"page":0,"pageSize":0,"classURL":"/shoppingManagementSystem/commodity/IMG_0703.JPG","classDesc":"铅笔"}
//// {"id":15,"className":"动物","listCount":0,"pageCount":0,"page":0,"pageSize":0,"classURL":"/shoppingManagementSystem/commodity/IMG_0702.JPG","classDesc":"小狗"}]}
//                    JSONArray array=new JSONObject(decrypt).getJSONArray("list");
//                    if (array.length()>=5){
//                        simple0Id =((JSONObject)array.get(0)).getString("id");
//                        tv_homesimple0.setText(((JSONObject) array.get(0)).getString("className"));
//                        tv_simple0text.setText(((JSONObject) array.get(0)).getString("classDesc"));
//                        sdv_homesimple0.setImageURI(Constant.URL.BaseImg + ((JSONObject) array.get(0)).getString("classURL"));
//
//                        simple1Id =((JSONObject)array.get(1)).getString("id");
//                        tv_homesimple1.setText(((JSONObject)array.get(1)).getString("className"));
//                        tv_simple1text.setText(((JSONObject)array.get(1)).getString("classDesc"));
//                        sdv_homesimple1.setImageURI(Constant.URL.BaseImg + ((JSONObject) array.get(1)).getString("classURL"));
//
//                        simple2Id =((JSONObject)array.get(2)).getString("id");
//                        tv_homesimple2.setText(((JSONObject)array.get(2)).getString("className"));
//                        tv_simple2text.setText(((JSONObject)array.get(2)).getString("classDesc"));
//                        sdv_homesimple2.setImageURI(Constant.URL.BaseImg + ((JSONObject) array.get(2)).getString("classURL"));
//
//                        simple3Id =((JSONObject)array.get(3)).getString("id");
//                        tv_homesimple3.setText(((JSONObject)array.get(3)).getString("className"));
//                        tv_simple3text.setText(((JSONObject)array.get(3)).getString("classDesc"));
//                        sdv_homesimple3.setImageURI(Constant.URL.BaseImg + ((JSONObject) array.get(3)).getString("classURL"));
//
//                        simple4Id =((JSONObject)array.get(4)).getString("id");
//                        tv_homesimple4.setText(((JSONObject) array.get(4)).getString("className"));
//                        tv_simple4text.setText(((JSONObject)array.get(4)).getString("classDesc"));
//                        sdv_homesimple4.setImageURI(Constant.URL.BaseImg + ((JSONObject) array.get(4)).getString("classURL"));
//                    }
                } else if (Constant.URL.appgetCommodity.equals(url)) {//新品专区
                    Log.e("loge", "新品专区:" + json);
                    SellingCommodityEntity projectListEntity = new Gson().fromJson(json, SellingCommodityEntity.class);


                    newList = new ArrayList<>();
                    newList.addAll(projectListEntity.getList());
                    //新品专区
                    newRVAdapter.setData(newList);

                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFailure(String url, String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoading();
            }
        });
    }

    @Override
    public void onCallback() {
//        if (nextPage == page + 1) {
//            page++;
        getData();
//        }
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    //    private void removeLoadingItem() {
//        if (goodsList.size() > 0) {
//            if (goodsList.get(goodsList.size() - 1).getType() == 1) {
//                goodsList.remove(goodsList.size() - 1);
//            }
//        }
//    }
    @Override
    public void onItemClick(View v, int position) {
        String projectId = (String) v.getTag(R.id.tag_id);
        if (!TextUtils.isEmpty(projectId)) {
            //进入商品详情页
            Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
            intent.putExtra("projectId", projectId);
            startActivity(intent);
        }
    }

    /**
     * 导航页viewPager适配器
     */
    class HomeAdAdapter extends PagerAdapter {

        private List<SimpleDraweeView> imgData;

        public void setData(List<ImageCarouselEntity.DataEntity> data) {
            if (imgData == null) {
                imgData = new ArrayList<>();
            } else {
                imgData.clear();
            }
            if (navView == null) {
                return;
            }
            navView.setCount(data.size());
            if (data.size() > 1) {
                imgData.add(initSimpleDraweeView(data.size() - 1, data.get(data.size() - 1).getPicture_url()));
            }
            for (int i = 0; i < data.size(); i++) {
                imgData.add(initSimpleDraweeView(i, data.get(i).getPicture_url()));
            }
            if (data.size() > 1) {
                imgData.add(initSimpleDraweeView(0, data.get(0).getPicture_url()));
            }
            this.notifyDataSetChanged();
        }

        public SimpleDraweeView initSimpleDraweeView(int index, String url) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.sdv_home, null, false);
            simpleDraweeView.setImageURI(Uri.parse(Constant.URL.BaseImg + url));
            simpleDraweeView.setTag(index);
            simpleDraweeView.setOnClickListener(HomeFragment.this);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v);
    }

    @Override
    public void onDestroyView() {
        if (viewPager != null) {
            viewPager.stopAutoScroll();
        }
        super.onDestroyView();
    }
}
