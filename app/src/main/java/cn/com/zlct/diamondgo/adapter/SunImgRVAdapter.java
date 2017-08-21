package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Map;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.util.BitMapUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class SunImgRVAdapter extends AbsRecyclerViewAdapter<Uri> {

    private View.OnClickListener listener;
    private List<Bitmap> alreadyUploadImg;

    public SunImgRVAdapter(Context context, List<Uri> data, View.OnClickListener listener) {
        super(context, data, R.layout.recyclerview_sun_img);
        this.listener = listener;
    }

    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, Uri d, int position) {
        ImageView iv = (ImageView) holder.getView(R.id.iv_sunImg);
        int width = (PhoneUtil.getPhoneWidth(context) - PhoneUtil.dp2px(context, 60)) / 4;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
        lp.topMargin = PhoneUtil.dp2px(context, 6);
        lp.rightMargin = PhoneUtil.dp2px(context, 6);
        iv.setLayoutParams(lp);
        if (position == data.size() - 1) {
            iv.setImageResource(R.drawable.camera_add);
        } else {
            Bitmap bitmap = BitMapUtil.Uri2Bitmap(context, d);
            iv.setImageBitmap(bitmap);
        }
        ImageButton ibDel = (ImageButton) holder.getView(R.id.ib_sunImgDel);
        ibDel.setTag(d);
        ibDel.setOnClickListener(listener);
//        ibDel.setVisibility(View.VISIBLE);
        if (alreadyUploadImg != null && !TextUtils.isEmpty(alreadyUploadImg.get(position).toString())) {
            ibDel.setVisibility(View.VISIBLE);
        } else {
            ibDel.setVisibility(View.GONE);
        }
    }

    public void upAlreadyUp(List<Bitmap> alreadyUploadImg){
        this.alreadyUploadImg = alreadyUploadImg;
    }
}
