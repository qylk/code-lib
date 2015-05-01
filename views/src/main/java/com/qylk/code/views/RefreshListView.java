/**
 * Copyright 2012-2013 Jeremie Martinez (jeremiemartinez@gmail.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.qylk.code.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.*;
import com.qylk.code.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author jmartinez
 *         <p/>
 *         Simple Android ListView that enables pull to refresh as Twitter or Facebook apps ListView. Developers must implement OnRefreshListener interface and set it to the list. They also have to
 *         call finishRefreshing when their task is done. See <a href="https://github.com/jeremiemartinez/RefreshListView">Project Site</a> for more information.
 */
public class RefreshListView extends ListView {

    private static final int RESISTANCE = 3;
    //  private static final int HEADER_HEIGHT = 60;
    private static final int DURATION = 300;

    private OnRefreshListener mRefreshListener;
    private View mContainer;
    private RelativeLayout header;
    private ProgressBar mProgress;
    private TextView mComment;
    private ImageView mArrowIcon;
    private TextView date;
    private LayoutInflater mLayoutInflater;

    private float mCurrentY;

    private int mHeaderHeight;

    private boolean enabledDate;
    private Date lastUpdateDate;
    private DateFormat formatter;

    public static final int PULLING_DOWN = 0;
    public static final int CAN_RELEASE_NOW = 1;
    public static final int UPDATING = 2;

    private int mCurrentState;


    private static final int CLOCKWISE = 0;
    private final int ANTICLOCKWISE = 1;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * initializing method. Call in constructors to set up the headers.
     *
     * @param context activity context, got by constructors
     */
    private void init(Context context) {
        formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        mLayoutInflater = LayoutInflater.from(context);
        mContainer = mLayoutInflater.inflate(R.layout.layout_refreshlistview_header, null);
        header = (RelativeLayout) mContainer.findViewById(R.id.header);
        mArrowIcon = (ImageView) mContainer.findViewById(R.id.arrow);
        mProgress = (ProgressBar) mContainer.findViewById(R.id.progress);
        date = (TextView) mContainer.findViewById(R.id.date);
        mComment = (TextView) mContainer.findViewById(R.id.comment);

        mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        header.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        addHeaderView(mContainer);

        mHeaderHeight = (int) getResources().getDimension(R.dimen.refreshlistView_header_height);
        changeHeaderHeight(0);

        mComment.setText(getResources().getString(R.string.refreshlistview_pulldown));
        mCurrentState = PULLING_DOWN;
    }

    /**
     * Call to perform item click. Reset the position without the header
     *
     * @see AbsListView#performItemClick(View, int, long)
     */
    @Override
    public boolean performItemClick(View view, int position, long id) {
        if (position == 0) {
            return true;
        } else {
            return super.performItemClick(view, position - getHeaderViewsCount(), id);
        }
    }

    /**
     * Set up first touch to later calculations.
     *
     * @see AbsListView#onInterceptTouchEvent(MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentY = ev.getY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Handle what to do when the user release the touch.
     *
     * @see AbsListView#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mCurrentState != UPDATING) {
                    if (mCurrentState == CAN_RELEASE_NOW) {//开始去更新
                        header.startAnimation(new ResizeHeaderAnimation(mHeaderHeight));
                        startRefreshing();
                    } else {//放弃了
                        header.startAnimation(new ResizeHeaderAnimation(0));
                    }
                } else {//正在更新
                    header.startAnimation(new ResizeHeaderAnimation(mHeaderHeight));
                }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * Used to show header when scrolling down.
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE && getFirstVisiblePosition() == 0) {
            if (isAllowedToShowHeader(ev.getY())) {
                changeHeaderHeight(getHeightWithScrollResistance(ev.getY()));//控制header位置
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Call to update UI when the refresh started.
     */
    private void startRefreshing() {
        mArrowIcon.clearAnimation();
        mArrowIcon.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
        mComment.setText(getResources().getString(R.string.refreshlistview_updating));

        if (mRefreshListener != null) {
            mRefreshListener.onRefresh(this);
        }
        mCurrentState = UPDATING;
    }

    /**
     * Call when refreshing task is done. Must be called by the developer.
     */
    public void finishRefreshing() {
        finishRefreshing(null);
    }

    /**
     * Call when refreshing task is done. Must be called by the developer.
     *
     * @param updateDate allow developer to set the last updateDate
     */
    public void finishRefreshing(Date updateDate) {
        header.startAnimation(new ResizeHeaderAnimation(0));
        mProgress.setVisibility(View.INVISIBLE);
        mArrowIcon.setVisibility(View.VISIBLE);
        if (updateDate == null)
            lastUpdateDate = new Date();
        else
            lastUpdateDate = updateDate;
        date.setText(getFormattedDate(lastUpdateDate));
        mComment.setText(getResources().getString(R.string.refreshlistview_pulldown));
        mCurrentState = PULLING_DOWN;
        invalidate();
    }

