package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.model.SellingCommodityEntity;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * 首页商品适配器
 * Created by Administrator on 2017/5/17 0017.
 */
public class AllGoodsRVAdapter extends AbsRecyclerViewAdapter<SellingCommodityEntity.DataEntity> {

    private View.OnClickListener clickListener;
    private OnAdapterCallbackListener callbackListener;

    /**
     * 携带加入购物车
     * @param context
     * @param clickListener
     * @param callbackListener
     */
    public AllGoodsRVAdapter(Context context, View.OnClickListener clickListener,
                             OnAdapterCallbackListener callbackListener) {
        super(context, R.layout.recyclerview_all_goods, R.layout.item_next_page_loading);
        this.clickListener = clickListener;
        this.callbackListener = callbackListener;
    }

    /**
     * 不携带加入购物车
     * @param context
     * @param callbackListener
     */
    public AllGoodsRVAdapter(Context context,OnAdapterCallbackListener callbackListener) {
        super(context, R.layout.recyclerview_all_goods, R.layout.item_next_page_loading);
        this.callbackListener = callbackListener;
    }


    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             SellingCommodityEntity.DataEntity d, int position) {
        switch (d.getType()) {
            case 0:
                int speed = (int) Double.parseDouble(d.getCommodity_price());
                holder.setItemTag(R.id.tag_id, d.getId())
                        .bindSimpleDraweeView(R.id.all_homeSinglesun, Uri.parse(Constant.URL.BaseImg + getURL(d.getHome_address())))
                        .bindTextView(R.id.all_tittle, d.getCommodity_name())
                        .bindTextView(R.id.all_money, "¥"+speed );
//                ImageView addBtn = (ImageView) holder.getView(R.id.tv_allAddCart);
//                addBtn.setTag(d.getProjectId());
//                addBtn.setTag(R.id.tag_relation, holder.getView(R.id.sdv_projectImg));
//                addBtn.setTag(R.id.tag_url, d.getFilePath());
//                addBtn.setOnClickListener(clickListener);
                break;
            case 1://滑到底，加载下一页
                callbackListener.onCallback();
                break;
        }


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