package com.qylk.code.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import com.qylk.code.R;

/**
 * Created by qylk on 15/4/2.
 */
public class RoundImageView extends ImageView {
    /**
     * 圆角矩形类型
     */
    public static final int TYPE_ROUND_RECT = 0;
    /**
     * 圆形类型
     */
    public static final int TYPE_ROUND = 1;
    private Matrix mMatrix;
    private Paint mBitmapPaint;
    private int mType = TYPE_ROUND;
    private int mRadius = 25;
    private RectF mRoundRect;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.roundImageView);
        int N = array.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(array.getIndex(i), array);
        }
        array.recycle();
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        //init();//new code added
    }

//    private final Paint maskPaint = new Paint();
//    private final Paint zonePaint = new Paint();
//
//    private void init() {
//        maskPaint.setAntiAlias(true);
//        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        zonePaint.setAntiAlias(true);
//        zonePaint.setColor(Color.WHITE);
//    }

    private void initAttr(int attr, TypedArray array) {
        if (attr == R.styleable.roundImageView_type) {
            mType = array.getInt(attr, TYPE_ROUND);
        } else if (attr == R.styleable.roundImageView_radius) {
            mRadius = array.getDimensionPixelSize(attr,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius,
                            getContext().getResources().getDisplayMetrics()));
        }
    }


    /**
     * 设置圆形半径，或者圆角半径
     *
     * @param raduis
     * @see #TYPE_ROUND
     * @see #TYPE_ROUND_RECT
     */
    public void setRadius(int raduis) {
        this.mRadius = raduis;
    }

    /**
     * 设置圆形或者圆角矩形
     *
     * @param type
     * @see #TYPE_ROUND
     * @see #TYPE_ROUND_RECT
     */
    public void setType(int type) {
        this.mType = type;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mType == TYPE_ROUND) {
            int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
            int padding = getPaddingTop();
            this.mRadius = (min - padding) / 2;
            setMeasuredDimension(min, min);
        }
    }


    //TODO 每次onDraw都执行，是否可以优化
    private void setupShader() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        Bitmap bmp = drawableToBitamp(drawable);
        ////将bmp作为着色器，就是在指定区域内绘制bmp
        BitmapShader mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        final int rw = getWidth() - getPaddingLeft() - getPaddingRight();
        final int rh = getHeight() - getPaddingTop() - getPaddingBottom();
        if (mType == TYPE_ROUND) {
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = rw * 1.0f / bSize;
        } else if (mType == TYPE_ROUND_RECT) {
            scale = Math.max(rw * 1.0f / bmp.getWidth(), rh
                    * 1.0f / bmp.getHeight());
        }
        mMatrix.setScale(scale, scale);  // shader的变换矩阵，我们这里主要用于放大或者缩小
        mBitmapShader.setLocalMatrix(mMatrix);// 设置变换矩阵
        mBitmapPaint.setShader(mBitmapShader); // 设置shader
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setupShader();
        if (mType == TYPE_ROUND_RECT) {
            canvas.drawRoundRect(mRoundRect, mRadius, mRadius,
                    mBitmapPaint);
        } else {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mBitmapPaint);
        }
    }

//    @Override
//    public void draw(Canvas canvas) {
//        canvas.saveLayer(mRoundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
//        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
//        //
//        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
//        super.draw(canvas);
//        canvas.restore();
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mType == TYPE_ROUND_RECT)
            mRoundRect = new RectF(0, 0, w, h);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
