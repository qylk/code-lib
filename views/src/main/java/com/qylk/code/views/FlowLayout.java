package com.qylk.code.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个流布局，尽管往里放任意多的东西，它会自动横向排列，自动换行
 * Created by qylk on 15/4/3.
 */
public class FlowLayout extends ViewGroup {
    private List<Integer> linesHeight = new ArrayList<Integer>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 产生Child的LayoutParams，当child获取LayoutParams时就是这个
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0, top = 0;
        int line = 0;
        int pwidth = getWidth();//ViewGroup尺寸
        int childSum = getChildCount();
        for (int i = 0; i < childSum; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            if (left + child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin > pwidth) {//换行
                left = 0;//重置left
                top += linesHeight.get(line++);
            }
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int offsetTop = (linesHeight.get(line) - child.getMeasuredHeight()) / 2;//为了垂直居中效果
            child.layout(left, top + offsetTop, left + childWidth, top + offsetTop + childHeight);
            left += childWidth;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        linesHeight.clear();
        int pwidth = MeasureSpec.getSize(widthMeasureSpec);//父ViewGroup的最大尺寸
        int pheight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);//父ViewGroup的尺寸模式
        int modeheight = MeasureSpec.getMode(heightMeasureSpec);


        int width = 0, height = 0;//统计父ViewGroup的所需尺寸（如果mode!=EXACTLY）

        int lineWidth = 0;//行宽
        int lineHeight = 0;//行高

        int childSum = getChildCount();
        for (int i = 0; i < childSum; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);//先测量child
            MarginLayoutParams childLp = (MarginLayoutParams) child.getLayoutParams();//这里就可以获得child尺寸了
            int childWidth = child.getMeasuredWidth() + childLp.leftMargin + childLp.rightMargin;//child尺寸
            int childHeight = child.getMeasuredHeight() + childLp.topMargin + childLp.bottomMargin;
            if (lineWidth + childWidth > pwidth) {//改行容不下这个child,换行
                width = Math.max(lineWidth, width);// 取最大的行宽为ViewGroup的宽度
                lineWidth = childWidth;
                height += lineHeight;//增加ViewGroup高度
                linesHeight.add(lineHeight);//保存当前行高度
                lineHeight = childHeight;//重置lineHeight，重新开始统计最大值
            } else {//不换行
                lineWidth += childWidth;
                lineHeight = Math.max(childHeight, lineHeight);//最高的child作为行高
            }
            if (i == childSum - 1) {//最后一个child
                width = Math.max(width, lineWidth);
                height += lineHeight;
                linesHeight.add(lineHeight);//把最后一行的行高保存起来
            }
        }
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? pwidth : width,
                modeheight == MeasureSpec.EXACTLY ? pheight : height);//设置尺寸

    }
}
