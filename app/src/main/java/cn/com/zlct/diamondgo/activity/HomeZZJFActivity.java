package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.AllGoodsRVAdapter;
import cn.com.zlct.diamondgo.adapter.ZZJFAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.model.ZZJFEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class HomeZZJFActivity extends BaseActivity {

    @BindView(R.id.swipeRefreshs)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerViews)
    RecyclerView recyclerView;

    ZZJFAdapter zzjfAdapter;
    private List<ZZJFEntity> list;

    @Override
    protected int getViewResId() {
        return R.layout.activity;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String title = "title";
        if (intent != null) {
            title = intent.getStringExtra("title");
        }

        ActionBarUtil.initActionBar(getSupportActionBar(), title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(R.drawable.zzjf_bg);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        zzjfAdapter = new ZZJFAdapter(this);
        zzjfAdapter.addHeaderView(imageView);
        zzjfAdapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                
            }
        });
        recyclerView.setAdapter(zzjfAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, zzjfAdapter.getHeaderCount()));

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void loadData() {
        String url = String.format(Constant.URL.getVipInfo);
        OkHttpUtil.getJSON(url, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge",json);
                ZZJFEntity imageCarousel = new Gson().fromJson(json, ZZJFEntity.class);
                list = new ArrayList<>();
                list.add(imageCarousel);
                if(list.size()>0){
                    zzjfAdapter.setData(list);
                }
            }
            @Override
            public void onFailure(String url, String error) {

            }
        });
    }
}
