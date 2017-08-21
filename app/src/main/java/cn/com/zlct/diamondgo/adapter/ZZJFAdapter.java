package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.model.ZZJFEntity;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class ZZJFAdapter extends AbsRecyclerViewAdapter<ZZJFEntity> {

    private View.OnClickListener clickListener;

    public ZZJFAdapter(Context context, View.OnClickListener clickListener) {
        super(context, R.layout.item_zzjd);
        this.clickListener = clickListener;
    }
    public ZZJFAdapter(Context context) {
        super(context, R.layout.item_zzjd);
    }
    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, ZZJFEntity d, int position) {
        holder.bindTextView(R.id.name, d.getClassName())
                .bindTextView(R.id.money, d.getMoney()+"");
//        TextView textView= (TextView) holder.getView(R.id.btn);
//        textView.setOnClickListener(clickListener);
    }
}