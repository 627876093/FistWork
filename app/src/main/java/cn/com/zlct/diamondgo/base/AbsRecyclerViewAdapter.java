package cn.com.zlct.diamondgo.base;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecyclerView的多(单)布局 适配器
 * 可以实现ListView、GridView和瀑布流的效果
 *
 * 配置数据 -- 必须重写{@link #onBindHolder(RecyclerViewHolder, Object, int)}
 * 添加头部 -- {@link #addHeaderView(View)}
 *         -- 添加头部后，部分item刷新(如{@link #notifyItemRemoved(int)})时 不可直接使用原始数据源的下标
 * 多布局实现 -- 必须重写{@link #getItemType(T)}方法
 *           -- 强制要求数据源中有一个type名称的属性，重写时返回该值
 *           -- type非0时 当前item默认宽度撑满
 *           -- 如需不撑满 需重写{@link #isItemFullSpan(int)} 根据type的值返回是否撑满
 */
public abstract class AbsRecyclerViewAdapter<T> extends RecyclerView.Adapter<AbsRecyclerViewAdapter.RecyclerViewHolder>{

    protected Context context;
    protected List<T> data;
    private int[] resId;//多布局的布局文件

    public static final int TYPE_HEADER = -1;//第一条头部的类型
    private List<View> mHeaderViews = new ArrayList<>();

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public AbsRecyclerViewAdapter(Context context, int... resId) {
        this.context = context;
        this.resId = resId;
        data = new ArrayList<>();
    }

    public AbsRecyclerViewAdapter(Context context, List<T> data, int... resId) {
        this.context = context;
        this.resId = resId;
        this.data = data;
    }

    public void setData(List<T> data){
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void addData(List<T> data){
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public List<T> getData(){
        return data;
    }

    public void deleteItem(int position){
        this.data.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * Item点击监听 -- 添加头部后，头部的点击事件需自行处理
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    /**
     * Item长按监听 -- 添加头部后，头部的长按事件需自行处理
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return getHeaderCount();
        }
        return data.size() + getHeaderCount();
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("header is null");
        }
        mHeaderViews.add(header);
        this.notifyDataSetChanged();
    }

    public void clearHeader() {
        if (getHeaderCount() > 0) {
            mHeaderViews.clear();
            this.notifyDataSetChanged();
        }
    }

    /**
     * 因支持添加头部功能 该方法不能重写 也不能直接在外部调用
     * 使用多布局时 只能重写 {@link #getItemType(T)}
     */
    @Override
    public int getItemViewType(int position) {
        int count = getHeaderCount();
        if (count > 0 && position < count) {//头部的类型
            return TYPE_HEADER - position;
        }
        return getItemType(data.get(position - count));
    }

    /**
     * 当前position所对应的Item的类型
     * 默认Item的类型为0，其他Item的类型依次递增
     */
    public int getItemType(T d) {
        return super.getItemViewType(0);
    }

    /**
     * 当前position所对应的Item 是否宽度撑满 -- 默认非0撑满 (此方法不能重写)
     */
    private boolean isFullSpan(int position) {
        int type = getItemViewType(position);
        if (type == 0) {//默认Item
            return false;
        } else if (type < 0) {//头部
            return true;
        } else {//其他类型
            return isItemFullSpan(type);
        }
    }

    /**
     * 多布局时 类型为type的item 是否宽度撑满 -- 默认非0撑满
     */
    protected boolean isItemFullSpan(int type) {
        return true;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType <= TYPE_HEADER) {
            return new RecyclerViewHolder(mHeaderViews.get(-1 - viewType));
        }
        View view = LayoutInflater.from(context).inflate(resId[viewType], parent, false);
        return new RecyclerViewHolder(view);
    }

    /**
     * 因支持添加头部功能 该方法不能重写 只能重写 {@link #onBindHolder(RecyclerViewHolder, Object, int)}
     */
    @Override
    public void onBindViewHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        if (getItemViewType(position) <= TYPE_HEADER) {
            return;
        }
        onBindHolder(holder, data.get(position - getHeaderCount()), position - getHeaderCount());
    }

    /**
     * 配置数据 -- holder的类型必须是 AbsRecyclerViewAdapter.RecyclerViewHolder
     * 如需给整条Item setTag()，请调用 {@link RecyclerViewHolder#setItemTag(int, Object)}
     */
    public abstract void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, T d, int position);

    /**
     * GridLayoutManager时 设置type不为0的item 占整行位置
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isFullSpan(position) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * StaggeredGridLayoutManager时 设置type不为0的item 占整行位置
     */
    @Override
    public void onViewAttachedToWindow(AbsRecyclerViewAdapter.RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null) {
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(holder.getParent().isFullSpan(holder.getLayoutPosition()));
            } else if (!(lp instanceof GridLayoutManager.LayoutParams)) {
                /**
                 * LinearLayoutManager时 头部item撑满
                 */
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) lp;
                layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
                layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                holder.itemView.setLayoutParams(layoutParams);
            }
        }
    }

    protected class RecyclerViewHolder extends RecyclerView.ViewHolder {

        Map<Integer, View> cacheMap = new HashMap();
        View layoutView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            if(onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = RecyclerViewHolder.this.getLayoutPosition();
                        if (position >= getHeaderCount()) {
                            onItemClickListener.onItemClick(v, position - getHeaderCount());
                        }
                    }
                });
            }
            if (onItemLongClickListener != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = RecyclerViewHolder.this.getLayoutPosition();
                        if (position >= getHeaderCount()) {
                            return onItemLongClickListener.onItemLongClick(v, position - getHeaderCount());
                        }
                        return true;
                    }
                });
            }
            this.layoutView = itemView;
        }

        private AbsRecyclerViewAdapter getParent() {
            return AbsRecyclerViewAdapter.this;
        }

        public View getView(int id){
            if(cacheMap.containsKey(id)){
                return cacheMap.get(id);
            } else {
                View view = layoutView.findViewById(id);
                cacheMap.put(id, view);
                return view;
            }
        }

        public RecyclerViewHolder setViewLayoutParams(int width, int height){
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) layoutView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            layoutView.setLayoutParams(layoutParams);
            return this;
        }

        /**
         * 给整条item设置tag
         */
        public RecyclerViewHolder setItemTag(int id, Object tag) {
            layoutView.setTag(id, tag);
            return this;
        }

        /**
         * 绑定TextView
         */
        public RecyclerViewHolder bindTextView(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(text);
            return this;
        }

        /**
         * 绑定TextView 含html标签
         */
        public RecyclerViewHolder bindTextViewWithHtml(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(Html.fromHtml(text));
            return this;
        }

        /**
         * 绑定ImageView
         */
        public RecyclerViewHolder bindImageView(int id, int imgId){
            ImageView iv = (ImageView) getView(id);
            iv.setImageResource(imgId);
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerViewHolder bindSimpleDraweeView(int id, String url){
            bindSimpleDraweeView(id, Uri.parse(url));
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerViewHolder bindSimpleDraweeView(int id, Uri uri){
            SimpleDraweeView sdv = (SimpleDraweeView) getView(id);
            sdv.setImageURI(uri);
            return this;
        }

        /**
         * 设置进度条进度
         */
        public RecyclerViewHolder bindProgressBar(int id, int num){
            ProgressBar progressBar = (ProgressBar) getView(id);
            progressBar.setProgress(num);
            return this;
        }

        /**
         * 设置星星进度条
         */
        public RecyclerViewHolder bindRatingBar(int id, float rating){
            RatingBar ratingBar = (RatingBar) getView(id);
            ratingBar.setRating(rating);
            return this;
        }
    }
}
