package cn.com.zlct.diamondgo.base;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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

import cn.com.zlct.diamondgo.util.Constant;

/**
 * RecyclerView的多(单)布局 适配器
 * 可以实现ListView、GridView和瀑布流的效果
 *
 * 配置数据 -- 必须重写{@link #onBindHolder(RecyclerHolder, Object, int)}
 * 设置空数据页面
 *          -- {@link #setEmptyView(int)} 或 {@link #setEmptyView(View)}
 *          -- 当头部和数据源都为空时 显示EmptyView
 *          -- 如需只有当数据源为空时 显示EmptyView
 *                  重写 {@link #getEmptyCount()} 调用 {@link #getEmptyCountOfData()}
 *                  此时需重写 {@link #setEmptyLayoutParams()} 调用 {@link #setEmptyHeight(int)}
 *          -- 给EmptyView中的{@link TextView}设置文字 调用 {@link #setEmptyTips(int, String)}
 *          -- 给EmptyView中的{@link View}设置点击事件 调用 {@link #setEmptyClickListener(int, View.OnClickListener)}
 * 添加头部 -- {@link #addHeaderView(View)} -- 最多添加98条头部
 *         -- 添加头部后，部分item刷新(如{@link #notifyItemChanged(int)})时 不可直接使用原始数据源的下标
 *         -- 因父类方法为final，不可重写，添加头部时可调用自定义方法{@link #notifyItemChange2(int)}
 * 添加尾部 -- {@link #addFooterView(View)}
 *      添加头部或尾部后 其点击事件需自行处理
 * 多布局实现 -- 必须重写{@link #getItemType(T)}方法 重写时返回type类型 item的类型从0开始 依次递增
 *           -- type非0时 当前item默认宽度撑满
 *           -- 如需不撑满 需重写{@link #isItemFullSpan(int)} 根据type的值返回是否撑满
 * 点击事件 -- 给item设置 调用 {@link #setOnItemClickListener(OnItemClickListener)}
 *         -- 给item中View设置 前提: 调用 {@link #setOnItemClickListener(OnItemClickListener)}
 *              Adapter中调用 {@link RecyclerHolder#setOnClickListener(int)}
 *                      或 {@link RecyclerHolder#setOnClickListenerAndTag(int, Object)}
 * 长按事件类似
 * 注意  -- 不可重写的方法 -- {@link #onBindViewHolder(RecyclerHolder, int)} -- 配置数据
 *                      替代的重写方法{@link #onBindHolder(RecyclerHolder, Object, int)}
 *      -- 不可重写的方法 -- {@link #getItemViewType(int)} -- 多布局时获取item的类型
 *                      替代的重写方法{@link #getItemType(T)}
 *      -- 不可重写的方法 -- {@link #isFullSpan(int)} -- 多布局时item是否撑满 默认非0撑满
 *                      替代的重写方法{@link #isItemFullSpan(int)}
 *      -- 不可重写的方法 -- {@link #getGridSpanSize(int, int)} -- 多布局 GridLayoutManager 非0不撑满时 item占用宽度
 *                      替代的重写方法{@link #getItemSpanSize(int)}
 *      -- 可选的方法 -- {@link #getEmptyCountOfData()} -- 空数据的数量（只考虑数据源的数量）
 *                      在重写{@link #getEmptyCount()}时调用
 *      -- 可选的方法 -- {@link #setEmptyHeight(int)} -- 设置空数据的高度（默认撑满）
 *                      在重写{@link #setEmptyLayoutParams()}时调用
 */
public abstract class AbsRecyclerAdapter<T> extends RecyclerView.Adapter<AbsRecyclerAdapter.RecyclerHolder> {

    protected Context context;
    protected List<T> data;
    private int[] resId;//多布局的布局文件

    public static final int TYPE_EMPTY = -1;//空数据的类型
    public static final int TYPE_HEADER = -2;//第一条头部的类型
    public static final int TYPE_FOOTER = -100;//第一条尾部的类型
    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();
    private View mEmpty;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public AbsRecyclerAdapter(Context context, int... resId) {
        this.context = context;
        this.resId = resId;
        data = new ArrayList<>();
    }

    public AbsRecyclerAdapter(Context context, List<T> data, int... resId) {
        this.context = context;
        this.resId = resId;
        this.data = data;
    }

