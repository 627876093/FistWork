package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.CollectionList;
import cn.com.zlct.diamondgo.model.GetUserBankCard;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.PhoneUtil;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class MyCollectionAdapter extends AbsRecyclerViewAdapter<CollectionList.DataEntity> {

    public MyCollectionAdapter(Context context) {
        super(context, R.layout.item_collection,R.layout.item_next_page_loading);

    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             CollectionList.DataEntity d, int position) {

        holder.bindSimpleDraweeView(R.id.sdv_collection, Constant.URL.BaseImg+d.getCommodityUrl())
                .bindTextView(R.id.tv_collectionName, d.getCommodity_name())
                .bindTextView(R.id.tv_collectionType, "品种： " + d.getSort_name())
                .bindTextView(R.id.tv_collectionMoney, "¥" + d.getCommodity_price());
    }
}