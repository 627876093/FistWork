package cn.com.zlct.diamondgo.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.AppContext;
import cn.com.zlct.diamondgo.MainActivity;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.CommentAddActivity;
import cn.com.zlct.diamondgo.activity.GetOrderActivity;
import cn.com.zlct.diamondgo.activity.H5Activity;
import cn.com.zlct.diamondgo.activity.OrderRefundActivity;
import cn.com.zlct.diamondgo.activity.ProjectDetailActivity;
import cn.com.zlct.diamondgo.adapter.OrderRecordLVAdapter;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.custom.ConfirmDialog;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.OrderList;
import cn.com.zlct.diamondgo.model.PayResult;
import cn.com.zlct.diamondgo.model.UserPaymentEntity;
import cn.com.zlct.diamondgo.util.AliPayUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * “订单”页面的ViewPagerFragment
 */
public class PurRecordVPFragment extends BaseFragment implements OkHttpUtil.OnDataListener, View.OnClickListener,
        AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.srl_purchaseRecord)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lv_purchaseRecord)
    public ListView listView;
    @BindView(R.id.ll_empty)
    public LinearLayout llEmpty;
    @BindView(R.id.tv_emptyTips)
    public TextView tvEmpty;


    int mark;
    private String jsonString;

    private int page;//当前页
    private int nextPage;//下一页
    private static int pageSize = 15;//每页数量
    private OrderRecordLVAdapter listViewAdapter;
    private List<OrderList.DataEntity> orderList;
    private LoadingDialog loadingDialog;
    private Object order;

    public static PurRecordVPFragment newInstance(String jsonString, int mark) {
        PurRecordVPFragment viewPagerFragment = new PurRecordVPFragment();
        Bundle bundle = new Bundle();
        bundle.putString("jsonString", jsonString);
        bundle.putInt("mark", mark);
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @Override
    protected int getViewResId() {
        return R.layout.fragment_purchase_record;
    }

    @Override
    protected void getData(Bundle arguments) {
        jsonString = arguments.getString("jsonString");
        mark = arguments.getInt("mark");
        listViewAdapter = new OrderRecordLVAdapter(getActivity(), mark, this, new OnAdapterCallbackListener() {
            @Override
            public void onCallback() {
                if (nextPage == page + 1) {
                    page++;
                    try {
                        getBalance();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        tvEmpty.setText("暂无订单数据");


        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getActivity().getFragmentManager(), "loading");
    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(jsonString)) {
            page = 1;
            nextPage = 1;
            try {
                getBalance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getBalance() throws JSONException {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", new JSONObject(jsonString).getString("phone"));
//      0全部，1待付款，2待发货，3待收货，4待退款
//      -1:全部 0：未支付；1：支付待发货；2：发货待收货；3：收货待评价；4：已评价;5:待退款
        String order_state = "-1";
        if (mark == 0) {//全部
            order_state = "-1";
        }
        if (mark == 1) {//待付款
            order_state = "0";
        }
        if (mark == 2) {//待发货
            order_state = "1";
        }
        if (mark == 3) {//待收货
            order_state = "2";
        }
        if (mark == 4) {//待退款
            order_state = "5";
        }
        builder.add("order_state", order_state);
        builder.add("page", page + "");
        builder.add("pageSize", pageSize + "");
        OkHttpUtil.postJson(Constant.URL.appgetOrderList, builder, this);
    }

    @Override
    public void onResponse(String url, String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                dismissLoading();
                if (Constant.URL.appgetOrderList.equals(url)) {//获取我的订单列表
                    Log.e("loge", "订单列表: " + json);
                    OrderList financing = new Gson().fromJson(json, OrderList.class);
                    if (page == 1) {
                        if (orderList == null) {
                            orderList = new ArrayList<>();
                        } else {
                            orderList.clear();
                        }
                    }
                    removeLastItem();
                    if (financing.getList().size() > 0) {
                        orderList.addAll(financing.getList());
                        if (orderList.size() % pageSize == 0) {//可能还有下一页
                            orderList.add(new OrderList.DataEntity(1));
                            nextPage = page + 1;
                        }
                    } else {
                        if (page != 1) {
                            Toast.makeText(getActivity(), "已经到底了", Toast.LENGTH_SHORT).show();
                        }
                    }
                    setAdapterData();
                }
            }
        } catch (Exception e) {
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

    private void removeLastItem() {
        if (orderList.size() > 0) {
            if (orderList.get(orderList.size() - 1).getType() == 1) {
                orderList.remove(orderList.size() - 1);
            }
        }
    }

    @Override
    public void onFailure(String url, String error) {}

    private void setAdapterData() {
        listViewAdapter.setData(orderList);
        if (orderList.size() > 0) {
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
    public void onClick(final View v) {
        try {
            final int p = (int) v.getTag(R.id.tag_order_btn1);
            String str = (String) v.getTag(R.id.tag_order_btn2);
//            switch (v.getId()) {
//                case R.id.order_tv_btn1:
            if (str.equals("删除订单")) {
                final ConfirmDialog delDialog = ConfirmDialog.newInstance("删除订单", "取消", "确认");
                delDialog.setOnBtnClickListener(new ConfirmDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(View view) throws Exception {
                        loadingDialog = LoadingDialog.newInstance("加载中");
                        loadingDialog.show(getActivity().getFragmentManager(), "loading");
                        OkHttpUtil.postJson(Constant.URL.appdeleteOrder, "order_Account", orderList.get(p).getOrder_Account(), new orderOkHttp());
                    }
                });
                delDialog.show(getActivity().getFragmentManager(), "logout");
            } else if (str.equals("提醒发货")) {
                ToastUtil.initToast(getActivity(), "已发送提醒");
            } else if (str.equals("确认收货")) {
                ConfirmDialog delDialog = ConfirmDialog.newInstance("确认收货", "取消", "确认");
                delDialog.setOnBtnClickListener(new ConfirmDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(View view) throws Exception {
                        loadingDialog = LoadingDialog.newInstance("加载中");
                        loadingDialog.show(getActivity().getFragmentManager(), "loading");
                        OkHttpUtil.postJson(Constant.URL.confirmReceipt, "code", orderList.get(p).getOrder_Account(), new orderOkHttp());
                    }
                });
                delDialog.show(getActivity().getFragmentManager(), "logout");
            } else if (str.equals("查看物流")) {
                loadingDialog = LoadingDialog.newInstance("加载中");
                loadingDialog.show(getActivity().getFragmentManager(), "loading");
                OkHttpUtil.postJson(Constant.URL.queryLogisticsNo, "order_Account", orderList.get(p).getOrder_Account(), new OkHttpUtil.OnDataListener() {
                    @Override
                    public void onResponse(String url, String json) {
                        try {
                            Log.e("loge", json);
                            dismissLoading();
                            JSONObject object = new JSONObject(json);
                            if (object.getString("msg").equals("success")) {
                                String urlStr=Constant.URL.BaseImg+object.getString("data");
                                Intent intent=new Intent(getContext(),H5Activity.class);
                                intent.putExtra("url", urlStr);
                                intent.putExtra("title","物流信息");
                                startActivity(intent);
                            }else {
                                ToastUtil.initToast(getContext(),"请求失败，请重试");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String url, String error) {}
                });
            } else if (str.equals("申请退款")) {
                //申请退款
//                        Intent intent=new Intent(getContext(), OrderRefundActivity.class);
//                        intent.putExtras("",orderList.get(p).);
//                        startActivityForResult(intent,0);
                ConfirmDialog delDialog = ConfirmDialog.newInstance("申请退款", "取消", "确认");
                delDialog.setOnBtnClickListener(new ConfirmDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(View view) throws Exception {
                        loadingDialog = LoadingDialog.newInstance("加载中");
                        loadingDialog.show(getActivity().getFragmentManager(), "loading");
                        OkHttpUtil.postJson(Constant.URL.userRefund, "code", orderList.get(p).getOrder_Account(), new orderOkHttp());
                    }
                });
                delDialog.show(getActivity().getFragmentManager(), "logout");
            } else
//                    break;
//                case R.id.order_tv_btn2:
                if (str.equals("付款")) {
                    setPayment(p);
//                        switch (orderList.get(p).getPaymentMethod()){
//                            case 0://支付宝
//                                break;
//                            case 1://微信
//                                break;
//                            case 2://余额
//                                break;
//                        }
                } else if (str.equals("评价")) {
                    //评价
                    Intent intent = new Intent(getContext(), CommentAddActivity.class);
                    intent.putExtra("code", orderList.get(p).getOrder_Account().toString());
                    intent.putExtra("commodity_id", orderList.get(p).getAccList().get(0).getCmdId());
                    startActivity(intent);

                } else if (str.equals("listview")) {
                    //订单详情
                    IntentGetOrder(p);
                }
        } catch (Exception e) {
        }
    }

    public void setPayment(final int p) {

        if (orderList.get(p).getIntegral() != 0) {
            final EditText et = new EditText(getContext());
            et.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            new AlertDialog.Builder(getContext()).setTitle("请输入通宝账户支付密码")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String input = et.getText().toString();
                            if (input.equals("")) {
                                ToastUtil.initToast(getContext(), "内容不能为空");
                            } else {
                                dialog.dismiss();

                                loadingDialog = LoadingDialog.newInstance("正在请求支付...");
                                loadingDialog.show(getActivity().getFragmentManager());
                                FormBody.Builder builder = new FormBody.Builder();
                                builder.add("code", orderList.get(p).getOrder_Account());// 订单号
                                builder.add("phone", SharedPreferencesUtil.getPhone(getContext()));//
                                double Account_price = 0;//合计价格
                                for (int i = 0; i < orderList.get(p).getAccList().size(); i++) {
                                    Account_price += orderList.get(p).getAccList().get(i).getCommodity_price() * orderList.get(p).getAccList().get(i).getCount();
                                }
                                builder.add("money", Account_price + "");// 总金额
                                builder.add("payType", orderList.get(p).getPaymentMethod() + "");// 支付类型（0：支付宝，1：微信，2：余额）
                                builder.add("userIntegral", orderList.get(p).getIntegral() + "");// 用户消耗积分
                                builder.add("deviceType", "1");// 设备类型（0：ios,1:安卓）
                                builder.add("userIntegralPW", input);// 消耗积分时需要传密码

                                OkHttpUtil.postJson(Constant.URL.userPayment, builder, new userPayment(orderList.get(p).getPaymentMethod()));


                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    return;
                }
            }).show();
        } else {
            loadingDialog = LoadingDialog.newInstance("正在请求支付...");
            loadingDialog.show(getActivity().getFragmentManager());
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("code", orderList.get(p).getOrder_Account());// 订单号
            builder.add("phone", SharedPreferencesUtil.getPhone(getContext()));// 电话
            double Account_price = 0;//合计价格
            for (int i = 0; i < orderList.get(p).getAccList().size(); i++) {
                Account_price += orderList.get(p).getAccList().get(i).getCommodity_price() * orderList.get(p).getAccList().get(i).getCount();
            }
            builder.add("money", Account_price + "");// 总金额
            builder.add("payType", orderList.get(p).getPaymentMethod() + "");// 支付类型（0：支付宝，1：微信，2：余额）
            builder.add("userIntegral", 0 + "");// 用户消耗积分
            builder.add("deviceType", "1");// 设备类型（0：ios,1:安卓）
            builder.add("userIntegralPW", "");// 消耗积分时需要传密码电话

            OkHttpUtil.postJson(Constant.URL.userPayment, builder, new userPayment(orderList.get(p).getPaymentMethod()));

        }
    }

    public void IntentGetOrder(int p) {
        Intent intentDetail = new Intent(getActivity(), GetOrderActivity.class);
        intentDetail.putExtra("order_Account", orderList.get(p).getOrder_Account());
        startActivity(intentDetail);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //订单详情
        IntentGetOrder(position);


    }

    class userPayment implements OkHttpUtil.OnDataListener {
        int payMode;

        public userPayment(int payMode) {
            this.payMode = payMode;
        }

        @Override
        public void onResponse(String url, String json) {
            dismissLoading();
            Log.e("loge", "支付接口：" + json);
//                {"code":"4691161","msg":"success"}
            if (payMode == 2) {
                try {
                    JSONObject object = new JSONObject(json);
                    if (!object.getString("msg").equals("success")) {
                        ToastUtil.initToast(getActivity(), "支付失败");
                        return;
                    } else {
                        ToastUtil.initToast(getContext(), "支付成功");
                        loadingDialog = LoadingDialog.newInstance("加载中");
                        loadingDialog.show(getActivity().getFragmentManager(), "loading");
                        loadData();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                UserPaymentEntity userEntity = new Gson().fromJson(json, UserPaymentEntity.class);
                if (payMode == 0) {//支付宝
                    aliPay(userEntity);

                } else if (payMode == 1) {//微信

//                    SharedPreferencesUtil.saveWeChatPay(this, "weChat", "pay", userEntity.getSettlementMoney());
//                    String result = WeChatUtil.weChatPay(this, userEntity);
//                    if (TextUtils.isEmpty(result)) {
//                        dismissLoading();
//                    } else {
//                        isUseWeChat = true;
//                    }

                }
            }
        }

        @Override
        public void onFailure(String url, String error) {

        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    Log.e("loge", "resultStatus: 支付宝" + resultStatus);
                    if ("9000".equals(resultStatus)) {
                        ToastUtil.initToast(getContext(), "支付成功");
                        loadingDialog = LoadingDialog.newInstance("加载中");
                        loadingDialog.show(getActivity().getFragmentManager(), "loading");
                        loadData();
                    } else {
                        switch (resultStatus) {
                            case "8000"://正在处理中
                                ToastUtil.initToast(getContext(), "正在处理中");
                                break;
                            case "4000"://订单支付失败
                                ToastUtil.initToast(getContext(), "支付失败");
                                break;
                            case "6001"://用户中途取消
                                ToastUtil.initToast(getContext(), "用户中途取消");
                                break;
                            case "6002"://网络连接出错
                                ToastUtil.initToast(getContext(), "网络连接出错");
                                break;
                        }
                        return;
                    }
//                    Intent intent = new Intent(getContext(), PayResultActivity.class);
//                    intent.putExtra("payMode", "ali");
//                    intent.putExtra("result", resultStatus);
//                    startActivityForResult(intent, Constant.Code.PaymentCode);
                    break;
                }
                default:
                    break;
            }
        }
    };


    private void aliPay(UserPaymentEntity userEntity) {
        final String orderInfo = AliPayUtil.aliPay(getContext(), userEntity);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask aliPay = new PayTask(getActivity());
                String result = aliPay.pay(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @Override
    public void onRefresh() {
        loadData();
    }


    class orderOkHttp implements OkHttpUtil.OnDataListener {

        @Override
        public void onResponse(String url, String json) {
            try {
                Log.e("loge", json);
                dismissLoading();
                JSONObject object = new JSONObject(json);
                if (object.getString("msg").equals("success")) {
                    if (url.equals(Constant.URL.appdeleteOrder)) {//删除订单
                        Log.e("loge", "删除订单成功");
                    } else if (url.equals(Constant.URL.confirmReceipt)) {//确认收货
                        Log.e("loge", "确认收货成功");
                    } else if (url.equals(Constant.URL.userRefund)) {//申请退款
                        Log.e("loge", "申请退款成功");
                    }

                    loadingDialog = LoadingDialog.newInstance("加载中");
                    loadingDialog.show(getActivity().getFragmentManager(), "loading");
                    loadData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(String url, String error) {

        }
    }
}
