package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.util.List;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseAdapter;
import cn.com.zlct.diamondgo.base.AbsBaseListAdapter;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.custom.ListViewInScrollView;
import cn.com.zlct.diamondgo.model.OrderList;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * “购买记录”页面ListView适配器
 */
public class OrderRecordLVAdapter extends AbsBaseListAdapter<OrderList.DataEntity> {

    private int mark;
    private View.OnClickListener listener;
    private OnAdapterCallbackListener callbackListener;

    public OrderRecordLVAdapter(Context context, int mark, View.OnClickListener listener, OnAdapterCallbackListener callbackListener) {
        super(context, R.layout.item_order_list, R.layout.item_next_page_loading);
        this.mark = mark;
        this.listener = listener;
        this.callbackListener = callbackListener;
    }

    @Override
    public void bindData(ViewHolder holder, OrderList.DataEntity d, final int position) {
        switch (d.getType()){
            case 0:
                 double Account_price=0;//合计价格

                for (int i=0;i<d.getAccList().size();i++){
                    Account_price+=d.getAccList().get(i).getCommodity_price()*d.getAccList().get(i).getCount();
                }

                     holder.bindTextView(R.id.order_tv_Account, "订单号:" + d.getOrder_Account())
                .bindTextView(R.id.order_tv_Price, "合计：¥" + (Account_price-d.getIntegral()));

//                TextView tv1= (TextView) holder.getView(R.id.order_tv_btn1);
//                TextView tv2= (TextView) holder.getView(R.id.order_tv_btn2);
//                tv1.setTag(position);
//                tv1.setOnClickListener(listener);
//                tv2.setTag(position);
//                tv2.setOnClickListener(listener);
//      0全部，1待付款，2待发货，3待评价，4售后
//      -1:全部 0：未支付；1：支付待发货；3：收货待评价；5:待退款
//      2：发货待收货;4：已评价;
                final ListViewInScrollView lv= (ListViewInScrollView) holder.getView(R.id.order_lv);

                lv.setVisibility(View.GONE);
                LinearLayout ll= (LinearLayout) holder.getView(R.id.order_lv_tk);
                ll.setVisibility(View.GONE);

                TextView order_tv_btn1= (TextView) holder.getView(R.id.order_tv_btn1);
                TextView order_tv_btn2= (TextView) holder.getView(R.id.order_tv_btn2);
                if (d.getOrder_state()>=5){
                    //售后
                    holder.bindTextView(R.id.order_tv_State, d.getTime());
                    lv.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                    holder.getView(R.id.ll_sh).setVisibility(View.GONE);

                    holder.bindSimpleDraweeView(R.id.order_lv_sdv,Constant.URL.BaseImg+d.getAccList().get(0).getPictureURL())
                            .bindTextView(R.id.order_tv_lv1, "退款编号："+d.getOrder_Account())
                            .bindTextView(R.id.order_tv_lv2, "退款金額：¥"+(Account_price-d.getIntegral()));
                    if(d.getOrder_state()==5){
                        holder.bindTextView(R.id.order_tv_lv3, "退款状态：待退款");
                    }else if (d.getOrder_state()==6){
                        holder.bindTextView(R.id.order_tv_lv3, "退款状态：退款中");
                    }else if (d.getOrder_state()==7){
                        holder.bindTextView(R.id.order_tv_lv3, "退款状态：退款成功");
                    }else if (d.getOrder_state()==8){
                        holder.bindTextView(R.id.order_tv_lv3, "退款状态：退款失败");
                    }

                }else {
                    ll.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    lv.setFocusable(false);
                    lv.setAdapter(new MyAdapter(d.getAccList()));
//                    (-1:全部 0：待付款；1：待发货；2：待收货；3:待评价；4：已评价 5:待退款 6:退款中 7:退款成功 8:退款失败)
                    if (d.getOrder_state()==0){
                        //待付款
                        holder.bindTextView(R.id.order_tv_State, "待付款");
                        order_tv_btn1.setText("删除订单");
                        order_tv_btn1.setVisibility(View.VISIBLE);

                        order_tv_btn2.setText("付款");
                        order_tv_btn2.setVisibility(View.VISIBLE);
                    }
                    if (d.getOrder_state()==1){
                        //待发货
                        holder.bindTextView(R.id.order_tv_State, "待发货");
                        order_tv_btn1.setText("提醒发货");
                        order_tv_btn1.setVisibility(View.GONE);

                        order_tv_btn2.setText("");
                        order_tv_btn2.setVisibility(View.GONE);
                    }
                    if (d.getOrder_state()==2){
                        //待收货
                        holder.bindTextView(R.id.order_tv_State, "待收货");
                        order_tv_btn2.setText("确认收货");
                        order_tv_btn2.setVisibility(View.VISIBLE);

                        order_tv_btn1.setText("查看物流");
                        order_tv_btn1.setVisibility(View.VISIBLE);
                    }
                    if (d.getOrder_state()==3){
                        //交易完成
                        holder.bindTextView(R.id.order_tv_State, "交易完成");
                        order_tv_btn1.setText("申请退款");
                        order_tv_btn1.setVisibility(View.VISIBLE);

                        order_tv_btn2.setText("评价");
                        order_tv_btn2.setVisibility(View.VISIBLE);

                    }
                    if (d.getOrder_state()==4){
                        //已评价
                        holder.bindTextView(R.id.order_tv_State, "已评价");
                        order_tv_btn2.setText("");
                        order_tv_btn2.setVisibility(View.GONE);
                    }
                }
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                        view.setTag(R.id.tag_order_btn1, position);
                        view.setTag(R.id.tag_order_btn2, "listview");
                        view.setOnClickListener(listener);
                    }
                });
                order_tv_btn1.setTag(R.id.tag_order_btn1, position);
                order_tv_btn1.setTag(R.id.tag_order_btn2, order_tv_btn1.getText().toString().trim() + "");
                order_tv_btn1.setOnClickListener(listener);

                order_tv_btn2.setTag(R.id.tag_order_btn1, position);
                order_tv_btn2.setTag(R.id.tag_order_btn2,order_tv_btn2.getText().toString().trim()+"");
                order_tv_btn2.setOnClickListener(listener);
                break;
            case 1://滑到底，加载下一页
                callbackListener.onCallback();
                break;
        }
    }


    class MyAdapter extends BaseAdapter{
        List<OrderList.DataEntity.ChildDataEntity> mlist;
        public MyAdapter(List<OrderList.DataEntity.ChildDataEntity> list){
            mlist=list;
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
                view = View.inflate(context, R.layout.item_order_list_list, null);
                viewHolder = new ViewHolder();
                viewHolder.sdv_list = (SimpleDraweeView) view.findViewById(R.id.sdv_list);
                viewHolder.tv_lv1 = (TextView) view.findViewById(R.id.tv_lv1);
                viewHolder.tv_lv2 = (TextView) view.findViewById(R.id.tv_lv2);
                viewHolder.tv_lv3 = (TextView) view.findViewById(R.id.tv_lv3);
                viewHolder.tv_lv_num= (TextView) view.findViewById(R.id.tv_lv_num);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.sdv_list.setImageURI(Constant.URL.BaseImg + mlist.get(position).getPictureURL());
            viewHolder.tv_lv1.setText(mlist.get(position).getCmdName());
            viewHolder.tv_lv2.setText("品种："+mlist.get(position).getCmdClass());
            viewHolder.tv_lv3.setText("￥"+mlist.get(position).getCommodity_price());

            viewHolder.tv_lv_num.setText("x" + mlist.get(position).getCount());
            return view;
        }

         class ViewHolder {
             SimpleDraweeView sdv_list;
            TextView tv_lv1;
            TextView tv_lv2;
             TextView tv_lv3;
             TextView tv_lv_num;
        }
    }
}
