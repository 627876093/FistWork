package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseAdapter;
import cn.com.zlct.diamondgo.model.LocalEntity;
import cn.com.zlct.diamondgo.model.PushInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * Created by Administrator on 2017/5/27 0027.
 */
public class ZoneLVAdapter extends AbsBaseAdapter<PushInfoEntity.DataEntity> {

    public ZoneLVAdapter(Context context) {
        super(context, R.layout.item_zone);
    }

    @Override
    public void onBindHolder(ViewHolder holder, PushInfoEntity.DataEntity d, int position) {
        holder.bindTextView(R.id.zone_tv, d.getPush_title())
                .bindSimpleDraweeView(R.id.zone_sdv, Constant.URL.BaseImg+d.getPicture_url());

        TextView s= (TextView) holder.getView(R.id.zone_tv);
        switch (d.getPush_color()){
            case 0:
                holder.bindImageView(R.id.zone_iv, R.drawable.icon_zone0);
                s.setTextColor(Color.rgb(95,187,225));
                break;
            case 1:
                holder.bindImageView(R.id.zone_iv, R.drawable.icon_zone1);
                s.setTextColor(Color.rgb(247,169,69));
                break;
            case 2:
                holder.bindImageView(R.id.zone_iv, R.drawable.icon_zone2);
                s.setTextColor(Color.rgb(237,85,75));
                break;
            case 3:
                holder.bindImageView(R.id.zone_iv, R.drawable.icon_zone3);
                s.setTextColor(Color.rgb(4,201,192));
                break;
            case 4:
                holder.bindImageView(R.id.zone_iv, R.drawable.icon_zone4);
                s.setTextColor(Color.rgb(2149,81,201));
                break;
        }
    }
}
