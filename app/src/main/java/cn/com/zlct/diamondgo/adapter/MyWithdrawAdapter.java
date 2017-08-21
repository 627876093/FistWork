package cn.com.zlct.diamondgo.adapter;

import android.content.Context;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.GetRecomList;
import cn.com.zlct.diamondgo.model.GetWithdrawList;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class MyWithdrawAdapter extends AbsRecyclerViewAdapter<GetWithdrawList.DataEntity> {

    public MyWithdrawAdapter(Context context) {
        super(context, R.layout.item_withdraw,R.layout.item_next_page_loading);

    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             GetWithdrawList.DataEntity d, int position) {

        holder.bindTextView(R.id.tv_time, d.getDate())
                .bindTextView(R.id.tv_money, "-¥"+ d.getTxvalue());
    }
}