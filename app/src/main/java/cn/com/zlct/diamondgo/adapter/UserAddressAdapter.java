package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.model.GetAddressList;
import cn.com.zlct.diamondgo.model.GetUserBankCard;
import cn.com.zlct.diamondgo.util.PhoneUtil;

/**
 * Created by Administrator on 2017/5/19 0019.
 */
public class UserAddressAdapter extends AbsRecyclerViewAdapter<GetAddressList.DataEntity> {


    private View.OnClickListener clickListener;

    public UserAddressAdapter(Context context, View.OnClickListener clickListener) {
        super(context, R.layout.item_address,R.layout.item_next_page_loading);
        this.clickListener = clickListener;
    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             GetAddressList.DataEntity d, int position) {

        holder.bindTextView(R.id.tv_RealName, data.get(position).getRecipientName() + "  " + data.get(position).getRecipientPhone())
                .bindTextView(R.id.tv_Address, data.get(position).getAddress());

        RadioButton defaultAddress = (RadioButton) holder.getView(R.id.rb_defaultAddress);
        if ("0".equals(data.get(position).getIsdefault())) {
            defaultAddress.setSelected(false);
        } else {
            defaultAddress.setSelected(true);
        }
        defaultAddress.setTag(position);
        defaultAddress.setOnClickListener(clickListener);
        TextView edit = (TextView) holder.getView(R.id.tv_addressEdit);
        edit.setTag(position);
        edit.setOnClickListener(clickListener);
        ImageView delete = (ImageView) holder.getView(R.id.iv_addressDelete);
        delete.setTag(position);
        delete.setOnClickListener(clickListener);

        LinearLayout ll= (LinearLayout) holder.getView(R.id.ll_addressEdit);
        ll.setTag(position);
        ll.setOnClickListener(clickListener);

    }
}
