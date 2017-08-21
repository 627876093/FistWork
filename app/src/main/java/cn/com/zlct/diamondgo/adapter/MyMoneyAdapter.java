package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.view.View;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.model.GetWithdrawalList;
import cn.com.zlct.diamondgo.model.RebateList;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class MyMoneyAdapter extends AbsRecyclerViewAdapter<GetWithdrawalList.DataEntity> {
    private OnAdapterCallbackListener callbackListener;

    public MyMoneyAdapter(Context context, OnAdapterCallbackListener callbackListene) {
        super(context, R.layout.item_money, R.layout.item_next_page_loading);

    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             GetWithdrawalList.DataEntity d, int position) {
        switch (d.getType()) {
            case 0:
                String type = "提现中";//0提现中 1提现成功 2提现失败
                if (d.getStruts() == 1) {
                    type = "提现成功";
                }
                if (d.getStruts() == 2) {
                    type = "提现失败";
                }
                holder.bindTextView(R.id.tv_zfbName, d.getZfbName() + "")
                        .bindTextView(R.id.tv_money, "¥" + d.getMoney() + "")
                        .bindTextView(R.id.tv_zfbId, d.getZfbId() + "")
                        .bindTextView(R.id.tv_type, type + "");
                break;
            case 1://滑到底，加载下一页
                callbackListener.onCallback();
                break;
        }
    }
}