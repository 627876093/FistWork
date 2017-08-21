package cn.com.zlct.diamondgo.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.IntegralLVAdapter;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.GetIntegralList;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;

/**
 * 商品详情 评论 页面
 * Created by Administrator on 2017/6/1 0001.
 */
public class ProCommentVPFragment extends BaseFragment implements OkHttpUtil.OnDataListener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.srl_purchaseRecord)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lv_purchaseRecord)
    public ListView listView;
    @BindView(R.id.ll_empty)
    public LinearLayout llEmpty;
    @BindView(R.id.tv_emptyTips)
    public TextView tvEmpty;
    private String type;
    private int page;//当前页
    private int nextPage;//下一页
    private static int pageSize = 15;//每页数量
    private IntegralLVAdapter integralLVAdapter;
    private List<GetIntegralList.DataEntity> balanceList;

    public static ProCommentVPFragment newInstance(String s,String type) {
        ProCommentVPFragment viewPagerFragment = new ProCommentVPFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", s);
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @Override
    protected int getViewResId() {
        return R.layout.fragment_purchase_record;//共用布局
    }

    @Override
    protected void getData(Bundle arguments) {
        type = arguments.getString("type");

        integralLVAdapter = new IntegralLVAdapter(getActivity());
        listView.setAdapter(integralLVAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        tvEmpty.setText("暂无评论");

    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(type)) {
            page = 1;
            nextPage = 1;
            if (balanceList == null) {
                balanceList = new ArrayList<>();
            } else {
                balanceList.clear();
            }
            getBalance();
        }
    }

    public void getBalance() {
        OkHttpUtil.postJson(Constant.URL.appQuery, "commodity_id", type, this);
    }

    @Override
    public void onResponse(String url, String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                Log.e("loge","评论：  "+ json);
//                if (Constant.URL.appQuery.equals(url)) {
//                    GetIntegralList balance = new Gson().fromJson(json, GetIntegralList.class);
////                    if (page == 1) {
//                    dismissLoading();
////                    }
////                    removeLastItem();
////                    if ("0".equals(balance.getErrNum())) {
//                    balanceList.addAll(balance.getList());
////                        if (balanceList.size() % pageSize == 0) {//可能还有下一页
////                            balanceList.add(new AccountsDetailsEntity.DataEntity(1));
////                            nextPage = page + 1;
////                        }
////                    } else {
////                        if (page != 1) {
////                            Toast.makeText(getActivity(), "已经到底了", Toast.LENGTH_SHORT).show();
////                        }
////                    }
//                    setAdapterData();
//                }
            }
        } catch (Exception e) {
        }
    }

    private void dismissLoading() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(String url, String error) {
    }

    private void removeLastItem() {
        if (balanceList.size() > 0) {
            if (balanceList.get(balanceList.size() - 1).getType() == 1) {
                balanceList.remove(balanceList.size() - 1);
            }
        }
    }

    private void setAdapterData() {
        integralLVAdapter.setData(balanceList);
        if (balanceList.size() > 0) {
            if (llEmpty.getVisibility() != View.GONE) {
                llEmpty.setVisibility(View.GONE);
            }
        } else {
            if (llEmpty.getVisibility() != View.VISIBLE) {
                llEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

//    @Override
//    public synchronized void onCallback() {
//        if (nextPage == page + 1) {
//            page++;
//            getBalance();
//        }
//
//    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