    /**
     * Change the header height while scrolling down by making it visible and increasing topMargin of the header.
     *
     * @param height the height of the header
     */
    private void changeHeaderHeight(int height) {
        hideOrShowHeader(height);

        LayoutParams layoutParams = (LayoutParams) mContainer.getLayoutParams();
        layoutParams.height = height;
        mContainer.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams headerLayoutParams = (LinearLayout.LayoutParams) header.getLayoutParams();
        headerLayoutParams.topMargin = height - mHeaderHeight;//通过TopMargin控制位置
        header.setLayoutParams(headerLayoutParams);

        if (mCurrentState != UPDATING) {
            if (height > mHeaderHeight && mCurrentState == PULLING_DOWN) {
                mArrowIcon.startAnimation(getRotationAnimation(ANTICLOCKWISE));
                mComment.setText(getResources().getString(R.string.refreshlistview_release));
                mCurrentState = CAN_RELEASE_NOW;//完全展现出header
            } else if (height < mHeaderHeight && mCurrentState == CAN_RELEASE_NOW) {
                mArrowIcon.startAnimation(getRotationAnimation(CLOCKWISE));
                mComment.setText(getResources().getString(R.string.refreshlistview_pulldown));
                mCurrentState = PULLING_DOWN;//放弃了
            }
        }
    }

    /**
     * Check whether or not the header should be shown.
     *
     * @param newY just acquired Y event
     * @return true if it is, false otherwise
     */
    private boolean isAllowedToShowHeader(float newY) {
        return isScrollingEnough(newY) && (mCurrentState != UPDATING || (mCurrentState == UPDATING && (newY - mCurrentY) > 0));
    }

    /**
     * Check if the scroll is enough to be taken into acccount.
     *
     * @param newY just acquired Y event
     * @return true if it is, false otherwise
     */
    private boolean isScrollingEnough(float newY) {
        float deltaY = Math.abs(mCurrentY - newY);
        ViewConfiguration config = ViewConfiguration.get(getContext());
        return deltaY > config.getScaledTouchSlop();//检查下拉距离是否够大
    }

    /**
     * Calculate height header when scrolling down.
     *
     * @param newY just acquired Y event
     * @return the height of the header to set
     */
    private int getHeightWithScrollResistance(float newY) {
        return Math.max((int) (newY - mCurrentY) / RESISTANCE, 0);
    }

    /**
     * Hide or show the header according to its height.
     *
     * @param height current height of the header
     */
    private void hideOrShowHeader(int height) {
        if (height <= 0) {
            header.setVisibility(View.GONE);
        } else {
            header.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Getter to know if date is enabled
     *
     * @return true if date is enabled, false otherwise
     */
    public boolean isEnabledDate() {
        return enabledDate;
    }

    /**
     * Set enabled date, the first date to show will just be "No past update".
     *
     * @param enabledDate
     */
    public void setEnabledDate(boolean enabledDate) {
        setEnabledDate(enabledDate, null);
    }

    public void setEnabledDate(boolean enabledDate, Date firstDate) {
        this.enabledDate = enabledDate;
        lastUpdateDate = firstDate;
        if (enabledDate) {
            date.setVisibility(View.VISIBLE);
            if (firstDate != null) {
                date.setText(getFormattedDate(firstDate));
            } else {
                date.setText(getResources().getString(R.string.refreshlistview_no_update));
            }
        } else {
            date.setVisibility(View.GONE);
        }
    }

    /**
     * Getter for last update date.
     *
     * @return the last update date or null is it has never been updated yet.
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    private String getFormattedDate(Date date) {
        return formatter.format(date);
    }

    public void setmRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    /**
     * Callback. Call when user asks to refresh the list. Required to be implemented by developer.
     */
    public interface OnRefreshListener {
        void onRefresh(RefreshListView listView);
    }

    private Animation getRotationAnimation(int rotation) {
        int fromAngle = 0;
        int toAngle = 0;
        if (rotation == ANTICLOCKWISE)
            toAngle = 180;
        else
            fromAngle = 180;
        Animation animation = new RotateAnimation(fromAngle, toAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(DURATION);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * Animation to resize the header's height
     */
    public class ResizeHeaderAnimation extends Animation {
        private int toHeight;

        public ResizeHeaderAnimation(int toHeight) {
            this.toHeight = toHeight;
            setDuration(DURATION);
        }

        /**
         * Animation core, animate the height of the header to a specific value.
         *
         * @see Animation#applyTransformation(float, Transformation)
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float height = (toHeight - mContainer.getHeight()) * interpolatedTime + mContainer.getHeight();
            LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
            LinearLayout.LayoutParams headerlp = (LinearLayout.LayoutParams) header.getLayoutParams();
            headerlp.topMargin = (int) height - mHeaderHeight;
            lp.height = (int) height;
            lp.width = mContainer.getWidth();
            mContainer.requestLayout();
        }

        /**
         * Used at the end of the animation to hide completely the header if it's required (toHeight == 0).
         *
         * @see Animation#getTransformation(long, Transformation)
         */
        @Override
        public boolean getTransformation(long currentTime, Transformation outTransformation) {
            hideOrShowHeader(toHeight);
            return super.getTransformation(currentTime, outTransformation);
        }
    }

    /**
     * Display a toast when there is an error in refresh task
     *
     * @param errorMessage error message to display
     */
    public void errorInRefresh(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
