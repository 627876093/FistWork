package cn.com.zlct.diamondgo.base;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *ListView的多布局适配器封装
 * Created by Administrator on 2017/5/22 0022.
 */
public abstract class AbsBaseListAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> data;
    private int[] resId;//getItemViewType方法的返回值，即为resId数组的下标

    public AbsBaseListAdapter(Context context, int... resId){
        this.context = context;
        this.resId = resId;
        data = new ArrayList();
    }
    public void setData(T data){
        List<T> list=new ArrayList<>();
        list.add(data);
        this.data = list;
        this.notifyDataSetChanged();
    }
    public void setData(List<T> data){
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void addData(List<T> data){
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(resId[getItemViewType(position)], null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        //进行赋值的操作
        bindData(viewHolder, data.get(position), position);
        return convertView;
    }

    /**
     * 整条Item避免setTag()，如必须，需设置id
     */
    public abstract void bindData(ViewHolder holder, T d, int position);

    /**
     * 返回当前position所对应的Item的类型，必须从0开始，依次递增
     * 1、谁继承谁实现
     * 2、前提条件 -- 强制要求数据源中有一个type名称的属性，通过反射得到该属性
     */
    @Override
    public int getItemViewType(int position) {
        T t = data.get(position);
        Class cl = t.getClass();
        try {
            Field field = cl.getDeclaredField("type");
            field.setAccessible(true);//开始私有属性的使用权限
            return field.getInt(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return resId.length;
    }

    protected class ViewHolder{
        /**
         * 该Map用于缓存布局中的控件
         */
        Map<Integer, View> cacheMap = new HashMap();
        View layoutView;

        /**
         * 该构造参数 需要传递一个布局对象过来
         */
        public ViewHolder(View layoutView){
            this.layoutView = layoutView;
        }

        /**
         * 通过控件ID 找到该控件
         */
        public View getView(int id){
            if(cacheMap.containsKey(id)){
                return cacheMap.get(id);
            } else {
                View view = layoutView.findViewById(id);
                cacheMap.put(id, view);
                return view;
            }
        }

        /**
         * 绑定TextView
         */
        public ViewHolder bindTextView(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(text);
            return this;
        }

        /**
         * 绑定TextView 含html标签
         */
        public ViewHolder bindTextViewWithHtml(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(Html.fromHtml(text));
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public ViewHolder bindSimpleDraweeView(int id, String url){
            return bindSimpleDraweeView(id, Uri.parse(url));
        }

        /**
         * 绑定SimpleDraweeView
         */
        public ViewHolder bindSimpleDraweeView(int id, Uri uri){
            SimpleDraweeView sdv = (SimpleDraweeView) getView(id);
            sdv.setImageURI(uri);
            return this;
        }
    }
}
