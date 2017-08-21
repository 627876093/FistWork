package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseAdapter;
import cn.com.zlct.diamondgo.model.LocalEntity;

/**
 * 个人中心 ListView适配器
 */
public class MineLVAdapter extends AbsBaseAdapter<LocalEntity> {

    public MineLVAdapter(Context context, List<LocalEntity> data) {
        super(context, data, R.layout.item_mine);
    }

    @Override
    public void onBindHolder(ViewHolder holder, LocalEntity d, int position) {
        boolean isBorder = position == 2 || position == 6 || position == 7;
        holder.bindImageView(R.id.iv_mineItemIcon, d.getImgId())
                .bindTextView(R.id.tv_mineItemName, d.getName())
                .bindTextView(R.id.tv_text, "")
                .setViewVisible(R.id.v_mineItemLineThin, isBorder ? View.GONE : View.VISIBLE)
                .setViewVisible(R.id.v_mineItemLineBorder, isBorder ? View.VISIBLE : View.GONE);
        holder.getView(R.id.iv_mineItemArrow).setVisibility(View.VISIBLE);

//        客服热线：400-0355-189                                客服微信：zzgjjt8
        if (position == 6) {
            holder.getView(R.id.iv_mineItemArrow).setVisibility(View.GONE);
            holder.bindTextView(R.id.tv_text, "400-0355-189");
        }
        if (position == 7) {
            holder.getView(R.id.iv_mineItemArrow).setVisibility(View.GONE);
            holder.bindTextView(R.id.tv_text, "zzgjjt8");
        }
    }
}
