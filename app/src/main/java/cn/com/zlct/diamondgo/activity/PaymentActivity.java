package cn.com.zlct.diamondgo.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.PaymentAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.DividerGridItemDecoration;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.CartListEntity;
import cn.com.zlct.diamondgo.model.GetAddressList;
import cn.com.zlct.diamondgo.model.PayResult;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.model.UserPaymentEntity;
import cn.com.zlct.diamondgo.model.WeiChatPaymentEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.AliPayUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.WeChatUtil;
import okhttp3.FormBody;

/**
 * 填写订单页面
 * Created by Administrator on 2017/6/5 0005.
 */
public class PaymentActivity extends BaseActivity implements OkHttpUtil.OnDataListener {

    @BindView(R.id.rv_payment)
    RecyclerView recyclerView;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_pay)
    TextView tv_pay;
    @BindView(R.id.tv_jf)
    TextView tv_jf;
    @BindView(R.id.tv_payMode)
    TextView tv_payMode;
    @BindView(R.id.tv_payMoney)
    TextView tv_payMoney;
    @BindView(R.id.ib_cartItemCheck)
    CheckBox checkBox;
    @BindView(R.id.tv_desc)
    TextView tv_desc;

    //头部地址栏控件
    TextView tv_name, tv_nameRed, tv_phone, tv_address;


    int type;//0:详情 1：购物车
    private String userId;
    private LoadingDialog loadingDialog;
    private String addressJson;
    private List<GetAddressList.DataEntity> addressList;//地址list
    private List<CartListEntity.DataEntity> list;
    private PaymentAdapter recyclerViewAdapter;
    private String[] str;
    private String value;

    String count;
    String commodityMoney, payMoney;
    double dMoney = 0;
    String payIntegral;
    String payMode = "2";
    UserInfoEntity u;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultStatus = payResult.getResultStatus();
