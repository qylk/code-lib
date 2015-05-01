package com.qylk.code.widgets.defined;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

public class DragAndDrop extends View {
	private Paint mPaint;
	private Paint mTextPaint;
	private Paint mLinePaint;
	private boolean mIsDraging = false;
	private static final float RADIUS = 170;
	private static final String TEXT_LEGEND = "Drag Me";
	private static final String TEXT_LEGEND_DRAGING = "Drop Here";

	public DragAndDrop(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.FILL);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setTextSize(30);

		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(1);
		mLinePaint.setColor(Color.WHITE);
		PathEffect effect = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		mLinePaint.setPathEffect(effect);

		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				ClipData data = ClipData.newPlainText("data", "dataText");
				v.startDrag(data, new DragShadowBuilder(v), null, 0);
				return true;
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final float centerX = getWidth() / 2;
		final float centerY = getHeight() / 2;
		canvas.drawCircle(centerX, centerY, RADIUS, mPaint);
		canvas.drawLine(0, centerY, centerX + RADIUS, centerY, mLinePaint);// Horizontal
		// line
		canvas.drawLine(centerX, 0, centerX, centerY + RADIUS, mLinePaint);// vertical
		// line

		final FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
		// 为了居中对齐
		final int height = -(fontMetrics.descent + fontMetrics.ascent);
		if (mIsDraging)
			canvas.drawText(TEXT_LEGEND_DRAGING, 0,
					TEXT_LEGEND_DRAGING.length(), centerX,
					centerY + height / 2, mTextPaint);
		else
			canvas.drawText(TEXT_LEGEND, 0, TEXT_LEGEND.length(), centerX,
					centerY + height / 2, mTextPaint);
		canvas.drawLine(0, centerY + fontMetrics.descent, centerX + RADIUS,
				centerY + fontMetrics.descent, mLinePaint);// descent line
		canvas.drawLine(0, centerY + fontMetrics.ascent, centerX + RADIUS,
				centerY + fontMetrics.ascent, mLinePaint);// ascent line
		canvas.drawLine(0, centerY - height / 2, centerX + RADIUS, centerY
				- height / 2, mLinePaint);// maybe baseline
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int sizeH = (int) (getPaddingLeft() + getPaddingRight() + RADIUS * 2);
		int sizeV = (int) (getPaddingTop() + getPaddingBottom() + RADIUS * 2);
		setMeasuredDimension(sizeH, sizeV);
	}

	@Override
	public boolean onDragEvent(DragEvent event) {
		// 顺序：DRAG_STARTED->DRAG_ENTERED->DRAG_LOCATION->DRAG_EXITED->DRAG_ENDED
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:// 此时返回true,则表示该View允许接收Drop事件，否则不会再接收拖拽事件
				mIsDraging = true;
				invalidate();
				System.out.println("start");
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				System.out.println("location");
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				System.out.println("enter");
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				System.out.println("exit");
				break;
			case DragEvent.ACTION_DROP:// 此时返回true,表示准许Drop，返回false,表示反悔DROP事件
				mIsDraging = false;
				invalidate();
				Toast.makeText(getContext(),
						event.getClipData().getItemAt(0).getText(),
						Toast.LENGTH_SHORT).show();
				System.out.println("drop");
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				System.out.println("ended:" + event.getResult());// getResult()返回是否正确DROP了
				break;
			default:
				break;
		}
		return true;
	}
}
