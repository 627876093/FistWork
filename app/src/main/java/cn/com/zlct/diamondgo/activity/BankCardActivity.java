package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.UserBankCardAdapter;
import cn.com.zlct.diamondgo.adapter.ZZJFAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.GetUserBankCard;
import cn.com.zlct.diamondgo.model.MsgEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import okhttp3.FormBody;

/**
 * 我的银行卡
 */
public class BankCardActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.toolbar_icon)
    public Toolbar toolbar;
    @BindView(R.id.bank_SwipeRefreshLayout)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.bank_RecyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.ll_empty)
    public LinearLayout llEmpty;
    @BindView(R.id.tv_emptyTips)
    public TextView tvEmpty;

    UserInfoEntity userInfoEntity;
    private LoadingDialog loadingDialog;
    private Gson gson = new GsonBuilder().create();

    UserBankCardAdapter userBankCardAdapter;
    List<GetUserBankCard.DataEntity> list;

    @Override
    protected int getViewResId() {
        return R.layout.activity_recycler;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        ToolBarUtil.initToolBar(toolbar, "我的银行卡", R.drawable.add_white, this);
        loadingDialog = LoadingDialog.newInstance("加载中...");
        loadingDialog.show(getFragmentManager());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userBankCardAdapter = new UserBankCardAdapter(this,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("phone", userInfoEntity.getPhone());
                builder.add("id", list.get((Integer) v.getTag()).getId());

                OkHttpUtil.postJson(Constant.URL.appUserBankBindingDelete, builder, new OkHttpUtil.OnDataListener() {
                    @Override
                    public void onResponse(String url, String json) {
                        Log.e("loge", json);
                        MsgEntity msgEntity=gson.fromJson(json, MsgEntity.class);
                        ToastUtil.initToast(BankCardActivity.this, msgEntity.getMsg()+"");
                        loadData();
                    }
                    @Override
                    public void onFailure(String url, String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.initToast(BankCardActivity.this,"删除失败，请重试");
                            }
                        });
                    }
                });
            }
        });
        recyclerView.setAdapter(userBankCardAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, userBankCardAdapter.getHeaderCount()));

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        tvEmpty.setText("暂无银行卡");


    }

    @Override
    protected void loadData() {
        OkHttpUtil.postJson(Constant.URL.appUserBankBindingQuery, "phone",userInfoEntity.getPhone(), new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                GetUserBankCard getUserBankCard=gson.fromJson(json, GetUserBankCard.class);
                dismissLoading();
                list = new ArrayList<>();
                list.addAll(getUserBankCard.getList());
                userBankCardAdapter.setData(list);
                if (list.size() > 0) {
                    if (llEmpty.getVisibility() != View.GONE) {
                        llEmpty.setVisibility(View.GONE);
                    }
                } else {
                    if (llEmpty.getVisibility() != View.VISIBLE) {
                        llEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(String url, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(BankCardActivity.this,"获取失败，刷新重试");
                        dismissLoading();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_iconBack:
                onBackPressed();
                break;
            case R.id.toolbar_iconNext://新增
                startActivityForResult(new Intent(this, BankCardEditActivity.class), Constant.Code.AddCode);
                break;
        }
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
