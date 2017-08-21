package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseAdapter;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class ImgGVAdapter extends AbsBaseAdapter<String> {

    public ImgGVAdapter(Context context, List<String> data) {
        super(context, data, R.layout.sdv_home);
    }

    @Override
    public void onBindHolder(ViewHolder holder, String d, int position) {
        SimpleDraweeView sdv = (SimpleDraweeView) holder.getView(R.id.sdv_homeAd);
        sdv.setAspectRatio(1);
        sdv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        sdv.setImageURI(Uri.parse(Constant.URL.BaseImg + d));
    }
}
