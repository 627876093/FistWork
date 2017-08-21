package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.RebateList;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class MyRebateAdapter extends AbsRecyclerViewAdapter<RebateList.DataEntity> {

    public MyRebateAdapter(Context context) {
        super(context, R.layout.item_rebate,R.layout.item_next_page_loading);

    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             RebateList.DataEntity d, int position) {

        holder.bindTextView(R.id.tv_name, d.getRebate_phone())
                .bindTextView(R.id.tv_phone, "¥" + d.getRebate());
        holder.getView(R.id.sdv_rebate).setVisibility(View.GONE);

    }
}