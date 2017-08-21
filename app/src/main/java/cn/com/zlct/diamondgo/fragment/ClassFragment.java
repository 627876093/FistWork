package cn.com.zlct.diamondgo.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.H5Activity;
import cn.com.zlct.diamondgo.activity.ProjectListActivity;
import cn.com.zlct.diamondgo.adapter.ClassLVAdapter;
import cn.com.zlct.diamondgo.adapter.ZoneLVAdapter;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.ClassInfoEntity;
import cn.com.zlct.diamondgo.model.ProjectListEntity;
import cn.com.zlct.diamondgo.model.PushInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;

/**
 * 分类 Fragment
 */
public class ClassFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

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
    List<ClassInfoEntity.DataEntity> list;
    ClassLVAdapter classLVAdapter;

    @Override
    protected int getViewResId() {
        return R.layout.fragment_zone;
    }

    @Override
    protected void init(View v) {
        llEmpty.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_new);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_toolbarTitle.setText(getString(R.string.main2));

        LinearLayout llHead = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.item_classhand, null, false);
        classLVAdapter = new ClassLVAdapter(getActivity());
        listView.addHeaderView(llHead);
        listView.setAdapter(classLVAdapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        tvEmpty.setText("暂无数据");
        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getActivity().getFragmentManager(), "loading");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProjectListActivity.class);
                intent.putExtra("id", list.get(position-1).getId());
                intent.putExtra("commodity_name", "");
                startActivity(intent);
            }
        });
        llHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全部商品
                Intent intent = new Intent(getActivity(), ProjectListActivity.class);
                intent.putExtra("id", "");
                intent.putExtra("commodity_name", "");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {
        OkHttpUtil.getJSON(Constant.URL.appgetCommodityClass, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {//获取商品分类
                Log.e("loge", "获取商品分类" + json);
                ClassInfoEntity pushInfoEntity = new Gson().fromJson(json, ClassInfoEntity.class);
                dismissLoading();
                list = new ArrayList<>();
                list.addAll(pushInfoEntity.getList());
                classLVAdapter.setData(list);

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(getContext(), "刷新失败，请重试");
                        dismissLoading();
                    }
                });
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
