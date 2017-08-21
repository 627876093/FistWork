package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.model.SellingCommodityEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.PhoneUtil;

/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class NewRVAdapter extends AbsRecyclerViewAdapter<SellingCommodityEntity.DataEntity> {
    int type=0;
    public NewRVAdapter(Context context) {
        super(context, R.layout.sdv_homes);
    }
    public NewRVAdapter(Context context,int i) {
        super(context, R.layout.sdv_homes);
        type=i;
    }
    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             SellingCommodityEntity.DataEntity d, int position) {
        LinearLayout layout= (LinearLayout) holder.getView(R.id.ll_homeAd);
        layout.getLayoutParams().width= PhoneUtil.getPhoneWidth(context)/3;

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) holder.getView(R.id.sdv_homeAd);
        simpleDraweeView.setImageURI(Uri.parse(Constant.URL.BaseImg + getURL(d.getHome_address())));

        TextView textView= (TextView) holder.getView(R.id.name);
        textView.setVisibility(View.GONE);

        if (type==1){
            textView.setText(d.getCommodity_name());
            textView.setVisibility(View.VISIBLE);
        }
    }

    private String getURL(String s) {
        if (s != null && s.length() > 0) {
            if (s.contains(";")) {
                return s.substring(0, s.indexOf(";"));
            } else {
                return s;
            }
        } else {
            return s;
        }
    }
}
