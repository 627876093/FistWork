package cn.com.zlct.diamondgo.adapter;

import android.content.Context;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.CollectionList;
import cn.com.zlct.diamondgo.model.GetRecomList;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class MyCircleAdapter extends AbsRecyclerViewAdapter<GetRecomList.DataEntity> {

    public MyCircleAdapter(Context context) {
        super(context, R.layout.item_recom,R.layout.item_next_page_loading);

    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             GetRecomList.DataEntity d, int position) {

        holder.bindSimpleDraweeView(R.id.sdv_recom, Constant.URL.BaseImg+d.getHeadURL())
                .bindTextView(R.id.tv_name, d.getNickname())
                .bindTextView(R.id.tv_phone,  d.getPhone());
    }
}