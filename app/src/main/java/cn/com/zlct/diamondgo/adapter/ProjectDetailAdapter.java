package cn.com.zlct.diamondgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.ProjectDetailActivity;
import cn.com.zlct.diamondgo.base.AbsBaseAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.OnAdapterCallbackListener;
import cn.com.zlct.diamondgo.custom.GridViewInScrollView;
import cn.com.zlct.diamondgo.custom.ListViewInScrollView;
import cn.com.zlct.diamondgo.model.ProjectCommodityEntity;
import cn.com.zlct.diamondgo.model.ProjectDataEntity;
import cn.com.zlct.diamondgo.model.ProjectImgEntity;
import cn.com.zlct.diamondgo.model.SellingCommodityEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import okhttp3.FormBody;

/**
 * 商品详情的Adapter
 * Created by Administrator on 2017/6/2 0002.
 */
public class ProjectDetailAdapter extends AbsRecyclerViewAdapter<ProjectCommodityEntity> {

    private AdapterView.OnItemClickListener onItemClickListener;
    private OnAdapterCallbackListener callbackListener;
    private Context context;
    List<SellingCommodityEntity.DataEntity> newList;
    LinearLayout llfooter;

    public ProjectDetailAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener,
                                OnAdapterCallbackListener callbackListener) {
        super(context, R.layout.item_projectetail, R.layout.item_next_page_loading);
        this.onItemClickListener = onItemClickListener;
        this.callbackListener = callbackListener;
        this.context = context;
    }


    @Override
    public void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder,
                             ProjectCommodityEntity d, int position) {
//        LinearLayout layout1 = (LinearLayout) holder.getView(R.id.list1);
//        LinearLayout layout2 = (LinearLayout) holder.getView(R.id.list2);

        ListViewInScrollView inScrollView = (ListViewInScrollView) holder.getView(R.id.lv_list2);

        if (d.getType() == 0) {
//            layout1.setVisibility(View.VISIBLE);
//            layout2.setVisibility(View.GONE);
            String s = d.getImgList().getDesc_address();
//            holder.bindSimpleDraweeView(R.id.simple, Constant.URL.BaseImg + getURL(s, position));
            ImgLVAdapters imgLVAdapters = new ImgLVAdapters(getURL(s));
            inScrollView.setFocusable(false);
            inScrollView.setAdapter(imgLVAdapters);
            setListViewHeightBasedOnChildren(inScrollView);
            if (inScrollView.getFooterViewsCount() != 0){
                if (llfooter!=null){
                    inScrollView.removeFooterView(llfooter);
                }
            }
        } else if (d.getType() == 1) {
            List<Map<String, String>> list = new ArrayList<>();

//            ,"返利金额","二级返利"
            String[] strings = {"商品价格", "商品参数",
                    "商品型号", "货号", "规格类型", "商品颜色",
                    "生产日期", "保质期", "净含量", "生产许可证编号",
                    "批准文号", "条码编号", "厂商名称", "厂商地址"};
            ProjectImgEntity imgEntity = d.getImgList();
            Log.i("aaa", "length:  " + strings.length + "");
            for (int i = 0; i < strings.length; i++) {
                Map<String, String> map = new HashMap<>();
                map.put("name", strings[i] + ":");

                map.put("text", "");
                switch (i) {
                    case 0:
                        map.put("text", imgEntity.getCommodity_price());
                        break;
                    case 1:
                        map.put("text", imgEntity.getCommodity_parameter());
                        break;
//                    case 2:
//                        map.put("text",imgEntity.getRebate());
//                        break;
//                    case 3:
//                        map.put("text",imgEntity.getTwoRebate());
//                        break;
                    case 2:
                        map.put("text", imgEntity.getModelCode());
                        break;
                    case 3:
                        map.put("text", imgEntity.getGoodsCode());
                        break;
                    case 4:
                        map.put("text", imgEntity.getNormsType());
                        break;
                    case 5:
                        map.put("text", imgEntity.getColor());
                        break;
                    case 6:
                        map.put("text", imgEntity.getProduceDate());
                        break;
                    case 7:
                        map.put("text", imgEntity.getQuality());
                        break;
                    case 8:
                        map.put("text", imgEntity.getNetWeight());
                        break;
                    case 9:
                        map.put("text", imgEntity.getGenerateCode());
                        break;
                    case 10:
                        map.put("text", imgEntity.getApprovalCode());
                        break;
                    case 11:
                        map.put("text", imgEntity.getBarCode());
                        break;
                    case 12:
                        map.put("text", imgEntity.getManufacturerName());
                        break;
                    case 13:
                        map.put("text", imgEntity.getManufacturerAddress());
                        break;
                }
                list.add(map);
            }

            inScrollView.setFocusable(false);
            inScrollView.setAdapter(new ProjectNormsdapter(list));

            if (inScrollView.getFooterViewsCount() == 0) {
                llfooter = (LinearLayout) LayoutInflater.from(context)
                        .inflate(R.layout.item_you, null, false);
                initDataForFooter(llfooter, imgEntity.getSort_id());
                inScrollView.addFooterView(llfooter);
            }
            setListViewHeightBasedOnChildren(inScrollView);

        } else if (d.getType() == 2) {

            if (inScrollView.getFooterViewsCount() != 0){
                if (llfooter!=null){
                    inScrollView.removeFooterView(llfooter);
                }
            }

//            layout1.setVisibility(View.GONE);
//            layout2.setVisibility(View.VISIBLE);
//            ProjectCommodityEntity.DataEntity dataEntity = d.getList().get(position);
            List<ProjectDataEntity.DataEntity> dataEntityList = new ArrayList<>();
            dataEntityList = d.getDataList().getList();
            ProjectList_ListAdapter listAdapter = new ProjectList_ListAdapter(context, onItemClickListener, callbackListener);
            inScrollView.setFocusable(false);
            inScrollView.setAdapter(listAdapter);
            listAdapter.setData(dataEntityList);
            setListViewHeightBasedOnChildren(inScrollView);
//            //获得list高度
//            int totalHeight = 0;
//            for (int i = 0; i < listAdapter.getCount(); i++) {
//                View listItem = listAdapter.getView(i, null, inScrollView);
//                listItem.measure(0, 0);
//                totalHeight += listItem.getMeasuredHeight();
//            }
//            ViewGroup.LayoutParams params = inScrollView.getLayoutParams();
//            params.height = totalHeight + (inScrollView.getDividerHeight() * (listAdapter.getCount() - 1));
//            inScrollView.setLayoutParams(params);

//            switch (dataEntity.getType()) {
//                case 0:
//                    holder.bindSimpleDraweeView(R.id.sdv_cartItemImg, Constant.URL.BaseImg + getURL(dataEntity.getHead_portrait(), position))
//                            .bindTextView(R.id.tv_name, dataEntity.getNickname())
//                            .bindTextView(R.id.tv_text, dataEntity.getComment_content())
//                            .bindTextView(R.id.tv_color, dataEntity.getComment_time());
//
//                    ImageView iv = (ImageView) holder.getView(R.id.iv_img);
//                    if (dataEntity.getUserVIP().equals("1")) {
//                        iv.setImageResource(R.drawable.vip1);
//                    } else if (dataEntity.getUserVIP().equals("2")) {
//                        iv.setImageResource(R.drawable.vip2);
//                    } else if (dataEntity.getUserVIP().equals("3")) {
//                        iv.setImageResource(R.drawable.vip3);
//                    } else if (dataEntity.getUserVIP().equals("4")) {
//                        iv.setImageResource(R.drawable.vip4);
//                    } else if (dataEntity.getUserVIP().equals("5")) {
//                        iv.setImageResource(R.drawable.vip5);
//                    } else {
//                        iv.setVisibility(View.VISIBLE);
//                    }
//                    GridViewInScrollView gridView= (GridViewInScrollView) holder.getView(R.id.gv_sunImg);
//                    gridView.setVisibility(View.GONE);
//                    gridView.setFocusable(false);
//                    String path=dataEntity.getPicture_path()+"";
//                    if (path.length()>0){
//                        gridView.setVisibility(View.VISIBLE);
//                        List<String> list=new ArrayList<>();
//                        if (path.contains(";")) {
//                            String [] s=path.split(";");
//                            for (int i=0;i<s.length;i++){
//                                list.add(s[i]);
//                            }
//                        } else {
//                            list.add(path);
//                        }
//                        gridView.setAdapter(new ImgGVAdapter(context, list));
//                        gridView.setClickable(false);
//                        gridView.setPressed(false);
//                        gridView.setEnabled(false);
//                    }
//                    break;
//                case 1://滑到底，加载下一页
//                    callbackListener.onCallback();
//                    break;
//            }
        }
    }

    private void initDataForFooter(LinearLayout llfooter, String s) {
        RecyclerView rv_youLove = (RecyclerView) llfooter.findViewById(R.id.rv_youLove);

        final GridLayoutManager manager = new GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false);
        rv_youLove.setLayoutManager(manager);
        final NewRVAdapter newRVAdapter = new NewRVAdapter(context, 1);
        rv_youLove.setAdapter(newRVAdapter);


        FormBody.Builder builder = new FormBody.Builder();
        builder.add("page", "1");
        builder.add("pageSize", "3");
        builder.add("commodity_name", "");
        builder.add("sort_id", s);
        builder.add("brand_id", "");
        builder.add("xlkind", "2");
        OkHttpUtil.postJson(Constant.URL.appgetCommodity, builder, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                SellingCommodityEntity projectListEntity = new Gson().fromJson(json, SellingCommodityEntity.class);

                newList = new ArrayList<>();
                newList.addAll(projectListEntity.getList());
                //新品专区
                newRVAdapter.setData(newList);
            }

            @Override
            public void onFailure(String url, String error) {

            }
        });


        newRVAdapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //进入商品详情页
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                intent.putExtra("projectId", newList.get(position).getId() + "");
                context.startActivity(intent);
            }
        });
    }

    /**
     * 控制list高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private String[] getURL(String s) {
        try {
            if (s != null && s.length() > 0) {
                if (s.contains(";")) {
                    return s.split(";");
                } else {
                    return new String[]{s};
                }
            } else {
                return new String[]{s};
            }
        } catch (Exception e) {
            return new String[]{""};
        }
    }

    private String getURL(String s, int p) {
        try {
            if (s != null && s.length() > 0) {
                if (s.contains(";")) {
                    return s.split(";")[p];
                } else {
                    return s;
                }
            } else {
                return s;
            }
        } catch (Exception e) {
            return "";
        }

    }

    class ProjectNormsdapter extends BaseAdapter {

        List<Map<String, String>> mlist;

        public ProjectNormsdapter(List<Map<String, String>> list) {
            this.mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
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
            if (convertView == null) {
                view = View.inflate(context, R.layout.item_nprms, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.tv_text = (TextView) view.findViewById(R.id.tv_text);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //手动添加内容
            viewHolder.tv_name.setText(mlist.get(position).get("name"));
            viewHolder.tv_text.setText(mlist.get(position).get("text"));

            return view;

        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_text;
        }
    }

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
            if (convertView == null) {
                view = View.inflate(context, R.layout.item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.simple = (SimpleDraweeView) view.findViewById(R.id.simple);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //手动添加内容
            viewHolder.simple.setImageURI(Constant.URL.BaseImg + strings[position]);
            viewHolder.simple.getLayoutParams().height = PhoneUtil.getPhoneWidth(context) / 2;
            return view;
        }

        class ViewHolder {
            SimpleDraweeView simple;
        }
    }

    class ProjectList_ListAdapter extends AbsBaseAdapter<ProjectDataEntity.DataEntity> {
        private AdapterView.OnItemClickListener clickListener;
        private OnAdapterCallbackListener callbackListener;

        public ProjectList_ListAdapter(Context context, AdapterView.OnItemClickListener clickListener,
                                       OnAdapterCallbackListener callbackListener) {
            super(context, R.layout.item_projectail_list, R.layout.item_next_page_loading);
            this.clickListener = clickListener;
            this.callbackListener = callbackListener;
        }

        @Override
        public void onBindHolder(ViewHolder holder, ProjectDataEntity.DataEntity dataEntity, int position) {
            switch (dataEntity.getType()) {
                case 0:
                    holder.bindSimpleDraweeView(R.id.sdv_cartItemImg, Constant.URL.BaseImg + getURL(dataEntity.getHead_portrait(), position))
                            .bindTextView(R.id.tv_name, dataEntity.getNickname())
                            .bindTextView(R.id.tv_text, dataEntity.getComment_content())
                            .bindTextView(R.id.tv_color, dataEntity.getComment_time());

                    ImageView iv = (ImageView) holder.getView(R.id.iv_img);
                    if (dataEntity.getUserVIP().equals("1")) {
                        iv.setImageResource(R.drawable.vip1);
                    } else if (dataEntity.getUserVIP().equals("2")) {
                        iv.setImageResource(R.drawable.vip2);
                    } else if (dataEntity.getUserVIP().equals("3")) {
                        iv.setImageResource(R.drawable.vip3);
                    } else if (dataEntity.getUserVIP().equals("4")) {
                        iv.setImageResource(R.drawable.vip4);
                    } else if (dataEntity.getUserVIP().equals("5")) {
                        iv.setImageResource(R.drawable.vip5);
                    } else {
                        iv.setVisibility(View.VISIBLE);
                    }
                    GridViewInScrollView gridView = (GridViewInScrollView) holder.getView(R.id.gv_sunImg);
                    gridView.setVisibility(View.GONE);
                    gridView.setFocusable(false);
                    String path = dataEntity.getPicture_path() + "";
                    if (path.length() > 0) {
                        gridView.setVisibility(View.VISIBLE);
                        List<String> list = new ArrayList<>();
                        if (path.contains(";")) {
                            String[] s = path.split(";");
                            for (int i = 0; i < s.length; i++) {
                                list.add(s[i]);
                            }
                        } else {
                            list.add(path);
                        }
                        gridView.setAdapter(new ImgGVAdapter(context, list));
                        gridView.setClickable(false);
                        gridView.setPressed(false);
                        gridView.setEnabled(false);
                        gridView.setOnItemClickListener(clickListener);

                    }
                    break;
                case 1://滑到底，加载下一页
                    callbackListener.onCallback();
                    break;
            }
        }


    }
}
