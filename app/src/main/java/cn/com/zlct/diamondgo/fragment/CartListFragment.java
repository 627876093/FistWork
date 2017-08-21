package cn.com.zlct.diamondgo.fragment;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.LoginActivity;
import cn.com.zlct.diamondgo.activity.PaymentActivity;
import cn.com.zlct.diamondgo.activity.ProjectDetailActivity;
import cn.com.zlct.diamondgo.adapter.CartRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.custom.ConfirmDialog;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.custom.LoadingDialogSimple;
import cn.com.zlct.diamondgo.model.CartListEntity;
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * "购物车" Fragment
 */
public class CartListFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        OkHttpUtil.OnDataListener {

    @BindView(R.id.toolbar_cart)
    public Toolbar toolbarAll;
    @BindView(R.id.tv_toolbarTitleCart)
    public TextView toolbarTitle;
    @BindView(R.id.srl_cartList)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_cartList)
    public RecyclerView recyclerView;
    @BindView(R.id.ll_empty)
    public LinearLayout llEmpty;
    @BindView(R.id.tv_emptyTips)
    public TextView tvEmpty;
    @BindView(R.id.ll_cartListBalance)
    public LinearLayout llPay;
    @BindView(R.id.tv_cartListTotal)
    public TextView tvCartTotal;
    @BindView(R.id.tv_useExper)
    public TextView tvUseExp;
    @BindView(R.id.tv_toolbarbj)
    TextView tv_bj;

    @BindView(R.id.ib_all)
    ImageButton ib_all;
    @BindView(R.id.tv_all)
    TextView tv_all;

    private String userId;
    private int typeNum;
    private int userExper = 0;
    int totalMoney = 0;//总需金额
    //    private int upDoneCount;//获取数据后处理完成的数量
//    private boolean newFlag;//是否有新手商品
//    private boolean expFlag;//是否同步体验金金额
    private boolean isRevising;//是否正在修改商品数量中
    //    private boolean deletable;//是否可以删除无用条目
//    private boolean needRefresh;//是否需要刷新
    private CartRecyclerViewAdapter recyclerViewAdapter;
    private List<CartListEntity.DataEntity> cartList;
    private String jsonList;

    private List<String> delItem;
    private LoadingDialog loadingDialog;
    private ConfirmDialog delDialog;

