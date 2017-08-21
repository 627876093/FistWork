package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseAdapter;
import cn.com.zlct.diamondgo.model.ClassInfoEntity;
import cn.com.zlct.diamondgo.model.PushInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * Created by Administrator on 2017/5/27 0027.
 */
public class ClassLVAdapter extends AbsBaseAdapter<ClassInfoEntity.DataEntity> {

    public ClassLVAdapter(Context context) {
        super(context, R.layout.item_class);
    }

    @Override
    public void onBindHolder(ViewHolder holder, ClassInfoEntity.DataEntity d, int position) {
        holder.bindTextView(R.id.class_tv, d.getClassName());
    }
}