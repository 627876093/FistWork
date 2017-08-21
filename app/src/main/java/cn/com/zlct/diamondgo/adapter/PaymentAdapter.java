package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.CartListEntity;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * 填写订单订单内容
 * Created by Administrator on 2017/6/5 0005.
 */
public class PaymentAdapter  extends AbsRecyclerViewAdapter<CartListEntity.DataEntity> {

    private View.OnClickListener clickListener;

    public PaymentAdapter(Context context, View.OnClickListener clickListener) {
        super(context, R.layout.item_payment);
        this.clickListener = clickListener;
    }
    public PaymentAdapter(Context context) {
        super(context, R.layout.item_payment);
    }
    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, CartListEntity.DataEntity d, int position) {

        holder.bindTextView(R.id.tv_payMentTitle,d.getCommodity_name())
                .bindTextView(R.id.tv_payMentCopies,"品种："+d.getSort_name())
                .bindTextView(R.id.tv_payMentMoney,"¥"+d.getCommodity_price())
                .bindTextView(R.id.tv_count, "x" + d.getCount())
                .bindSimpleDraweeView(R.id.sdv_payMentImg, Constant.URL.BaseImg + getURL(d.getCommodityUrl()))
        ;

    }

    private String getURL(String s) {
        if(s!=null&&s.length()>0){
            if (s.contains(";")){
                return s.substring(0,s.indexOf(";"));
            }else {
                return s;
            }
        }else {
            return s;
        }

    }
}
