package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.MyCollectionAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.CollectionList;
import cn.com.zlct.diamondgo.model.GetUserBankCard;
import cn.com.zlct.diamondgo.model.MsgEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import okhttp3.FormBody;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.toolbar_icon)
    public Toolbar toolbar;
    @BindView(R.id.bank_SwipeRefreshLayout)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.bank_RecyclerView)
    public RecyclerView recyclerView;

    UserInfoEntity userInfoEntity;
    private LoadingDialog loadingDialog;
    private Gson gson = new GsonBuilder().create();

    MyCollectionAdapter myCollectionAdapter;
    List<CollectionList.DataEntity> list;

    @Override
    protected int getViewResId() {
        return R.layout.activity_recycler;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        toolbar.setVisibility(View.GONE);
        ActionBarUtil.initActionBar(getSupportActionBar(), "我的收藏", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadingDialog = LoadingDialog.newInstance("加载中...");
        loadingDialog.show(getFragmentManager());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myCollectionAdapter = new MyCollectionAdapter(this);
        recyclerView.setAdapter(myCollectionAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, myCollectionAdapter.getHeaderCount()));

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        myCollectionAdapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(MyCollectionActivity.this, ProjectDetailActivity.class);
                intent.putExtra("projectId", list.get(position).getCommodityId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {
        OkHttpUtil.postJson(Constant.URL.getCollectionList, "phone",userInfoEntity.getPhone(), new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                CollectionList collectionList=gson.fromJson(json, CollectionList.class);
                dismissLoading();
                list = new ArrayList<>();
                list.addAll(collectionList.getList());
                myCollectionAdapter.setData(list);

//                if(list.size()>0){
//                }
            }

            @Override
            public void onFailure(String url, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(MyCollectionActivity.this,"获取失败，刷新重试");
                        dismissLoading();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }



    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
