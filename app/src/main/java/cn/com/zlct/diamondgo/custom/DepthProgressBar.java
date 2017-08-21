package cn.com.zlct.diamondgo.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import cn.com.zlct.diamondgo.R;


/**
 * 类似于 水深 的进度条
 */
public class DepthProgressBar extends View {

    private Paint mPaintCircle;
    private Paint mPaintRect;
    private int max;
    private int shape;
    private int progress;

    public DepthProgressBar(Context context) {
        this(context, null);
    }

    public DepthProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        init();
    }

    /**
     * 解析自定义属性
     */
    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.depthProgressBar);
        max = typedArray.getInteger(R.styleable.depthProgressBar_maxs, 100);
        shape = typedArray.getInt(R.styleable.depthProgressBar_shapes, 0);
        typedArray.recycle();
    }

    private void init() {
        progress = 0;
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.GRAY);
        mPaintCircle.setAlpha(160);
        mPaintRect = new Paint();
        mPaintRect.setAntiAlias(true);
        mPaintRect.setColor(Color.RED);
        mPaintRect.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        //wrap_content
        setMeasuredDimension(sizeWidth, sizeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (shape){
            case 0://画圆底
                canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, mPaintCircle);
                break;
            case 1://画方底
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaintCircle);
                break;
        }
        canvas.drawRect(0, 0, getWidth(), progress, mPaintRect);
    }

    public synchronized void setProgress(int pro) {
        if (pro * getHeight() / max > progress) {
            progress = pro * getHeight() / max;
            postInvalidate();
        }
    }

    public void initProgress() {
        progress = 0;
        postInvalidate();
    }
}
