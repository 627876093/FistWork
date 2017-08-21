package cn.com.zlct.diamondgo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.ListViewInScrollView;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.OrderList;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import okhttp3.FormBody;

/**
 * 订单详情
 */
public class GetOrderActivity extends BaseActivity {
    @BindView(R.id.tv_orderTime)
    TextView tv_orderTime;
    @BindView(R.id.tv_orderbh)
    TextView tv_orderbh;
    @BindView(R.id.tv_orderMoney)
    TextView tv_orderMoney;
    @BindView(R.id.tv_orderType)
    TextView tv_orderType;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.lv_order)
    ListViewInScrollView lv_order;
    @BindView(R.id.tv_orderly)
    TextView tv_orderly;
    @BindView(R.id.tv_payType)
    TextView tv_payType;
    @BindView(R.id.tv_orderhj)
    TextView tv_orderhj;
    @BindView(R.id.tv_paydk)
    TextView tv_paydk;
    @BindView(R.id.tv_payMoney)
    TextView tv_payMoney;


    String phone;
    String order_Account;
    double Account_price;
    private LoadingDialog loadingDialog;
    List<Map<String, Object>> list;

    @Override
    protected int getViewResId() {
        return R.layout.activity_getorder;
    }

    @Override
    protected void init() {
        phone = SharedPreferencesUtil.getUserId(this);
        order_Account = getIntent().getStringExtra("order_Account");
        ActionBarUtil.initActionBar(getSupportActionBar(), "订单详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        loadingDialog = LoadingDialog.newInstance("加载中");
        loadingDialog.show(getFragmentManager(), "loading");
    }

    @Override
    protected void loadData() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", phone);
        builder.add("order_Account", order_Account);
        OkHttpUtil.postJson(Constant.URL.appgetOrder, builder, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                dismissLoading();
//{"order":{"address":"河北省秦皇岛市北戴河区可垃圾咯啦咯啦咯","id":0,"time":"2017-07-04 17:21:27",
// page":0,"desc":" ","integral":3500,"user_name":"通宝账户","user_id":31,"pageSize":0,
// "twoRebate":140.0,"rebate":210.0,"update_time":"2017-07-04 17:21:27","totalAmount":7000.0,
// "paymentMethod":0,"order_Account":"8911366","order_state":0,"listCount":0,"pageCount":0,"recipientName":"阿胶浆","recipientPhone":"13656243325","accList":[{"id":0,"count":1,"cmdId":78,"cmdName":"千两花卷茶","cmdbrand":"云端黑茶","cmdClass":"云端黑茶","pictureURL":"/shoppingManagementSystem/CommodityUpload/14981861041531.jpg","twoRebate":0.0,"rebate":0.0,"order_Account":null,"commodity_price":7000.0,"sum_price":0.0}]}}
                try {
                    JSONObject object = new JSONObject(json).getJSONObject("order");
                    JSONArray array = object.getJSONArray("accList");
                    tv_orderTime.setText(object.getString("update_time"));

                    tv_orderbh.setText(object.getString("order_Account"));
                    list = new ArrayList<Map<String, Object>>();
                    Account_price = object.getDouble("totalAmount");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Map<String, Object> map = new HashMap<String, Object>();
//{"id":0,"count":1,"sum_price":0.0,"twoRebate":0.0,"rebate":0.0,"cmdbrand":"雅客","cmdName":"小明同学",
// "cmdId":59,"cmdClass":"食品部","pictureURL":"登录.jpg;设置.jpg;首页.jpg","commodity_price":6.0,
// "order_Account":null
//{"order":{"address":"河北省秦皇岛市北戴河区可垃圾咯啦咯啦咯","id":0,"time":"2017-07-03 16:23:38","page":0,"desc":"老板，快点发货哟.","recipientName":"阿胶浆","recipientPhone":"13656243325","pageSize":0,"twoRebate":9.0,"rebate":14.0,"integral":0,"user_name":"通宝账户","user_id":31,"order_Account":"8281324","order_state":0,"update_time":"2017-07-03 16:23:38","totalAmount":480.0,"paymentMethod":0,"listCount":0,"pageCount":0,"accList":[{"id":0,"count":1,"commodity_price":480.0,"twoRebate":0.0,"rebate":0.0,"cmdbrand":"云端黑茶","cmdName":"千两茶砣","cmdId":81,"cmdClass":"云端黑茶","pictureURL":"/shoppingManagementSystem/CommodityUpload/14981995080651.jpg","order_Account":null,"sum_price":0.0}]}}

                        map.put("cmdName", jsonObject.getString("cmdName"));
                        map.put("cmdClass", "品种：" + jsonObject.getString("cmdClass"));
                        map.put("commodity_price", "￥" + jsonObject.getString("commodity_price"));
                        map.put("pictureURL", Constant.URL.BaseImg + jsonObject.getString("pictureURL"));
                        map.put("count", "*" + jsonObject.getInt("count") + "");
                        map.put("cmdId", jsonObject.getInt("cmdId") + "");
                        list.add(map);
                    }
                    //(-1:全部 0：未支付；1：支付待发货；2：发货待收货；3：收货待评价;4：已评价;5:待退款;)
                    if (object.getInt("order_state") == 0) {
                        tv_orderType.setText("未支付");
                    } else if (object.getInt("order_state") == 1) {
                        tv_orderType.setText("待发货");
                    } else if (object.getInt("order_state") == 2) {
                        tv_orderType.setText("待收货");
                    } else if (object.getInt("order_state") == 3) {
                        tv_orderType.setText("已完成");
                    } else if (object.getInt("order_state") == 4) {
                        tv_orderType.setText("已完成");
                    } else if (object.getInt("order_state") == 5) {
                        tv_orderType.setText("待退款");
                    }
                    //0:支付宝;1：微信;2:余额
                    if (object.getInt("paymentMethod") == 0) {
                        tv_payType.setText("支付宝");
                    } else if (object.getInt("paymentMethod") == 1) {
                        tv_payType.setText("微信");
                    } else if (object.getInt("paymentMethod") == 2) {
                        tv_payType.setText("余额");
                    }

                    tv_orderMoney.setText((Account_price - object.getDouble("integral")) + "");
                    tv_name.setText(object.getString("recipientName"));
                    tv_phone.setText(object.getString("recipientPhone"));
                    tv_address.setText(object.getString("address"));

                    lv_order.setFocusable(false);
                    lv_order.setAdapter(new MyAdapter(list));
                    lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(GetOrderActivity.this, ProjectDetailActivity.class);
                            intent.putExtra("projectId",list.get(position).get("cmdId") + "");
                            startActivity(intent);
                        }
                    });


                    tv_orderly.setText("留言  " + object.getString("desc"));
                    tv_orderhj.setText("￥" + Account_price);
                    tv_paydk.setText("-￥" + object.getDouble("integral"));
                    tv_payMoney.setText((Account_price - object.getDouble("integral")) + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String url, String error) {
            }
        });
    }


    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


    class MyAdapter extends BaseAdapter {
        List<Map<String, Object>> mlist;

        public MyAdapter(List<Map<String, Object>> list) {
            mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = View.inflate(GetOrderActivity.this, R.layout.item_payment, null);
                viewHolder = new ViewHolder();
                viewHolder.sdv_payMentImg = (SimpleDraweeView) view.findViewById(R.id.sdv_payMentImg);
                viewHolder.tv_payMentTitle = (TextView) view.findViewById(R.id.tv_payMentTitle);
                viewHolder.tv_payMentCopies = (TextView) view.findViewById(R.id.tv_payMentCopies);
                viewHolder.tv_payMentMoney = (TextView) view.findViewById(R.id.tv_payMentMoney);
                viewHolder.tv_count = (TextView) view.findViewById(R.id.tv_count);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.sdv_payMentImg.setImageURI((String) mlist.get(position).get("pictureURL"));
            viewHolder.tv_payMentTitle.setText((String) mlist.get(position).get("cmdName"));
            viewHolder.tv_payMentCopies.setText((String) mlist.get(position).get("cmdClass"));
            viewHolder.tv_payMentMoney.setText((String) mlist.get(position).get("commodity_price"));
            viewHolder.tv_count.setText((String) mlist.get(position).get("count"));

            return view;
        }

        class ViewHolder {
            SimpleDraweeView sdv_payMentImg;
            TextView tv_payMentTitle;
            TextView tv_payMentCopies;
            TextView tv_payMentMoney;
            TextView tv_count;
        }
    }
}