    public void setData(List<T> data){
        this.data = data;
        setEmptyVisible();
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
     * Item点击监听 -- 添加头部、尾部后，点击事件需自行处理
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    /**
     * Item长按监听 -- 添加头部、尾部后，长按事件需自行处理
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return getDataCount() + getHeaderCount() + getFooterCount() + getEmptyCount();
    }

    public int getDataCount() {
        return data == null ? 0 : data.size();
    }

    public int getEmptyCount() {
        int size = getDataCount() + getHeaderCount() + getFooterCount();
        return mEmpty != null && size == 0 ? 1 : 0;
    }

    /**
     * 空数据提示的数量（是否显示空数据提示） -- 只考虑数据源的数量
     * 默认不调用 若需使用 请重写{@link #getEmptyCount()} 调用该方法
     */
    public int getEmptyCountOfData() {
        return mEmpty != null && getDataCount() == 0 ? 1 : 0;
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("Header is null");
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
     * 设置头部的LayoutParams 默认是(MATCH_PARENT, WRAP_CONTENT)
     * 如需加Margin, 或其他, 请重写该方法
     */
    public void setHeaderLayoutParams(View itemView, RecyclerView.LayoutParams lp) {
        itemView.setLayoutParams(lp);
    }

    public void addFooterView(View footer) {
        if (footer == null) {
            throw new RuntimeException("Footer is null");
        }
        mFooterViews.add(footer);
        this.notifyDataSetChanged();
    }

    public void clearFooter() {
        if (getFooterCount() > 0) {
            mFooterViews.clear();
            this.notifyDataSetChanged();
        }
    }

    public void setEmptyView(View empty) {
        if (empty == null) {
            throw new RuntimeException("Empty is null");
        }
        mEmpty = empty;
        mEmpty.setVisibility(View.GONE);
        setEmptyLayoutParams();
    }

    public void setEmptyLayoutParams() {
        mEmpty.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置空数据提示的高度 -- 仅供外部需要时调用
     * 默认不调用 若需使用 请重写{@link #setEmptyLayoutParams()} 调用该方法
     */
    public void setEmptyHeight(int height) {
        if (mEmpty != null && height > 0) {
            mEmpty.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height));
        }
    }

    public void setEmptyView(int resId) {
        setEmptyView(LayoutInflater.from(context).inflate(resId, null));
    }

    public void setEmptyVisible() {
        if (mEmpty == null) {
            return;
        }
        if (mEmpty.getVisibility() != View.VISIBLE) {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    public View getEmptyView() {
        return mEmpty;
    }

    /**
     * 修改空数据提示中的文字
     */
    public void setEmptyTips(int id, String tips) {
        if (mEmpty != null) {
            View v = mEmpty.findViewById(id);
            if (v != null && v instanceof TextView) {
                ((TextView) v).setText(tips);
            }
        }
    }

    /**
     * 空数据提示中给控件设置点击事件
     */
    public void setEmptyClickListener(int id, View.OnClickListener listener) {
        if (mEmpty != null) {
            View v = mEmpty.findViewById(id);
            if (v != null) {
                v.setVisibility(View.VISIBLE);
                v.setOnClickListener(listener);
            }
        }
    }

    public void notifyItemChange2(int position) {
        super.notifyItemChanged(position + getHeaderCount());
    }

    public void notifyItemRangeChange2(int positionStart, int itemCount) {
        super.notifyItemRangeChanged(positionStart + getHeaderCount(), itemCount);
    }

    public void notifyItemInsert2(int position) {
        super.notifyItemInserted(position + getHeaderCount());
    }

    public void notifyItemMove2(int fromPosition, int toPosition) {
        super.notifyItemMoved(fromPosition + getHeaderCount(), toPosition + getHeaderCount());
    }

    public void notifyItemRangeInsert2(int positionStart, int itemCount) {
        super.notifyItemRangeInserted(positionStart + getHeaderCount(), itemCount);
    }

    public void notifyItemRemove2(int position) {
        super.notifyItemRemoved(position + getHeaderCount());
    }

    public void notifyItemRangeRemove2(int positionStart, int itemCount) {
        super.notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount);
    }

    public int getResId(String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    public String getString(int resId) {
        return context.getString(resId);
    }

    public int getColor(int id) {
        return ContextCompat.getColor(context, id);
    }

    /**
     * 因支持添加头部功能 该方法不能重写 也不能直接在外部调用
     * 使用多布局时 只能重写 {@link #getItemType(T)}
     */
    @Override
    public int getItemViewType(int position) {
        int headCount = getHeaderCount();
        if (position < headCount) {//头部
            return TYPE_HEADER - position;
        }
        if (getEmptyCount() > 0) {//空数据
            return TYPE_EMPTY;
        }
        int nonFootCount = headCount + getDataCount();//头部和数据的数量
        if (position >= nonFootCount) {//尾部
            return TYPE_FOOTER + nonFootCount - position;
        }
        return getItemType(data.get(position - headCount));
    }

    /**
     * 当前position所对应的Item的类型
     * 默认Item的类型为0，多布局时其他Item的类型依次递增
     */
    public int getItemType(T d) {
        return super.getItemViewType(0);
    }

    /**
     * 当前position所对应的Item 是否宽度撑满 -- 默认非0撑满
     * 因支持添加头部功能 该方法不能重写 也不能直接在外部调用
     * 若需使用 只能重写 {@link #isItemFullSpan(int)}
     */
    private boolean isFullSpan(int position) {
        int type = getItemViewType(position);
        if (type == 0) {//默认Item
            return false;
        }
        if (type < 0) {//空数据、头部、尾部 默认撑满
            return true;
        }
        //其他类型
        return isItemFullSpan(type);
    }

    /**
     * 多布局时 类型为type的item 是否宽度撑满 -- 默认非0撑满
     */
    protected boolean isItemFullSpan(int type) {
        return true;
    }

    /**
     * 当LayoutManager为GridLayoutManager 且type非0的Item不撑满时 本方法会被调用
     * 因支持添加头部功能 该方法不能重写 也不能直接在外部调用
     * 若需使用 只能重写 {@link #getItemSpanSize(int)}
     */
    private int getGridSpanSize(int position, int defaultCount) {
        int type = getItemViewType(position);
        if (type < 0) {//空数据、头部、尾部 默认宽度
            return defaultCount;
        }
        //其他类型
        return getItemSpanSize(type);
    }

    /**
     * 多布局时 类型为type的item 所占宽度 -- 默认1个单位
     * 特殊布局时使用
     */
    protected int getItemSpanSize(int type) {
        return 1;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new RecyclerHolder(mEmpty);
        }
        if (viewType <= TYPE_FOOTER) {
            return new RecyclerHolder(mFooterViews.get(TYPE_FOOTER - viewType));
        }
        if (viewType <= TYPE_HEADER) {
            return new RecyclerHolder(mHeaderViews.get(TYPE_HEADER - viewType));
        }
        View v = LayoutInflater.from(context).inflate(resId[viewType], parent, false);
        return new RecyclerHolder(v);
    }

    /**
     * 因支持添加头部功能 该方法不能重写 只能重写 {@link #onBindHolder(RecyclerHolder, Object, int)}
     */
    @Override
    public void onBindViewHolder(AbsRecyclerAdapter.RecyclerHolder holder, int position) {
        if (getItemViewType(position) < 0) {
            return;
        }
        int realPosition = position - getHeaderCount();
        onBindHolder(holder, data.get(realPosition), realPosition);
    }

    /**
     * 配置数据 -- holder的类型必须是 AbsRecyclerAdapter.RecyclerHolder
     * 如需给整条Item setTag()，请调用 {@link RecyclerHolder#setItemTag(int, Object)}
     */
    public abstract void onBindHolder(AbsRecyclerAdapter.RecyclerHolder holder, T d, int position);

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
                    return isFullSpan(position) ? gridManager.getSpanCount() :
                            getGridSpanSize(position, gridManager.getSpanCount());
                }
            });
        }
    }

    /**
     * StaggeredGridLayoutManager时 设置type不为0的item 占整行位置
     */
    @Override
    public void onViewAttachedToWindow(AbsRecyclerAdapter.RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.layoutView.getLayoutParams();
        if(lp != null) {
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lpStaggered = (StaggeredGridLayoutManager.LayoutParams) lp;
                lpStaggered.setFullSpan(holder.getParent().isFullSpan(holder.getLayoutPosition()));
            } else if (holder.getItemViewType() < 0) {//LinearLayoutManager时 头部item撑满
                lp.width = RecyclerView.LayoutParams.MATCH_PARENT;
                setHeaderLayoutParams(holder.layoutView, lp);
            }
        }
    }

    protected class RecyclerHolder extends RecyclerView.ViewHolder {

        Map<Integer, View> cacheMap = new HashMap();
        View layoutView;

        public RecyclerHolder(View itemView) {
            super(itemView);
            if(onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getItemViewType() >= 0) {
                            onItemClickListener.onItemClick(v, getLayoutPosition() - getHeaderCount());
                        }
                    }
                });
            }
            if (onItemLongClickListener != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (getItemViewType() >= 0) {
                            return onItemLongClickListener.onItemLongClick(v,
                                    getLayoutPosition() - getHeaderCount());
                        }
                        return true;
                    }
                });
            }
            this.layoutView = itemView;
        }

        private AbsRecyclerAdapter getParent() {
            return AbsRecyclerAdapter.this;
        }

        public View getView(int id){
            if(cacheMap.containsKey(id)){
                return cacheMap.get(id);
            } else {
                View v = layoutView.findViewById(id);
                cacheMap.put(id, v);
                return v;
            }
        }

        public boolean isLastItem() {
            return getLayoutPosition() - getHeaderCount() - getEmptyCount() == data.size() - 1;
        }

        public RecyclerHolder setItemLayoutParams(int width, int height){
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) layoutView.getLayoutParams();
            lp.width = width;
            lp.height = height;
            layoutView.setLayoutParams(lp);
            return this;
        }

        public RecyclerHolder setItemMargins(int left, int top, int right, int bottom) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) layoutView.getLayoutParams();
            lp.setMargins(left, top, right, bottom);
            layoutView.setLayoutParams(lp);
            return this;
        }

        /**
         * 给整条item设置tag
         */
        public RecyclerHolder setItemTag(int id, Object tag) {
            layoutView.setTag(id, tag);
            return this;
        }

        public RecyclerHolder setVisible(int visible) {
            layoutView.setVisibility(visible);
            return this;
        }

        /**
         * 给item中控件设置点击事件，前提：需先设置 {@link #setOnItemClickListener(OnItemClickListener)}
         */
        public RecyclerHolder setOnClickListener(int id) {
            return setOnClickListenerAndTag(id, null);
        }

        public RecyclerHolder setOnClickListenerAndTag(int id, Object tag) {
            View v = getView(id);
            v.setTag(tag);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null) {
                        if (getItemViewType() >= 0) {
                            onItemClickListener.onItemClick(v, getLayoutPosition() - getHeaderCount());
                        }
                    }
                }
            });
            return this;
        }

        public RecyclerHolder setOnClickListenerAndSelected(int id, boolean selected) {
            setOnClickListener(id);
            return setViewSelected(id, selected);
        }

        public RecyclerHolder setViewVisible(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        public RecyclerHolder setViewSelected(int id, boolean selected) {
            getView(id).setSelected(selected);
            return this;
        }

        /**
         * 绑定TextView
         */
        public RecyclerHolder bindTextView(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(text);
            return this;
        }

        /**
         * 绑定TextView
         */
        public RecyclerHolder bindTextView(int id, int resId){
            return bindTextView(id, getString(resId));
        }

        /**
         * 绑定TextView
         */
        public RecyclerHolder bindTextViewWithBgColor(int id, String text, int colorId){
            TextView tv = (TextView) getView(id);
            tv.setText(Html.fromHtml(text));
            tv.setBackgroundResource(colorId);
            return this;
        }

        /**
         * 绑定TextView
         */
        public RecyclerHolder bindTextViewWithBgColor(int id, int resId, int color){
            return bindTextViewWithBgColor(id, getString(resId), color);
        }

        /**
         * 绑定TextView
         */
        public RecyclerHolder bindTextViewWithClickListener(int id, String text){
            bindTextViewWithHtml(id, text);
            return setOnClickListener(id);
        }

        /**
         * 绑定TextView
         */
        public RecyclerHolder bindTextViewWithSelected(int id, String text, boolean isSelect){
            bindTextViewWithHtml(id, text);
            return setViewSelected(id, isSelect);
        }

        /**
         * 绑定TextView 含html标签
         */
        public RecyclerHolder bindTextViewWithHtml(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(Html.fromHtml(text));
            return this;
        }

        /**
         * 绑定ImageView
         */
        public RecyclerHolder bindImageView(int id, int imgId){
            ImageView iv = (ImageView) getView(id);
            iv.setImageResource(imgId);
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerHolder bindSimpleDraweeView(int id, Uri uri){
            try {
                SimpleDraweeView sdv = (SimpleDraweeView) getView(id);
                sdv.setImageURI(uri);
            } catch (Exception e) {
                Log.e("loge", "bindSimpleDraweeView: " + e.getMessage());
            }
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerHolder bindSimpleDraweeView(int id, String url){
            if (!TextUtils.isEmpty(url)) {
                return bindSimpleDraweeView(id, Uri.parse(url));
            }
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerHolder bindSimpleDraweeView(int id, int imgId){
            return bindSimpleDraweeView(id, Uri.parse("res:///" + imgId));
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerHolder bindSimpleDraweeViewBase(int id, String url){
            return bindSimpleDraweeView(id, Constant.URL.BaseImg + url);
        }

        /**
         * 设置进度条进度
         */
        public RecyclerHolder bindProgressBar(int id, int num){
            ProgressBar progressBar = (ProgressBar) getView(id);
            progressBar.setProgress(num);
            return this;
        }

        /**
         * 设置星星进度条
         */
        public RecyclerHolder bindRatingBar(int id, float rating){
            RatingBar ratingBar = (RatingBar) getView(id);
            ratingBar.setRating(rating);
            return this;
        }
    }
}
