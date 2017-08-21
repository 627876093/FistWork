package cn.com.zlct.diamondgo.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.AddressActivity;
import cn.com.zlct.diamondgo.activity.H5Activity;
import cn.com.zlct.diamondgo.activity.MyCircleActivity;
import cn.com.zlct.diamondgo.activity.MyCollectionActivity;
import cn.com.zlct.diamondgo.activity.MyRebateActivity;
import cn.com.zlct.diamondgo.activity.MyRecommendActivity;
import cn.com.zlct.diamondgo.activity.OrderActivity;
import cn.com.zlct.diamondgo.activity.SettingActivity;
import cn.com.zlct.diamondgo.activity.UserInfoActivity;
import cn.com.zlct.diamondgo.adapter.MineLVAdapter;
import cn.com.zlct.diamondgo.adapter.ZoneLVAdapter;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.custom.ListViewInScrollView;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.LocalEntity;
import cn.com.zlct.diamondgo.model.PushInfoEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.Util;

/**
 * 推广信息 Fragment
 */
public class ZoneFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.ll_empty)
    public LinearLayout llEmpty;
    @BindView(R.id.tv_emptyTips)
    public TextView tvEmpty;
    @BindView(R.id.toolbar_new)
    Toolbar toolbar_new;
    @BindView(R.id.tv_toolbarTitle)
    TextView tv_toolbarTitle;
    @BindView(R.id.sv_zone)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.lv_zone)
    ListView listView;





    private Gson gson = new GsonBuilder().create();
    private LoadingDialog loadingDialog;
    List<PushInfoEntity.DataEntity> list;
    ZoneLVAdapter zoneLVAdapter;

    @Override
    protected int getViewResId() {
        return R.layout.fragment_zone;
    }

    @Override
    protected void init(View v) {
        llEmpty.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_new);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_toolbarTitle.setText(getString(R.string.main1));


        zoneLVAdapter = new ZoneLVAdapter(getActivity());
        listView.setAdapter(zoneLVAdapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        tvEmpty.setText("暂无数据");
        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getActivity().getFragmentManager(), "loading");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), H5Activity.class);
                intent.putExtra("title",list.get(position).getPush_title());
                intent.putExtra("url",list.get(position).getPush_url());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void loadData() {
        String phone = SharedPreferencesUtil.getPhone(getActivity());
        OkHttpUtil.getJSON(Constant.URL.appgetPushInfo, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", "获取推广信息" + json);
                PushInfoEntity pushInfoEntity = new Gson().fromJson(json, PushInfoEntity.class);
                dismissLoading();
                list = new ArrayList<>();
                list.addAll(pushInfoEntity.getList());
                zoneLVAdapter.setData(list);

                if (list.size() > 0) {
                    if (llEmpty.getVisibility() != View.GONE) {
                        llEmpty.setVisibility(View.GONE);
                    }
                }else {
                    if (llEmpty.getVisibility() != View.VISIBLE) {
                        llEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(String url, String error) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.initToast(getContext(), "刷新失败，请重试");
//                        dismissLoading();
//                    }
//                });
            }
        });
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
