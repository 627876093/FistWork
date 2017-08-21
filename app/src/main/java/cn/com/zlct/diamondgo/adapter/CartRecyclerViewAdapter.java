package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.CartListEntity;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * 购物车清单ListView 适配器
 */
public class CartRecyclerViewAdapter extends AbsRecyclerViewAdapter<CartListEntity.DataEntity> {

    private View.OnClickListener clickListener;

    public CartRecyclerViewAdapter(Context context, View.OnClickListener clickListener) {
        super(context, R.layout.item_cart_list);
        this.clickListener = clickListener;
    }
    public CartRecyclerViewAdapter(Context context) {
        super(context, R.layout.item_cart_list);
    }
    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, CartListEntity.DataEntity d, int position) {

        String shopCarId = data.get(position).getCommodityId();
        holder.bindTextView(R.id.tv_cartItemTitle,d.getCommodity_name())
        .bindTextView(R.id.tv_cartItemCopies,"品种："+d.getSort_name())
        .bindTextView(R.id.et_cartItemMoney,"¥"+d.getCommodity_price())
        .bindTextView(R.id.et_cartItemBy, "×" + d.getCount())
        .bindSimpleDraweeView(R.id.sdv_cartItemImg, Constant.URL.BaseImg + getURL(d.getCommodityUrl()))
        ;

        ImageView imageView = (ImageView) holder.getView(R.id.ib_cartItemCheck);
        imageView.setSelected(d.isChecked());
        imageView.setTag(shopCarId);
        imageView.setOnClickListener(clickListener);

        LinearLayout ll_delete= (LinearLayout) holder.getView(R.id.ll_delete);
        ll_delete.setTag(shopCarId);
        ll_delete.setOnClickListener(clickListener);

        LinearLayout ll_cartItemMoney= (LinearLayout) holder.getView(R.id.ll_cartItemMoney);
        LinearLayout ll_cartItemNum= (LinearLayout) holder.getView(R.id.ll_cartItemNum);

        if (!d.isbj()){
            ll_cartItemMoney.setVisibility(View.VISIBLE);
            ll_cartItemNum.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
        }else {
            ll_cartItemMoney.setVisibility(View.GONE);
            ll_cartItemNum.setVisibility(View.VISIBLE);
            ll_delete.setVisibility(View.VISIBLE);
        }

        ImageButton btnItemMinus = (ImageButton) holder.getView(R.id.btn_cartMinus);//-
        ImageButton btnItemPlus = (ImageButton) holder.getView(R.id.btn_cartPlus);//+
        TextView tvItemNum = (TextView) holder.getView(R.id.et_cartItemNum);

        btnItemMinus.setTag(shopCarId);
        btnItemMinus.setTag(R.id.tag_relation, tvItemNum);
        btnItemMinus.setOnClickListener(clickListener);

        btnItemPlus.setTag(shopCarId);
        btnItemPlus.setTag(R.id.tag_relation, tvItemNum);
        btnItemPlus.setOnClickListener(clickListener);

        tvItemNum.setText(data.get(position).getCount());
        tvItemNum.setTag(shopCarId);
        tvItemNum.setOnClickListener(clickListener);


//        TextView tvItemSwipe = (TextView) holder.getView(R.id.tv_cartItemSwipe);
//        TextView tvItemTitle = (TextView) holder.getView(R.id.tv_cartItemTitle);
//
//        ImageView iv = (ImageView) holder.getView(R.id.iv_labelStateCart);
//        iv.setVisibility(View.VISIBLE);
//
//
        SimpleDraweeView sdvItemImg = (SimpleDraweeView) holder.getView(R.id.sdv_cartItemImg);
//        String shopCarId = data.get(position).getShopCarId();
//        tvItemTitle.setTag(shopCarId);
//        tvItemTitle.setOnClickListener(clickListener);
        sdvItemImg.setTag(shopCarId);
        sdvItemImg.setOnClickListener(clickListener);
//        btnItemDelete.setTag(shopCarId);
//        btnItemDelete.setOnClickListener(clickListener);
//
//

//
//        tvItemSwipe.setTag(shopCarId);
//        tvItemSwipe.setTag(R.id.tag_relation, tvItemNum);
//        tvItemSwipe.setOnClickListener(clickListener);
//        String titleNewHand;
//        if ("1".equals(data.get(position).getProjectIsNewhand())) {//体验
//            titleNewHand = "<font color=#12B7F5>[体验]</font>";
//        } else {
//            titleNewHand = "";
//        }
//        String titleRestri;
//        String restriInfo = "";
//        if ("1".equals(data.get(position).getProjectIsRestriction())) {//限购
//            titleRestri = "<font color=#DD2727>[限购]</font>";
//            int restriCount = Integer.parseInt(data.get(position).getProjectRestrictionCount());
//            restriInfo = "<font color=#DD2727>(限购:" + restriCount + ")</font>";
//        } else {
//            titleRestri = "";
//        }
//        int unit = (int) Float.parseFloat(data.get(position).getProjectUnitPrice());
//        holder.bindTextViewWithHtml(R.id.tv_cartItemTitle, titleNewHand + titleRestri + data.get(position).getProjectTitle())
//                .bindSimpleDraweeView(R.id.sdv_cartItemImg, Constant.URL.BaseImg + data.get(position).getMerchandiseFilePath())
//                .bindTextViewWithHtml(R.id.tv_cartItemCopies, "总需:" + data.get(position).getProjectCopies() + "             剩余:" +
//                        "<font color=#fb4c34>:" + data.get(position).getProjectOverCopies() + "</font>");
////    + " (单价:" + unit + ")" + restriInfo)
////            .bindTextView(R.id.et_cartItemNum, data.get(position).getBuyCopies() + ""
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