//                    Log.e("loge", "resultStatus: 支付宝" + resultStatus);
//                    if ("9000".equals(resultStatus)) {
//                        ToastUtil.initToast(PaymentActivity.this, "支付成功");
//                        finish();
//                    } else {
//                        switch (resultStatus) {
//                            case "8000"://正在处理中
//                                ToastUtil.initToast(PaymentActivity.this, "正在处理中");
//                                break;
//                            case "4000"://订单支付失败
//                                ToastUtil.initToast(PaymentActivity.this, "支付失败");
//                                break;
//                            case "6001"://用户中途取消
//                                ToastUtil.initToast(PaymentActivity.this, "用户中途取消");
//                                break;
//                            case "6002"://网络连接出错
//                                ToastUtil.initToast(PaymentActivity.this, "网络连接出错");
//                                break;
//                        }
//                        return;
//                    }
                    Intent intent = new Intent(PaymentActivity.this, PayResultActivity.class);
                    intent.putExtra("payMode", "ali");
                    intent.putExtra("result", resultStatus);
                    startActivityForResult(intent, Constant.Code.PaymentCode);
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 支付后返回上一页面
     */
    private void payResult() {
        setResult(RESULT_OK);
        finish();
    }


    @Override
    protected int getViewResId() {
        return R.layout.activity_payment;
    }

    @Override
    protected void init() {
        userId = SharedPreferencesUtil.getUserId(this);
        u = PhoneUtil.getUserInfo(this);
        ActionBarUtil.initActionBar(getSupportActionBar(), "填写订单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadingDialog = LoadingDialog.newInstance("加载中...");
        loadingDialog.show(getFragmentManager());
        getIntentData();

        LinearLayout llHead = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.payment_hand, null, false);
        findViewByHead(llHead);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new PaymentAdapter(this);
        recyclerViewAdapter.addHeaderView(llHead);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerViewAdapter.setData(list);

        getAddressData();
        setUserJF();

    }

    private void setPayMode() {
        final String items[] = {"支付宝", "微信", "余额（" + u.getAccountbalance() + ")"};
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("选择支付方式"); //设置标题
        //builder.setMessage("是否确认退出?"); //设置内容
//        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                payMode = which + "";
                tv_payMode.setText(items[which]);
            }
        });

        builder.create().show();
    }

    private void setUserJF() {
//        payIntegral = u.getUserIntegral();
        payIntegral = u.getCurrency();
        if (payIntegral.equals("0")) {
            tv_jf.setText("您暂无现金分可抵扣");
            dMoney = 0;
        } else {
            dMoney = Double.parseDouble(commodityMoney) / 2;
            double dpayIntegral = Double.parseDouble(payIntegral);
            if (dpayIntegral >= dMoney) {
                tv_jf.setText("您有" + dpayIntegral + "分，可抵¥" + dMoney);
            } else {
                dMoney = dpayIntegral;
                tv_jf.setText("您有" + dpayIntegral + "分，可抵¥" + dMoney);
            }
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    double doui = Double.parseDouble(commodityMoney) - dMoney;
                    payMoney = doui + "";
                    tv_money.setText("实付：¥" + payMoney);
                } else {
//                    dMoney=0;
                    payMoney = commodityMoney;
                    tv_money.setText("实付：¥" + payMoney);
                }
            }
        });

    }

    private void findViewByHead(LinearLayout llHead) {
        tv_name = (TextView) llHead.findViewById(R.id.tv_name);
        tv_nameRed = (TextView) llHead.findViewById(R.id.tv_nameRed);
        tv_phone = (TextView) llHead.findViewById(R.id.tv_phone);
        tv_address = (TextView) llHead.findViewById(R.id.tv_address);

        llHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (addressList == null) {
//                    ToastUtil.initToast(PaymentActivity.this, "地址为空");
//                    return;
//                }
                Intent in = new Intent(PaymentActivity.this, PayAddressActivity.class);
                startActivityForResult(in, 1);

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1){
                getAddressData();
            }

            if (requestCode == Constant.Code.PaymentCode) {
                boolean result = data.getBooleanExtra("result", false);
                if (result) {
                    payResult();
                }
            }
            switch (requestCode) {
                case 0:
                    boolean result = data.getBooleanExtra("result", false);
                    if (result) {
                        setResult(RESULT_OK);
                        finish();
                    }
                    break;
                case 1:
                    try {
                        tv_name.setText(data.getStringExtra("name"));
                        tv_phone.setText(data.getStringExtra("phone"));
                        tv_address.setText(data.getStringExtra("address"));
                        tv_nameRed.setVisibility(View.GONE);
                        if (data.getStringExtra("isdefault").equals("1")) {
                            tv_nameRed.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

//        Intent intent = new Intent(this, PayResultActivity.class);
//        intent.putExtra("result", userRecharge.getErrNum());
//        startActivityForResult(intent, Constant.Code.PaymentCode);
    }

    public void getIntentData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if (type == 0) {
            CartListEntity.DataEntity dataEntity = new CartListEntity.DataEntity();
            dataEntity.setCommodityId(intent.getStringExtra("value"));
            dataEntity.setCommodity_name(intent.getStringExtra("name"));
            dataEntity.setCommodity_price(intent.getStringExtra("money"));
            dataEntity.setSort_name(intent.getStringExtra("sort_name"));
            dataEntity.setCount(intent.getStringExtra("count"));
            dataEntity.setCommodityUrl(intent.getStringExtra("commodity_url"));
            list = new ArrayList<>();
            list.add(dataEntity);
        } else if (type == 1) {
            list = new ArrayList<>();
            List<CartListEntity.DataEntity> arrayList = new ArrayList<>();
            CartListEntity cartListEntity = new Gson().fromJson(intent.getStringExtra("list"), CartListEntity.class);
            arrayList.addAll(cartListEntity.getList());
            String s = intent.getStringExtra("value");
            str = s.split("-");
            for (int i = 0; i < arrayList.size(); i++) {
                String id = arrayList.get(i).getCommodityId();
                for (int o = 0; o < str.length; o++) {
                    if (str[o].equals(id)) {
                        list.add(arrayList.get(i));
                        if (list.size() == str.length) {
                            break;
                        }
                    }
                }
            }
        }
        count = intent.getStringExtra("count");
        value = intent.getStringExtra("value");
        commodityMoney = intent.getStringExtra("money");
        tv_payMoney.setText("¥" + commodityMoney);
        tv_money.setText("实付：¥" + commodityMoney);
    }

    @OnClick({R.id.ll_payMode, R.id.tv_pay})
    public void OnBtn(View view) {
        switch (view.getId()) {
            case R.id.ll_payMode:
                setPayMode();
                break;
            case R.id.tv_pay:
                if (addressList.size()==0){
                    ToastUtil.initToast(this,"请先添加地址信息");
                    return;
                }
                if (payMode.equals("2")) {
                    double i;
                    if (checkBox.isChecked()) {
                        i = Double.parseDouble(commodityMoney) - (dMoney);
                    } else {
                        i = Double.parseDouble(commodityMoney);
                    }
                    if (i > Double.parseDouble(u.getAccountbalance())) {
                        ToastUtil.initToast(PaymentActivity.this, "余额不足，请选择其他支付方式");
                        break;
                    } else {
                        initPayment();
                    }
                } else {
                    initPayment();
                }
                break;
        }
    }

    public void initPayment() {
        loadingDialog = LoadingDialog.newInstance("正在生成订单...");
        loadingDialog.show(getFragmentManager());
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", userId);// 电话
        builder.add("commodity_id", value);// 商品id
        builder.add("eachcount", count);// 商品数量
        builder.add("recipientName", tv_name.getText().toString().trim() + "");// 收货人
        builder.add("recipientPhone", tv_phone.getText().toString().trim() + "");// 收货人电话
        builder.add("address", tv_address.getText().toString().trim() + "");// 收货地址
        builder.add("totalAmount", commodityMoney);// 商品总金额
        builder.add("integral", checkBox.isChecked() ? ((int) dMoney) + "" : 0 + "");// 消耗积分
        builder.add("paymentMethod", payMode);// 支付方式（0：支付宝，1：微信,2:余额）
        builder.add("desc", tv_desc.getText().toString().trim() + " ");// 备注
        OkHttpUtil.postJson(Constant.URL.appaddOrder, builder, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                dismissLoading();
                Log.e("loge", "生成订单：" + json);
                try {
                    JSONObject object = new JSONObject(json);
                    if (!object.getString("msg").equals("success")) {
                        return;
                    }
                    setPayment(object.getString("order_Account"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(String url, String error) {}
        });
    }


    public void setPayment(final String s) {
        if (checkBox.isChecked()) {
            final EditText et = new EditText(this);
            et.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            new AlertDialog.Builder(this).setTitle("请输入通宝账户支付密码")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String input = et.getText().toString();
                            if (input.equals("")) {
                                Toast.makeText(getApplicationContext(), "内容不能为空！" + input, Toast.LENGTH_LONG).show();
                            } else {
                                dialog.dismiss();
                                loadingDialog = LoadingDialog.newInstance("正在请求支付...");
                                loadingDialog.show(getFragmentManager());
                                FormBody.Builder builder = new FormBody.Builder();
                                builder.add("code", s);// 订单号
                                builder.add("phone", userId);// 电话
                                builder.add("money", commodityMoney);// 总金额
                                builder.add("payType", payMode);// 支付类型（0：支付宝，1：微信，2：余额）
                                builder.add("userIntegral", ((int) dMoney) + "");// 用户消耗积分
                                builder.add("deviceType", "1");// 设备类型（0：ios,1:安卓）
                                builder.add("userIntegralPW", input);// 消耗积分时需要传密码电话
                                OkHttpUtil.postJson(Constant.URL.userPayment, builder, PaymentActivity.this);
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
            loadingDialog.show(getFragmentManager());
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("code", s);// 订单号
            builder.add("phone", userId);// 电话
            builder.add("money", commodityMoney);// 总金额
            builder.add("payType", payMode);// 支付类型（0：支付宝，1：微信，2：余额）
            builder.add("userIntegral", 0 + "");// 用户消耗积分
            builder.add("deviceType", "1");// 设备类型（0：ios,1:安卓）
            builder.add("userIntegralPW", "");// 消耗积分时需要传密码电话
            OkHttpUtil.postJson(Constant.URL.userPayment, builder, this);
        }
    }

    public void getAddressData() {
        OkHttpUtil.postJson(Constant.URL.appgetAddress, "phone", userId, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", "地址列表：" + json);
                GetAddressList getUserBankCard = new GsonBuilder().create().fromJson(json, GetAddressList.class);
                dismissLoading();

                addressList = new ArrayList<>();
                addressList.addAll(getUserBankCard.getList());
                if (addressList.size() > 0) {
                    addressJson = json;
                    for (int i = 0; i < addressList.size(); i++) {
                        if (addressList.get(i).getIsdefault().equals("1")) {

                            addressList.get(i).getAddress();
                            addressList.get(i).getRecipientPhone();
                            addressList.get(i).getId();
                            tv_name.setText(addressList.get(i).getRecipientName() + "");
                            tv_phone.setText(addressList.get(i).getRecipientPhone() + "");
                            tv_address.setText(addressList.get(i).getAddress() + "");
                            tv_nameRed.setVisibility(View.GONE);
                            if (addressList.get(i).getIsdefault().equals("1")) {
                                tv_nameRed.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }
//                adapter.setData(list);
            }

            @Override
            public void onFailure(String url, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(PaymentActivity.this, "获取失败，刷新重试");
                        dismissLoading();
                    }
                });
            }
        });
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onResponse(String url, String json) {
        if (Constant.URL.userPayment.equals(url)) {
            dismissLoading();
            Log.e("loge", "支付接口：" + json);
//                {"code":"4691161","msg":"success"}
            if (payMode.equals("2")) {
                try {
                    JSONObject object = new JSONObject(json);
                    Intent intent = new Intent(this, PayResultActivity.class);
                    intent.putExtra("result", object.getString("msg"));
                    startActivityForResult(intent, Constant.Code.PaymentCode);
//                    if (!object.getString("msg").equals("success")) {
//                        ToastUtil.initToast(PaymentActivity.this, "支付失败");
//                        return;
//                    } else {
//                        ToastUtil.initToast(PaymentActivity.this, "支付成功");
//                        finish();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (payMode.equals("0")) {//支付宝
                    UserPaymentEntity userEntity = new Gson().fromJson(json, UserPaymentEntity.class);
                    aliPay(userEntity);
                } else if (payMode.equals("1")) {//微信
                    try {
                       String j= new JSONObject(json).getString("date");
                        WeiChatPaymentEntity userEntity=new GsonBuilder().create().fromJson(j,WeiChatPaymentEntity.class);
//                    SharedPreferencesUtil.saveWeChatPay(this, "weChat", "pay",(Integer.parseInt(commodityMoney)-(int) dMoney)+"");
                        String result = WeChatUtil.weChatPay(this, userEntity);
                        if (TextUtils.isEmpty(result)) {
                            dismissLoading();
                        }
                        dismissLoading();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }
    @Override
    public void onFailure(String url, String error) {}


    private void aliPay(UserPaymentEntity userEntity) {
        final String orderInfo = AliPayUtil.aliPay(this, userEntity);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask aliPay = new PayTask(PaymentActivity.this);
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

}