//    private LoadingDialogSimple loadingDialogSimple;

    private Gson gson = new GsonBuilder().create();
    private DecimalFormat decimal = new DecimalFormat("0.00");//保留两位小数使用

    @Override
    protected int getViewResId() {
        return R.layout.fragment_cart_list;
    }

    @Override
    protected void init(View view) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarAll);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("购物车");
        userId = SharedPreferencesUtil.getUserId(getActivity());
        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getActivity().getFragmentManager(), "loading");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAdapter = new CartRecyclerViewAdapter(getActivity(), this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        delItem = new ArrayList<>();
        tvEmpty.setText("购物车为空");

        //编辑按钮
        tv_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_bj.getText().equals("编辑")) {
                    tv_bj.setText("完成");
                    for (int i = 0; i < cartList.size(); i++) {
                        cartList.get(i).setIsbj(true);
                    }
                } else {
                    tv_bj.setText("编辑");
                    for (int i = 0; i < cartList.size(); i++) {
                        cartList.get(i).setIsbj(false);
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();


            }
        });

        //全选按钮
        ib_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                selectAll(v.isSelected());
                totalMoney = 0;
                updateMoney();
            }
        });
    }

    private void selectAll(boolean selected) {
        for (int i = 0; i < cartList.size(); i++) {
            cartList.get(i).setChecked(selected);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {
        isRevising = false;
//        expFlag = false;
//        deletable = false;
//        needRefresh = false;
        OkHttpUtil.postJson(Constant.URL.getShoppingCart, "phone", userId, this);
    }

    @Override
    public void onResponse(String url, String json) {
        try {
            dismissLoading();
            if (!TextUtils.isEmpty(json)) {
                Log.e("loge", "购物车: " + json);
                if (Constant.URL.getShoppingCart.equals(url)) {//获取用户购物车信息
                    CartListEntity cartListEntity = new Gson().fromJson(json, CartListEntity.class);
                    cartList = new ArrayList<>();
                    cartList.addAll(cartListEntity.getList());
                    jsonList = json;

                    if (tv_bj.getText().equals("编辑")) {
                        tv_bj.setText("编辑");
                        for (int i = 0; i < cartList.size(); i++) {
                            cartList.get(i).setIsbj(false);
                        }
                    } else {
                        tv_bj.setText("完成");
                        for (int i = 0; i < cartList.size(); i++) {
                            cartList.get(i).setIsbj(true);
                        }
                    }
                    recyclerViewAdapter.setData(cartList);
                    upEmpty();
                    cartListener.onCartUp(cartList.size());
                    totalMoney = 0;
                    updateMoney();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(String url, String error) {
    }

    /**
     * 是否显示空数据提示
     */
    private void upEmpty() {
        if (cartList.size() > 0) {
            if (llPay.getVisibility() != View.VISIBLE) {
                llPay.setVisibility(View.VISIBLE);
            }
            if (llEmpty.getVisibility() != View.GONE) {
                llEmpty.setVisibility(View.GONE);
            }
        } else {
            if (llPay.getVisibility() != View.GONE) {
                llPay.setVisibility(View.GONE);
            }
            if (llEmpty.getVisibility() != View.VISIBLE) {
                llEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    /**
     * 购物车筛选并修改完毕后设置数据
     */
    public void setData() {
        if (cartList.size() > 0) {
            dismissLoading();
            recyclerViewAdapter.setData(cartList);
            cartListener.onCartUp(cartList.size());
            upEmpty();
            updateMoney();
        }
    }

    /**
     * 更新商品数量和总金额
     */
    private void updateMoney() {
        int i = 0;
        int isSelectedNum = 0;
        for (; i < cartList.size(); i++) {
            if (cartList.get(i).isChecked()) {
                isSelectedNum++;
            }
        }
        ib_all.setSelected(isSelectedNum == cartList.size() ? true : false);
        tv_all.setText("已选(" + isSelectedNum + ")");

        typeNum = 0;
        for (int j = 0; j < cartList.size(); j++) {
            CartListEntity.DataEntity cartEntity = cartList.get(j);
            if (cartEntity.isChecked()) {
                typeNum++;
                double price = Double.valueOf(cartEntity.getCommodity_price());
                totalMoney += Integer.parseInt(cartEntity.getCount()) * (int) price;
            }
        }
        tvCartTotal.setText(Html.fromHtml(String.format("合计<font color=#DD2727>¥" + totalMoney + "</font>")));
    }

    @Override
    public void onClick(View v) {
        final String carId = (String) v.getTag();
        int position = getCarPosition(carId);
        if (position < 0) {//未找到
            return;
        }
        switch (v.getId()) {
//            case R.id.tv_cartItemTitle://点击标题
            case R.id.sdv_cartItemImg://点击图片
                Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                intent.putExtra("projectId", cartList.get(position).getCommodityId());
                startActivity(intent);
                break;
            case R.id.ll_delete://删除按钮
                if (isRevising) {
                    ToastUtil.initToast(getActivity(), "修改未完成，请稍候");
                    break;
                }
                delDialog = ConfirmDialog.newInstance("确定要删除该商品吗?", "取消", "删除");
                delDialog.setOnBtnClickListener(new ConfirmDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(View view) {
                        isRevising = true;
                        final int index = getCarPosition(carId);
                        loadingDialog = LoadingDialog.newInstance("删除中");
                        loadingDialog.show(getActivity().getFragmentManager(), "deleting");

                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("commodityId", carId);//商品ID
                        builder.add("phone", userId);//手机号
                        OkHttpUtil.postJson(Constant.URL.deleteCommodity, builder,
                                new OkHttpUtil.OnDataListener() {
                                    @Override
                                    public void onResponse(String url, String json) {
                                        try {

                                            if (!TextUtils.isEmpty(json)) {
                                                Log.e("loge", json);
                                                SingleWordEntity cartInfo = new Gson().fromJson(json, SingleWordEntity.class);
                                                loadingDialog.dismiss();
                                                if ("success".equals(cartInfo.getMsg())) {
                                                    cartList.remove(index);
                                                    recyclerViewAdapter.notifyItemRemoved(index);
                                                    cartListener.onCartUp(cartList.size());
                                                    updateMoney();
                                                    if (cartList.size() == 0) {
                                                        upEmpty();
                                                    }
                                                } else {
                                                    ToastUtil.initToast(getActivity(), "删除失败");
                                                }
                                                isRevising = false;
                                            }
                                        } catch (Exception e) {
                                        }
                                    }

                                    @Override
                                    public void onFailure(String url, String error) {
                                    }
                                });
                    }
                });
                delDialog.show(getActivity().getFragmentManager(), "delete");
                break;
            case R.id.ib_cartItemCheck://选择Item
                v.setSelected(!v.isSelected());
                cartList.get(position).setChecked(v.isSelected());
                totalMoney = 0;
                updateMoney();
                break;
            case R.id.btn_cartMinus://数量减
                if (isRevising) {
                    ToastUtil.initToast(getActivity(), "修改未完成，请稍候");
                    break;
                }
                int copiesMinus = Integer.parseInt(cartList.get(position).getCount());
                if (copiesMinus < 2) {
                    ToastUtil.initToast(getActivity(), "已经是最小份额了");
                } else {
                    isRevising = true;
                    TextView etMinus = (TextView) v.getTag(R.id.tag_relation);
                    upCartItemNum(position, copiesMinus - 1, etMinus);
                }
                break;
            case R.id.btn_cartPlus://数据加
                if (isRevising) {
                    ToastUtil.initToast(getActivity(), "修改未完成，请稍候");
                    break;
                }
                int copiesPlus = Integer.parseInt(cartList.get(position).getCount());
                isRevising = true;
                TextView etPlus = (TextView) v.getTag(R.id.tag_relation);
                upCartItemNum(position, copiesPlus + 1, etPlus);
                break;
        }
    }

    /**
     * 修改购物车商品数量
     */
    private void upCartItemNum(final int position, final int copies, final TextView tvEdit) {

//        loadingDialogSimple = LoadingDialogSimple.newInstance();
//        loadingDialogSimple.show(getActivity().getFragmentManager(), "loading");

        loadingDialog = LoadingDialog.newInstance("");
        loadingDialog.show(getActivity().getFragmentManager(), "loading");

//        String direction = cartCopies - copies < 0 ? "0" : "1";
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("commodityId", cartList.get(position).getCommodityId());//商品ID
        builder.add("phone", userId);//手机号
        builder.add("count", copies + "");//商品数量
        OkHttpUtil.postJson(Constant.URL.updateCommodityCount, builder, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                if (!TextUtils.isEmpty(json)) {
                    Log.e("loge", json);
                    SingleWordEntity cartInfo = new Gson().fromJson(json, SingleWordEntity.class);
//                    loadingDialogSimple.dismiss();
                    loadingDialog.dismiss();
                    if ("success".equals(cartInfo.getMsg()) && position >= 0) {
                        cartList.get(position).setCount(copies + "");
                        tvEdit.setText(copies + "");
                        loadData();
                    } else {
                        ToastUtil.initToast(getActivity(), "修改失败");
                    }
                    isRevising = false;
                }
            }

            @Override
            public void onFailure(String url, String error) {
            }
        });
    }

    /**
     * 去结算按钮
     */
    @OnClick(R.id.btn_cartListBalance)
    public void toPay() {
        if ("default".equals(userId)) {//未登录
            Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
            startActivity(intentLogin);
        } else {
            if (tv_bj.getText().toString().equals("完成")){
                ToastUtil.initToast(getActivity(), "购物车编辑中，请完成后再结算");
                return;
            }
            if (swipeRefreshLayout.isRefreshing()) {
                ToastUtil.initToast(getActivity(), "购物车刷新中,请稍后");
            }

            if (typeNum > 0) {
                if (isRevising) {
                    ToastUtil.initToast(getActivity(), "修改未完成，请稍候");
//                } else if (newHand > 0 && userExper < newHand) {
//                    final Snackbar snackbar = Snackbar.make(swipeRefreshLayout, "体验金余额为" + userExper +
//                            ",请修改体验商品的购买数量", Snackbar.LENGTH_INDEFINITE);
//                    snackbar.setAction("修改", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            for (int i = 0; i < cartList.size(); i++) {
//                                if ("1".equals(cartList.get(i).getProjectIsNewhand())) {
//                                    recyclerView.smoothScrollToPosition(i);
//                                    break;
//                                }
//                            }
//                            snackbar.dismiss();
//                        }
//                    });
//                    Snackbar.SnackbarLayout sbLayout = (Snackbar.SnackbarLayout) snackbar.getView();
//                    sbLayout.setAlpha(0.8f);
//                    snackbar.show();
                } else {
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer count = new StringBuffer();
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).isChecked()) {
                            buffer.append(cartList.get(i).getCommodityId());
                            count.append(cartList.get(i).getCount());
                            buffer.append("-");
                            count.append("-");

                        }
                    }
//                    buffer.substring(0, buffer.length()-1);
//                    count.substring(0, count.length()-1);
                    if (buffer.toString().length() > 0 && count.toString().length() > 0) {
                        buffer.deleteCharAt(buffer.length() - 1);
                        count.deleteCharAt(count.length() - 1);
                    }
                    Intent intentPayment = new Intent(getActivity(), PaymentActivity.class);
                    intentPayment.putExtra("type", 1);
                    intentPayment.putExtra("value", buffer.toString());//商品id
                    intentPayment.putExtra("money", totalMoney + "");
                    intentPayment.putExtra("list", jsonList);
                    intentPayment.putExtra("count", count.toString());

                    startActivityForResult(intentPayment, 0);
                }
            } else {
                ToastUtil.initToast(getActivity(), "共0种商品");
            }
        }
    }

    public int getCarPosition(String carId) {
        int position = -1;
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCommodityId().equals(carId)) {
                position = i;
                break;
            }
        }
        return position;
    }

}
