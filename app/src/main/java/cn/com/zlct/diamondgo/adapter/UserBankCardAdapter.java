package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.UserInfoActivity;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.GetUserBankCard;
import cn.com.zlct.diamondgo.model.LocalEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.PhoneUtil;

/**
 * Created by Administrator on 2017/5/19 0019.
 */
public class UserBankCardAdapter extends AbsRecyclerViewAdapter<GetUserBankCard.DataEntity> {


    private View.OnClickListener clickListener;

    public UserBankCardAdapter(Context context
            , View.OnClickListener clickListener) {
        super(context, R.layout.item_bankcard,R.layout.item_next_page_loading);
        this.clickListener = clickListener;
    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             GetUserBankCard.DataEntity d, int position) {
        holder.bindTextView(R.id.bankName, d.getBankMine())
                .bindTextView(R.id.cardNumber, "**** **** **** " + PhoneUtil.getEndNum(d.getBankCardNumber(), 4));
        ImageView iv = (ImageView) holder.getView(R.id.iv);
        iv.setOnClickListener(clickListener);
        iv.setTag(position);

    }
}
