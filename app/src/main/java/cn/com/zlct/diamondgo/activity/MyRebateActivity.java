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

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.MyCollectionAdapter;
import cn.com.zlct.diamondgo.adapter.MyRebateAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.CollectionList;
import cn.com.zlct.diamondgo.model.RebateList;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;

/**
 * 我的克拉
 * Created by Administrator on 2017/5/23 0023.
 */
public class MyRebateActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rebate_SwipeRefresh)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rebate_RecyclerView)
    public RecyclerView recyclerView;

    SimpleDraweeView sdv_img;
    UserInfoEntity userInfoEntity;
    private LoadingDialog loadingDialog;
    private Gson gson = new GsonBuilder().create();
    TextView tv_money,tv_btn;
    MyRebateAdapter myRebateAdapter;
    List<RebateList.DataEntity> list;
    double money=0.0;
    @Override
    protected int getViewResId() {
        return R.layout.activity_rebate;
    }


    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        ActionBarUtil.initActionBar(getSupportActionBar(), "我的克拉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }, "提现明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提现明细
                startActivity(new Intent(MyRebateActivity.this,MyWithdrawActivity.class));
            }
        });
        loadingDialog = LoadingDialog.newInstance("加载中...");
        loadingDialog.show(getFragmentManager());

        LinearLayout llHead = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.rebate_head, null, false);
        sdv_img= (SimpleDraweeView) llHead.findViewById(R.id.sdv_img);
        tv_money= (TextView) llHead.findViewById(R.id.tv_money);
        tv_btn= (TextView) llHead.findViewById(R.id.tv_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRebateAdapter = new MyRebateAdapter(this);
        myRebateAdapter.addHeaderView(llHead);
        recyclerView.setAdapter(myRebateAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, myRebateAdapter.getHeaderCount()));
        sdv_img.setImageURI(Constant.URL.BaseImg+userInfoEntity.getHeadURL());
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        money=Double.parseDouble(getIntent().getStringExtra("money"));
        tv_money.setText("¥"+money);
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提现
                if (money==0){
                    ToastUtil.initToast(MyRebateActivity.this,"可提现克拉为0");
                }else if (money<100){
                    ToastUtil.initToast(MyRebateActivity.this,"可提现克拉为100的倍数");
                }else {
                    startActivityForResult(new Intent(MyRebateActivity.this, MyWithdrawalsActivity.class).putExtra("money",money),0);
                }
            }
        });
    }

    @Override
    protected void loadData() {

        OkHttpUtil.postJson(Constant.URL.getUserCaratList, "phone", userInfoEntity.getPhone(), new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                RebateList rebateList = gson.fromJson(json, RebateList.class);
                dismissLoading();
                list = new ArrayList<>();
                list.addAll(rebateList.getList());
                myRebateAdapter.setData(list);
//                if(list.size()>0){
//                }
            }

            @Override
            public void onFailure(String url, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(MyRebateActivity.this, "获取失败，刷新重试");
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
            if (requestCode==0){
                tv_money.setText("¥"+(money-Integer.parseInt(data.getStringExtra("money"))));
            }
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
