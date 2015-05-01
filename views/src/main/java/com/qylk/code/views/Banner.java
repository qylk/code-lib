package com.qylk.code.views;

/**
 * Created by qylk on 15/3/29.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.qylk.code.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个广告条，可以自动播放
 */
public class Banner extends RelativeLayout {
    private static final boolean DEBUG = false;
    private static final String TAG = "Banner";
    private ViewPager mViewPager = null;
    private List<? extends View> mViews = null;
    private LinearLayout mPointContainer = null;
    private List<View> mPoints = null;
    private final Context mContext;
    private boolean mCalled = false;
    private boolean mShowPoint = false;
    private boolean mAutoPlay = false;
    private boolean mIsAutoPlaying = false;
    private int mAutoPlayInterval = 2000;
    private int mInitPosition = 0;
    private int mPointGravity = Gravity.LEFT | Gravity.BOTTOM;
    private int mPointSpacing = 15;
    private int mCurrentPosition = 0;
    private Drawable mPointFocusedDrawable;
    private Drawable mPointUnfocusedDrawable;
    private Drawable mPointContainerBackgroundDrawable;
    private Handler mPagerHandler;
    private float oldX, oldY;
    private final int mScaledTouchSlope;
    private onPagerItemClickListener mPagerItemClickListener;
    private Runnable mAutoPlayTask = new Runnable() {

        @Override
        public void run() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            mPagerHandler.postDelayed(mAutoPlayTask, mAutoPlayInterval);
        }
    };


    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        mScaledTouchSlope = ViewConfiguration.get(context).getScaledTouchSlop();
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.banner);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        loadUnsetDefaults(context);
        typedArray.recycle();
    }

    private void loadUnsetDefaults(Context context) {
        Resources res = context.getResources();
        if (mPointFocusedDrawable == null)
            mPointFocusedDrawable = res.getDrawable(R.drawable.banner_point_focused);
        if (mPointUnfocusedDrawable == null)
            mPointUnfocusedDrawable = res.getDrawable(R.drawable.banner_point_normal);
    }

    private void initAttr(int attr, TypedArray typedArray) {
        switch (attr) {
            case R.styleable.banner_point_focused:
                mPointFocusedDrawable = typedArray.getDrawable(attr);
                break;
            case R.styleable.banner_point_normal:
                mPointUnfocusedDrawable = typedArray.getDrawable(attr);
                break;
            case R.styleable.banner_point_container_background:
                mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);
                break;
            case R.styleable.banner_point_spacing:
                mPointSpacing = typedArray.getDimensionPixelSize(attr, mPointSpacing);
                break;
            case R.styleable.banner_point_gravity:
                mPointGravity = typedArray.getInt(attr, mPointGravity);
                break;
            case R.styleable.banner_show_point:
                mShowPoint = typedArray.getBoolean(attr, mShowPoint);
                break;
            case R.styleable.banner_auto_play:
                mAutoPlay = typedArray.getBoolean(attr, mAutoPlay);
                break;
            case R.styleable.banner_auto_play_interval:
                mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
                break;
            case R.styleable.banner_init_position:
                mInitPosition = typedArray.getInteger(attr, mInitPosition);
                break;
            default:
                break;
        }
    }

    public boolean isShowPoint() {
        return mShowPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.mShowPoint = showPoint;
        if (mCalled) {
            throw new RuntimeException("this method should be called before setViewPagerViews been called");
        }
    }
    
    public int getPointGravity() {
        return mPointGravity;
    }

    public void setPointGravity(int gravity) {
        this.mPointGravity = gravity;
        if (mCalled) {
            throw new RuntimeException("this method should be called before setViewPagerViews been called");
        }
    }

    public boolean isAutoPlay() {
        return mAutoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (autoPlay) {
            startAutoPlay();
        } else {
            stopAutoPlay();
        }
    }

    public int getAutoPlayInterval() {
        return mAutoPlayInterval;
    }

    public void setAutoPlayInterval(int autoPlayInterval) {
        this.mAutoPlayInterval = autoPlayInterval;
    }

    public int getInitPosition() {
        return mInitPosition;
    }

    public void setInitPosition(int initPosition) {
        this.mInitPosition = initPosition;
        if (mCalled)
            throw new RuntimeException("this method should be called before setViewPagerViews been called");
    }


    private void initView(Context context) {
        mViewPager = new ViewPager(context);
        mViewPager.setId(1000);
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void processPoint(Context context) {
        mPointContainer = new LinearLayout(context);
        mPointContainer.setOrientation(LinearLayout.HORIZONTAL);
        if (mPointContainerBackgroundDrawable != null) {
            mPointContainer.setBackgroundDrawable(mPointContainerBackgroundDrawable);
        }
        LayoutParams pointContainerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pointContainerLp.setMargins(10, 10, 10, 10);
        // 处理圆点在顶部还是底部
        if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            pointContainerLp.addRule(RelativeLayout.ALIGN_TOP, mViewPager.getId());
        } else {
            pointContainerLp.addRule(RelativeLayout.ALIGN_BOTTOM, mViewPager.getId());
        }
        int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        // 处理圆点在左边、右边还是水平居中
        if (horizontalGravity == Gravity.LEFT) {
            mPointContainer.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        } else if (horizontalGravity == Gravity.RIGHT) {
            mPointContainer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            mPointContainer.setGravity(Gravity.CENTER);
        }
        addView(mPointContainer, pointContainerLp);
    }

    /*
    * 设置ViewPager要显示的Views,如果是ImageView最好用setBackgroundResource，而不是setImageResource
    * */
    public void setViewPagerViews(List<? extends View> views) {
        mCalled = true;
        mViews = views;
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setCurrentItem(mInitPosition);
        if (mShowPoint) {
            processPoint(mContext);
            initPoints();
        }
        if (mAutoPlay) {
            processAutoPlay();
        }
    }

    private void initPoints() {
        mPointContainer.removeAllViews();
        mViewPager.removeAllViews();
        if (mPoints != null) {
            mPoints.clear();
        } else {
            mPoints = new ArrayList<View>();
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = mPointSpacing / 2;
        lp.setMargins(margin, 0, margin, 0);
        View point;
        for (int i = 0; i < mViews.size(); i++) {
            point = new ImageView(getContext());
            point.setLayoutParams(lp);
            point.setBackgroundDrawable(mPointUnfocusedDrawable);
            mPoints.add(point);
            mPointContainer.addView(point);
        }
    }

    private void processAutoPlay() {
        mPagerHandler = new Handler();
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://触摸时停止播放
                        stopAutoPlay();
                        oldX = event.getX();
                        oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP://触摸停止时继续播放
                        startAutoPlay();
                        if (Math.abs(event.getX() - oldX) + Math.abs(event.getY() - oldY) < 2 * mScaledTouchSlope)
                            onItemClick(mCurrentPosition);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public interface onPagerItemClickListener {
        void onPagerClick(int position);
    }

    public void setOnPagerItemClickListener(onPagerItemClickListener listner) {
        this.mPagerItemClickListener = listner;
    }

    private void onItemClick(int position) {
        if (mPagerItemClickListener != null)
            mPagerItemClickListener.onPagerClick(position);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startAutoPlay();
        } else if (visibility == INVISIBLE) {
            stopAutoPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPagerHandler != null) {
            mPagerHandler.removeCallbacks(mAutoPlayTask);
        }
    }

    private void startAutoPlay() {
        if (mAutoPlay && !mIsAutoPlaying) {
            mIsAutoPlaying = true;
            System.out.println("postDelayed");
            mPagerHandler.postDelayed(mAutoPlayTask, mAutoPlayInterval);
        }
        if (DEBUG)
            System.out.println("startAutoPlay");
    }

    private void stopAutoPlay() {
        if (mAutoPlay && mIsAutoPlaying) {
            mIsAutoPlaying = false;
            mPagerHandler.removeCallbacks(mAutoPlayTask);
            System.out.println("removeCallbacks");
        }
        if (DEBUG)
            System.out.println("stopAutoPlay");
    }

    private void switchToPosition(int newPosition) {
        mPoints.get(mCurrentPosition).setBackgroundDrawable(mPointUnfocusedDrawable);
        mPoints.get(newPosition).setBackgroundDrawable(mPointFocusedDrawable);
        mCurrentPosition = newPosition;
    }

    private final class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mAutoPlay ? Integer.MAX_VALUE : mViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = position % mViews.size();
            container.addView(mViews.get(realPosition));
            return mViews.get(realPosition);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int realPosition = position % mViews.size();
            container.removeView(mViews.get(realPosition));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    private final class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:// 开始滑动
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    // 当松开手时
                    // 如果没有其他页显示出来：SCROLL_STATE_DRAGGING --> SCROLL_STATE_IDLE
                    // 如果有其他页有显示出来（不管显示了多少），就会触发正在设置页码
                    // 页码没有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> SCROLL_STATE_IDLE
                    // 页码有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> onPageSelected --> SCROLL_STATE_IDLE
                    break;
                case ViewPager.SCROLL_STATE_IDLE:// 停止滑动
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (DEBUG)
                Log.v(TAG, "onPageScrolled:  position=" + position + "  positionOffset=" + positionOffset + "  positionOffsetPixels=" + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if (mShowPoint) {
                if (mAutoPlay) {
                    switchToPosition(position % mViews.size());
                } else {
                    switchToPosition(position);
                }
            }
        }
    }

}