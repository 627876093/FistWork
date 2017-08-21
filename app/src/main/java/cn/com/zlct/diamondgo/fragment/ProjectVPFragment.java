package cn.com.zlct.diamondgo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.AbsBaseListAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.util.Constant;

/**
 * 商品详情 图片 页面
 * Created by Administrator on 2017/6/1 0001.
 */
public class ProjectVPFragment extends BaseFragment {

    @BindView(R.id.lv_projectvp)
    public ListView listView;
    @BindView(R.id.ll_empty)
    public LinearLayout llEmpty;
    @BindView(R.id.tv_emptyTips)
    public TextView tvEmpty;

    private ImgLVAdapters imgLVAdapter;
    private String[] balanceList;
    String imglist;
    String type;
    public static ProjectVPFragment newInstance(String s,String type) {
        ProjectVPFragment viewPagerFragment = new ProjectVPFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imglist", s);
        bundle.putString("type", type);
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @Override
    protected int getViewResId() {
        return R.layout.fragment_projectvp;
    }

    @Override
    protected void getData(Bundle arguments) {
        type = arguments.getString("type");
        imglist=arguments.getString("imglist");

        tvEmpty.setText("暂无商品详情");

    }

    @Override
    protected void loadData() {
        if (imglist.contains(";")) {
            balanceList = imglist.split(";");
        } else {
            balanceList = new String[]{imglist};
        }

        imgLVAdapter = new ImgLVAdapters(balanceList);
        listView.setAdapter(imgLVAdapter);
//        imgLVAdapter.setData(balanceList);
        if (balanceList.length > 0) {
            if (llEmpty.getVisibility() != View.GONE) {
                llEmpty.setVisibility(View.GONE);
            }
        } else {
            if (llEmpty.getVisibility() != View.VISIBLE) {
                llEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

//    class ImgLVAdapter extends AbsBaseListAdapter<String[]> {
//
//        public ImgLVAdapter(Context context) {
//            super(context, R.layout.item_simple, R.layout.item_next_page_loading);
//        }
//
//        @Override
//        public void bindData(ViewHolder holder, String[] d, int position) {
//            holder.bindSimpleDraweeView(R.id.simple, Constant.URL.BaseImg + d[position]);
//            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) holder.getView(R.id.simple);
//            simpleDraweeView.getLayoutParams().height = simpleDraweeView.getWidth() / 2;
//
//        }
//    }
    class ImgLVAdapters extends BaseAdapter {
        String[] strings;

        public ImgLVAdapters(String[] strings) {
            this.strings = strings;
        }

        @Override
        public int getCount() {
            return strings.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder viewHolder;
            if (convertView == null){
                view = View.inflate(getContext(),R.layout.item_simple,null);
                viewHolder = new ViewHolder();
                viewHolder.simple = (SimpleDraweeView) view.findViewById(R.id.simple);
                view.setTag(viewHolder);
            }else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //手动添加内容
            viewHolder.simple.setImageURI(strings[position]);

            return view;
        }
        class ViewHolder{
            SimpleDraweeView simple;
        }
    }

}
