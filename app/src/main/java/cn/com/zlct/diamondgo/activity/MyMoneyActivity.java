package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.MyMoneyAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.GetWithdrawalList;
import cn.com.zlct.diamondgo.model.SellingCommodityEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * 我的余额
 * Created by Administrator on 2017/7/3 0003.
 */
public class MyMoneyActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener ,OnAdapterCallbackListener {

    @BindView(R.id.rebate_SwipeRefresh)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rebate_RecyclerView)
    public RecyclerView recyclerView;

    SimpleDraweeView sdv_img;
    UserInfoEntity userInfoEntity;
    private LoadingDialog loadingDialog;
    private Gson gson = new GsonBuilder().create();
    TextView tv_money, tv_btn, tv_moneytv, tv_ll;
    MyMoneyAdapter myMoneyAdapter;
    List<GetWithdrawalList.DataEntity> list;
    double money = 0.0;
    int page = 1;
    int nextPage = 1;//下一页
    int pageSize = 30;

    @Override
    protected int getViewResId() {
        return R.layout.activity_rebate;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        ActionBarUtil.initActionBar(getSupportActionBar(), "我的余额", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadingDialog = LoadingDialog.newInstance("加载中...");
        loadingDialog.show(getFragmentManager());

        LinearLayout llHead = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.rebate_head, null, false);
        sdv_img = (SimpleDraweeView) llHead.findViewById(R.id.sdv_img);
        tv_money = (TextView) llHead.findViewById(R.id.tv_money);
        tv_btn = (TextView) llHead.findViewById(R.id.tv_btn);
        tv_moneytv = (TextView) llHead.findViewById(R.id.tv_moneytv);
        tv_ll = (TextView) llHead.findViewById(R.id.tv_ll);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myMoneyAdapter = new MyMoneyAdapter(this,this);
        myMoneyAdapter.addHeaderView(llHead);
        recyclerView.setAdapter(myMoneyAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, myMoneyAdapter.getHeaderCount()));
        sdv_img.setImageURI(Constant.URL.BaseImg + userInfoEntity.getHeadURL());
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        money = Double.parseDouble(getIntent().getStringExtra("money"));
        tv_money.setText("¥" + money);
        tv_ll.setText("提现明细");
        tv_moneytv.setText("(可提现余额)");
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提现
                if (money <= 0) {
                    ToastUtil.initToast(MyMoneyActivity.this, "可提现余额为" + money);
                } else if (money < 100) {
                    ToastUtil.initToast(MyMoneyActivity.this, "可提现余额为100的倍数");
                } else {
                    startActivityForResult(new Intent(MyMoneyActivity.this, MoneyWithdrawalsActivity.class).putExtra("money", money), 0);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        nextPage = 1;
        getData();
    }

    private void getData() {
        //获取提现明细
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", userInfoEntity.getPhone());
        builder.add("page", page + "");
        builder.add("pageSize", pageSize + "");

        OkHttpUtil.postJson(Constant.URL.appGetWithdrawalList, builder, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                GetWithdrawalList rebateList = gson.fromJson(json, GetWithdrawalList.class);
                dismissLoading();
                if (page == 1) {
                    if (list == null) {
                        list = new ArrayList<>();
                    } else {
                        list.clear();
                    }
                }
                if (rebateList.getList().size() > 0) {
                    list.addAll(rebateList.getList());
                    if (list.size() % pageSize == 0) {//可能还有下一页
                        if (list.size() >=pageSize){
                            list.add(new GetWithdrawalList.DataEntity(1));
                            nextPage = page + 1;
                        }
                    }
                } else {
                    if (page != 1) {
                        Toast.makeText(MyMoneyActivity.this, "已经到底了", Toast.LENGTH_SHORT).show();
                    }
                }
                myMoneyAdapter.setData(list);
//                if(list.size()>0){
//                }
            }

            @Override
            public void onFailure(String url, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(MyMoneyActivity.this, "获取失败，刷新重试");
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
            if (requestCode == 0) {
                money = money - Integer.parseInt(data.getStringExtra("money"));
                tv_money.setText("¥" + (money));
            }
        }
    }

    @Override
    public void onRefresh() {
        loadData();
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
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
