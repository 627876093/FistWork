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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.UserAddressAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.ConfirmDialog;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.GetAddressList;
import cn.com.zlct.diamondgo.model.MsgEntity;
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import okhttp3.FormBody;

/**
 * 地址列表
 * Created by Administrator on 2017/5/23 0023.
 */
public class AddressActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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

    UserAddressAdapter adapter;
    List<GetAddressList.DataEntity> list;

    @Override
    protected int getViewResId() {
        return R.layout.activity_recycler;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        ToolBarUtil.initToolBar(toolbar, "收货地址", R.drawable.add_white, this);
        loadingDialog = LoadingDialog.newInstance("加载中...");
        loadingDialog.show(getFragmentManager());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAddressAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, adapter.getHeaderCount()));

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        tvEmpty.setText("暂无收货地址");
    }

    @Override
    protected void loadData() {
        OkHttpUtil.postJson(Constant.URL.appgetAddress, "phone", userInfoEntity.getPhone(), new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                GetAddressList getUserBankCard = gson.fromJson(json, GetAddressList.class);
                dismissLoading();
                list = new ArrayList<>();
                list.addAll(getUserBankCard.getList());
                adapter.setData(list);
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
                        ToastUtil.initToast(AddressActivity.this, "获取失败，刷新重试");
                        dismissLoading();
                    }
                });
            }
        });
    }

    String addressId = null;

    @Override
    public void onClick(final View v) {
        if (v.getTag()!=null) {
            if (list==null){
                addressId = list.get((Integer) v.getTag()).getId();
            }
        }
        switch (v.getId()) {
            case R.id.toolbar_iconBack:
                onBackPressed();
                break;
            case R.id.toolbar_iconNext://新增
                if (list==null){
                    list = new ArrayList<>();
                }
                Intent intentEdit = new Intent(this, AddressAddActivity.class);
                intentEdit.putExtra("isDefault", list.size() > 0 ? false : true);
                startActivityForResult(intentEdit, Constant.Code.AddAddressCode);
                break;

            case R.id.rb_defaultAddress://设为默认
                if (!v.isSelected()) {
                    setDefaultAddress(addressId);
                }
                break;
            case R.id.iv_addressDelete://删除
                ConfirmDialog confirmDialog = ConfirmDialog.newInstance("确定要删除该地址吗?", "取消", "确定");
                confirmDialog.show(getFragmentManager(), "delete");
                confirmDialog.setOnBtnClickListener(new ConfirmDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(View view) {
                        loadingDialog = LoadingDialog.newInstance("删除中");
                        loadingDialog.show(getFragmentManager(), "delete");

                        FormBody.Builder builder = new FormBody.Builder();
//                        @"id" : address.addressID,    // 商品名称（可选）
//                        @"phone" : userInfo.phone,                  // 分类id（可选）
//                        @"isdefault" : address.isdefault                // 品牌id（可选）
                        builder.add("phone", userInfoEntity.getPhone());
                        builder.add("id", addressId);
                        builder.add("isdefault", list.get((Integer) v.getTag()).getIsdefault());
                        OkHttpUtil.postJson(Constant.URL.DeleteAddress, builder, new OkHttpUtil.OnDataListener() {
                            @Override
                            public void onResponse(String url, String json) {

                                SingleWordEntity addressEntity = new Gson().fromJson(json, SingleWordEntity.class);
                                Log.e("loge", json);
                                dismissLoading();
                                ToastUtil.initToast(AddressActivity.this,addressEntity.getMsg().equals("success")? "成功":"失败");
                                loadData();

                            }
                            @Override
                            public void onFailure(String url, String error) {}
                        });

                    }
                });
                break;
            case R.id.ll_addressEdit://编辑
            case R.id.tv_addressEdit://编辑
                intoEdit(addressId, (Integer) v.getTag());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }


    /**
     * 进入编辑页面
     */
    public void intoEdit(String addressId, int i) {
        GetAddressList.DataEntity shippingAddress = list.get(findShippingById(addressId));
        String gsonStr = gson.toJson(shippingAddress);
        Intent intentAddressEdit = new Intent(this, AddressAddActivity.class);
        intentAddressEdit.putExtra("type", 1);
        intentAddressEdit.putExtra("isDefault", (list.get(i).getIsdefault()).equals("1") ? true:false);
        intentAddressEdit.putExtra("address", gsonStr);
        intentAddressEdit.putExtra("id", addressId);
        intentAddressEdit.putExtra("name", shippingAddress.getRecipientName());
        intentAddressEdit.putExtra("phone", shippingAddress.getRecipientPhone());

        startActivityForResult(intentAddressEdit, Constant.Code.EditAddressCode);
    }

    /**
     * 根据addressId找回地址实体
     */
    public int findShippingById(String addressId) {
        for (int i = 0; i < list.size(); i++) {
            if (addressId.equals(list.get(i).getId())) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 改变默认按钮
     */
    public void setDefaultAddress(String addressId) {
        boolean flag1 = false, flag2 = false;
        for (int i = 0; i < list.size(); i++) {
            GetAddressList.DataEntity address = list.get(i);
            if (addressId.equals(address.getId())) {
                address.setIsdefault(1 + "");
                flag1 = true;
                adapter.notifyItemChanged(i);

                FormBody.Builder builder = new FormBody.Builder();
                builder.add("phone", userInfoEntity.getPhone());
                builder.add("id", addressId);
                OkHttpUtil.postJson(Constant.URL.setDefaultAddress, builder, new OkHttpUtil.OnDataListener() {
                    @Override
                    public void onResponse(String url, String json) {
                        Log.e("loge", json);
                    }

                    @Override
                    public void onFailure(String url, String error) {

                    }
                });

            } else if ("1".equals(address.getIsdefault())) {
                address.setIsdefault(0 + "");
                flag2 = true;
                adapter.notifyItemChanged(i);
            }
            if (flag1 && flag2) {
                break;
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
