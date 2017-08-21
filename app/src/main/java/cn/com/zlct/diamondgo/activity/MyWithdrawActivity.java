package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.MyCircleAdapter;
import cn.com.zlct.diamondgo.adapter.MyWithdrawAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.GetWithdrawList;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;

/**
 * 提现列表
 * Created by Administrator on 2017/5/23 0023.
 */
public class MyWithdrawActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar_icon)
    public Toolbar toolbar;
    @BindView(R.id.bank_SwipeRefreshLayout)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.bank_RecyclerView)
    public RecyclerView recyclerView;

    UserInfoEntity userInfoEntity;
    private LoadingDialog loadingDialog;
    private Gson gson = new GsonBuilder().create();

    MyWithdrawAdapter myWithdrawAdapter;
    List<GetWithdrawList.DataEntity> list;


    @Override
    protected int getViewResId() {
        return R.layout.activity_recycler;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        toolbar.setVisibility(View.GONE);
        ActionBarUtil.initActionBar(getSupportActionBar(), "提现列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadingDialog = LoadingDialog.newInstance("加载中...");
        loadingDialog.show(getFragmentManager());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myWithdrawAdapter = new MyWithdrawAdapter(this);
        recyclerView.setAdapter(myWithdrawAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, myWithdrawAdapter.getHeaderCount()));

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

    }

    @Override
    protected void loadData() {
        OkHttpUtil.postJson(Constant.URL.appgetWithdraw, "phone", userInfoEntity.getPhone(), new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);

                GetWithdrawList g = gson.fromJson(json, GetWithdrawList.class);
                dismissLoading();
                list = new ArrayList<>();
                list.addAll(g.getList());
                myWithdrawAdapter.setData(list);
            }

            @Override
            public void onFailure(String url, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(MyWithdrawActivity.this, "获取失败，刷新重试");
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
