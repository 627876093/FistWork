package cn.com.zlct.diamondgo.adapter;

import android.content.Context;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseListAdapter;
import cn.com.zlct.diamondgo.model.GetIntegralList;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class IntegralLVAdapter extends AbsBaseListAdapter<GetIntegralList.DataEntity> {

    public IntegralLVAdapter(Context context) {
        super(context, R.layout.item_integral, R.layout.item_next_page_loading);
    }

    @Override
    public void bindData(ViewHolder holder, GetIntegralList.DataEntity d, int position) {
//
//                String money = " " + d.getDetailsMoney();
//                if ("0".equals(d.getDetailsPath())) {
//                    money = "+" + money;
//                } else {
//                    money = "-" + money;
//                }
                holder.bindTextView(R.id.tv_time, d.getCasename())
                        .bindTextView(R.id.tv_money, "-¥"+d.getCasevalue());


    }
}