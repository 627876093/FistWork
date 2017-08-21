package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.AllGoodsRVAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.ProjectListEntity;
import cn.com.zlct.diamondgo.model.SellingCommodityEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import okhttp3.FormBody;

/**
 * 商品列表
 * Created by Administrator on 2017/5/27 0027.
 */
public class ProjectListActivity extends BaseActivity implements OkHttpUtil.OnDataListener, OnAdapterCallbackListener
        , AbsRecyclerViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener ,RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.toolbar_Poject)
    Toolbar toolbar_Poject;
    @BindView(R.id.ib_home_inbox)
    ImageButton ib_home_inbox;
    @BindView(R.id.img_Poject_searchByVoice)
    ImageView img_search;
    @BindView(R.id.et_poject_search)
    EditText et_search;
    @BindView(R.id.srl_Poject)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_Poject)
    RecyclerView recyclerView;
    @BindView(R.id.rg_tab)
    public RadioGroup radioGroup;

    @BindView(R.id.ll_empty)
    public LinearLayout llEmpty;
    @BindView(R.id.tv_emptyTips)
    public TextView tvEmpty;

    private AllGoodsRVAdapter goodsAdapter;
    private List<SellingCommodityEntity.DataEntity> goodsList;
    private int page=1;//当前页
    private int nextPage=1;//下一页
    private final int pageSize = 20;//每页数量
    private String commodity_name; // 商品名称
    private String sort_id;// 分类ID
    private String brand_id;// 品牌ID
    private int xlkind;//排序情况： 1=销量小到大，2=销量大到小，3=价格小到大，4=价格大到小
    private String phone;
    private Gson gson = new GsonBuilder().create();
    private LoadingDialog loadingDialog;

    @Override
    protected int getViewResId() {
        return R.layout.activity_projectlist;
    }

    @Override
    protected void init() {
        llEmpty.setVisibility(View.GONE);
        phone = SharedPreferencesUtil.getUserId(this);
        Intent intent=getIntent();
        if (intent!=null){
            commodity_name=intent.getStringExtra("commodity_name")+"";
            sort_id=intent.getStringExtra("id");
            brand_id="";
            xlkind=2;
            if (commodity_name.length()>0){
                et_search.setText(commodity_name);
            }
        }else {
            commodity_name="";
            sort_id="";
            brand_id="";
            xlkind=2;
        }
        ib_home_inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=et_search.getText().toString().trim()+"";
                commodity_name=s.equals("")? "":s;
                page = 1;
                nextPage = 1;
                getData();
            }
        });

        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(0).performClick();
        tvEmpty.setText("暂无数据");
        //设置列表
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        goodsAdapter = new AllGoodsRVAdapter(this, this);
        goodsAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(goodsAdapter);
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, goodsAdapter.getHeaderCount()));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setHintEt(et_search,hasFocus);
            }
        });

    }
    private void setHintEt(EditText et,boolean hasFocus){
        String hint;
        if(hasFocus){
            hint = et.getHint().toString();
            et.setTag(hint);
            et.setHint("");
        }else{
            hint = et.getTag().toString();
            et.setHint(hint);
        }
    }

    @Override
    protected void loadData() {
//        page = 1;
//        nextPage = 1;

//        getData();
    }

    private void getData() {
//        private String page;// 当前页
//        private String pageSize;// 每页数据
//        private String commodity_name; // 商品名称
//        private String sort_id;// 分类ID
//        private String brand_id;// 品牌ID
//        private int xlkind;//排序情况： 1=销量小到大，2=销量大到小，3=价格小到大，4=价格大到小
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("page", page + "");
        builder.add("pageSize", pageSize+"");
        builder.add("commodity_name", commodity_name);
        builder.add("sort_id", sort_id);
        builder.add("brand_id", brand_id);
        builder.add("xlkind", xlkind + "");
        OkHttpUtil.postJson(Constant.URL.appgetCommodity, builder, this);
    }

    @Override
    public void onResponse(String url, String json) {
        dismissLoading();
        try {
            if (!TextUtils.isEmpty(json)) {
                if (Constant.URL.appgetCommodity.equals(url)) {//商品列表
                    Log.e("loge", "商品列表:" + json);
                    SellingCommodityEntity projectListEntity = new Gson().fromJson(json, SellingCommodityEntity.class);
                    if (page == 1) {
                        if (goodsList == null) {
                            goodsList = new ArrayList<>();
                        } else {
                            goodsList.clear();
                        }
                    }
                    removeLoadingItem();
                    if (projectListEntity.getList().size() > 0) {
                        goodsList.addAll(projectListEntity.getList());
                        if (goodsList.size() % pageSize == 0) {//可能还有下一页
                            if (goodsList.size() >=pageSize){
                                goodsList.add(new SellingCommodityEntity.DataEntity(1));
                                nextPage = page + 1;
                            }
                        }
                    } else {
                        if (page != 1) {
                            Toast.makeText(this, "已经到底了", Toast.LENGTH_SHORT).show();
                        }
                    }

                    goodsAdapter.setData(goodsList);

                    if (goodsList.size() > 0) {
                        if (llEmpty.getVisibility() != View.GONE) {
                            llEmpty.setVisibility(View.GONE);
                        }
                    }else {
                        if (llEmpty.getVisibility() != View.VISIBLE) {
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onFailure(String url, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoading();
            }
        });
    }

    @Override
    public void onCallback() {
        if (nextPage == page + 1) {
            page++;
            getData();
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

    private void removeLoadingItem() {
        if (goodsList.size() > 0) {
            if (goodsList.get(goodsList.size() - 1).getType() == 1) {
                goodsList.remove(goodsList.size() - 1);
            }
        }

    }

    @Override
    public void onItemClick(View v, int position) {
        String projectId = (String) v.getTag(R.id.tag_id);
        if (!TextUtils.isEmpty(projectId)) {
            //进入商品详情页
            Intent intent = new Intent(this, ProjectDetailActivity.class);
            intent.putExtra("projectId", projectId);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        nextPage = 1;
        getData();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getFragmentManager(), "loading");
        switch (checkedId) {
            case R.id.rb_zh://综合
                xlkind=2;
                getData();
                break;
            case R.id.rb_xl://销量
                if (xlkind==1){
                    xlkind=2;
                }else if (xlkind==2){
                    xlkind=1;
                }else {
                    xlkind=2;
                }
                getData();
                break;
            case R.id.rb_jg://价格
                if (xlkind==3){
                    xlkind=4;
                }else if (xlkind==4){
                    xlkind=3;
                }else {
                    xlkind=3;
                }
                getData();
                break;
        }
    }
}
