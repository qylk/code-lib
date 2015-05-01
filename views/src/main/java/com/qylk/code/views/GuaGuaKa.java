package com.qylk.code.views;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


/**
 * Created by qylk on 15/4/3.
 */
public class GuaGuaKa extends ImageView {
    private static final int THRESHOLD = 10;
    private Paint mMaskPaint;
    private Path mPath;
    private float mLastX;
    private float mLastY;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    public GuaGuaKa(Context context) {
        this(context, null);
    }

    public GuaGuaKa(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaKa(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mMaskPaint = new Paint();
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setDither(true);
        mMaskPaint.setStrokeWidth(20);
        mMaskPaint.setStyle(Paint.Style.STROKE);
        mMaskPaint.setStrokeCap(Paint.Cap.ROUND);
        mMaskPaint.setStrokeJoin(Paint.Join.ROUND);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);//通过一个Canvas来改变(绘制)我们的mBitmap
        mCanvas.drawColor(Color.GRAY);//打上Mask层(SRC层)
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//绘制背景图片
        mCanvas.drawPath(mPath, mMaskPaint);//绘制痕迹
        canvas.drawBitmap(mBitmap, 0, 0, null);//绘制带有痕迹的mBitmap
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltx = Math.abs(x - mLastX);
                float delty = Math.abs(y - mLastY);
                if (deltx > THRESHOLD || delty > THRESHOLD) {
                    mPath.lineTo(x, y);
                    mLastX = x;
                    mLastY = y;
                }
                break;
        }
        invalidate();
        return true;//返回true,否则没有MotionEvent.ACTION_MOVE事件
    }
}
