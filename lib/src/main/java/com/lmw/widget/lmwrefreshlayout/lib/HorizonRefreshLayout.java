package com.lmw.widget.lmwrefreshlayout.lib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.lmw.widget.lmwrefreshlayout.lib.listener.OnPullToRefreshListener;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;
import com.lmw.widget.lmwrefreshlayout.lib.utils.DensityUtil;
import com.lmw.widget.lmwrefreshlayout.lib.view.RefreshView;

public abstract class HorizonRefreshLayout extends BaseRefreshLayout {
    public static final String TAG = "HorizonRefreshLayout";
    private static final int INVALID_POINTER = -1;
    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float mPullLeftX = 0;
    // 上拉的距离
    private float mPullRightX = 0;
    // 按下Y坐标，上一个事件点Y坐标
    private float mDownX, mLastX, mInterceptDownY, mInterceptLastY, mInterceptDownX, mInterceptLastX;
    // 当前状态
    private State mState = State.IDLE;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float mRadio = 2;
    // 释放刷新的距离
    private float mReleaseDistance = 200;
    // 释放加载的距离
    private float mLoadMoreDistance = 50;
    private int mDuration = 200;
    private ValueAnimator mValueAnimator;
    private OnPullToRefreshListener mPullToRefreshListener;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsBeingDragged;
    private int mTouchSlop;
    private boolean isLayout;
    private boolean isPullLeft;
    private boolean isPullRight;

    public HorizonRefreshLayout(Context context) {
        this(context, null);
    }

    public HorizonRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizonRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null && mTarget != null && mHeaderView.getMeasuredHeight() != mTarget.getMeasuredHeight()) {
            mHeaderView.getLayoutParams().height = mTarget.getMeasuredHeight();
        }
        if (mFooterView != null && mTarget != null && mFooterView.getMeasuredHeight() != mTarget.getMeasuredHeight()) {
            mFooterView.getLayoutParams().height = mTarget.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isLayout) {
            return;
        }
        isLayout = true;
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        if (mHeaderView != null) {
            int headerWidth = mHeaderView.getMeasuredWidth();
            int headerHeight = mHeaderView.getMeasuredHeight();

            final View topChild = mHeaderView;
            final int topChildLeft = (int) (topChild.getPaddingLeft() + mPullLeftX + mPullRightX - headerWidth);
            final int topChildTop = (int) (topChild.getPaddingTop());
            final int topChildRight = topChildLeft + headerWidth;
            final int topChildBottom = topChildTop + headerHeight;

            topChild.layout(topChildLeft,
                    topChildTop,
                    topChildRight,
                    topChildBottom);
        }


        final View child = mTarget;
        final int childLeft = (int) (child.getPaddingLeft() + mPullLeftX + mPullRightX);
        final int childTop = child.getPaddingTop();
        final int childRight = childLeft + child.getMeasuredWidth();
        final int childBottom = childTop + child.getMeasuredHeight();

        child.layout(childLeft,
                childTop,
                childRight,
                childBottom);

        if (mFooterView != null) {
            int footerWidth = mFooterView.getMeasuredWidth();
            int footerHeight = mFooterView.getMeasuredHeight();

            final View bottomChild = mFooterView;
            final int bottomChildLeft = bottomChild.getPaddingLeft() + child.getMeasuredWidth();
            final int bottomChildTop = bottomChild.getPaddingTop();
            final int bottomChildRight = bottomChildLeft + footerWidth;
            final int bottomChildBottom = bottomChildTop + footerHeight;
            mFooterView.layout(bottomChildLeft,
                    bottomChildTop,
                    bottomChildRight,
                    bottomChildBottom);
        }

    }

    private void changeState(State state) {
        mState = state;
        switch (state) {
            case IDLE:
                ((RefreshView) mHeaderView).onStateChanged(state);
                ((RefreshView) mFooterView).onStateChanged(state);
                break;
            case RELEASE_TO_REFRESH:
                ((RefreshView) mHeaderView).onStateChanged(state);
                break;
            case REFRESHING:
                ((RefreshView) mHeaderView).onStateChanged(state);
                // 刷新操作
                if (mPullToRefreshListener != null) mPullToRefreshListener.onRefresh();
                break;
            case RELEASE_TO_LOAD:
                ((RefreshView) mFooterView).onStateChanged(state);
                break;
            case LOADING:
                // 正在加载状态
                ((RefreshView) mFooterView).onStateChanged(state);
                // 加载操作
                if (mPullToRefreshListener != null) mPullToRefreshListener.onLoadMore();
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = ev.getActionMasked();
        int pointerIndex;

        if (!isEnabled() || (mValueAnimator != null && mValueAnimator.isRunning())) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                isPullLeft = false;
                isPullRight = false;
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                mDownX = ev.getX(pointerIndex);
                mLastX = mDownX;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float x = ev.getX(pointerIndex);
                startDragging(x);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                isPullLeft = false;
                isPullRight = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    private void startDragging(float x) {
        final float xDiff = x - mDownX;

        if (xDiff > mTouchSlop && !canChildScrollLeft() && !mIsBeingDragged) {
            mIsBeingDragged = true;
        } else if (xDiff < 0 && Math.abs(xDiff) > mTouchSlop && !canChildScrollRight() && !mIsBeingDragged) {
            mIsBeingDragged = true;
        }
    }

    private boolean canChildScrollLeft() {
        return mTarget.canScrollHorizontally(-1);
    }

    private boolean canChildScrollRight() {
        return mTarget.canScrollHorizontally(1);
    }

    /**
     * 完成刷新或者加载
     */
    public void onFinish() {
        if (mState == State.REFRESHING || mState == State.LOADING) {
            changeState(State.IDLE);
        }
        resilience();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();

        if (!isEnabled() || (mValueAnimator != null && mValueAnimator.isRunning())) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                if (mCanPullDown && mState != State.LOADING && (ev.getX() - mLastX > 0)) {
                    moveDown(ev);
                } else if (mCanPullUp && mState != State.REFRESHING && (ev.getX() - mLastX < 0)) {
                    moveUp(ev);
                }

                if (mPullLeftX > 0) {
                    if (mPullLeftX <= mReleaseDistance) {//下拉刷新
                        changeState(State.IDLE);
                    } else if (mPullLeftX > mReleaseDistance) {//释放刷新
                        changeState(State.RELEASE_TO_REFRESH);
                    }
                } else if (mPullRightX < 0) {
                    if (Math.abs(mPullRightX) <= mLoadMoreDistance) {//下拉加载
                        changeState(State.IDLE);
                    } else if (Math.abs(mPullRightX) > mLoadMoreDistance) {//自动释放加载
                        changeState(State.RELEASE_TO_LOAD);
                        ev.setAction(MotionEvent.ACTION_UP);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState == State.RELEASE_TO_REFRESH) {
                    changeState(State.REFRESHING);
                } else if (mState == State.RELEASE_TO_LOAD) {
                    changeState(State.LOADING);
                } else {
                    changeState(State.IDLE);
                }

                resilience();
                break;
            case MotionEvent.ACTION_CANCEL:
                return false;
            default:
                break;
        }
        return true;
    }

    private void resilience() {
        if (mPullLeftX > 0 || mPullRightX < 0) {
            final float releaseDis = -(mPullLeftX + mPullRightX);
            mValueAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(mDuration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    if (mState == State.REFRESHING) {
                        scrollTo((int) (releaseDis + (Math.abs(releaseDis) - mReleaseDistance) * value), 0);
                    } else if (mState == State.LOADING) {
                        scrollTo((int) (releaseDis - (Math.abs(releaseDis) - mLoadMoreDistance) * value), 0);
                    } else {
                        scrollToIdle(value, releaseDis);
                    }
                }

                private void scrollToIdle(float value, float releaseDis) {
                    if (mPullLeftX > 0) {
                        scrollTo((int) (releaseDis + Math.abs(releaseDis) * value), 0);
                    } else {
                        scrollTo((int) (releaseDis - Math.abs(releaseDis) * value), 0);
                    }
                }
            });
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mState == State.REFRESHING) {
                        mPullLeftX = mReleaseDistance;
                    } else if (mState == State.LOADING) {
                        mPullRightX = -mLoadMoreDistance;
                    } else {
                        mPullRightX = 0;
                        mPullLeftX = 0;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mValueAnimator.start();
        }

    }

    private void moveUp(MotionEvent ev) {
        if (mPullRightX <= 0 && mPullLeftX == 0 && !isPullLeft) {
            mRadio = (float) (1 + Math.tan(Math.PI / 2 / getMeasuredWidth() * 2 * (1 + Math.abs(mPullRightX))));
            mPullRightX = mPullRightX + (ev.getX() - mLastX) / mRadio;
            if (mPullRightX < -getMeasuredWidth()) {
                mPullRightX = -getMeasuredWidth();
            }
            isPullRight = true;
        } else if (mPullRightX == 0 && mPullLeftX > 0) {
            mPullLeftX = mPullLeftX + (ev.getX() - mLastX);
        }

        if (mPullRightX > 0) {
            mPullRightX = 0;
        }

        if (mPullLeftX < 0) {
            mPullLeftX = 0;
        }

        scrollTo((int) -(mPullLeftX + mPullRightX), 0);
        mLastX = ev.getX();
    }

    private void moveDown(MotionEvent ev) {
        if (mPullLeftX >= 0 && mPullRightX == 0 && !isPullRight) {
            mRadio = (float) (1 + Math.tan(Math.PI / 2 / getMeasuredWidth() * (2 * mPullLeftX + 1)));
            mPullLeftX = mPullLeftX + (ev.getX() - mLastX) / mRadio;

            if (mPullLeftX > getMeasuredWidth()) {
                mPullLeftX = getMeasuredWidth();
            }
            isPullLeft = true;
        } else if (mPullLeftX == 0 && mPullRightX < 0) {
            mPullRightX = mPullRightX + ev.getX() - mLastX;
        }

        if (mPullRightX > 0) {
            mPullRightX = 0;
        }

        if (mPullLeftX < 0) {
            mPullLeftX = 0;
        }

        scrollTo((int) -(mPullLeftX + mPullRightX), 0);
        mLastX = ev.getX();
    }

    public void setReleaseDistance(float releaseDistance) {
        this.mReleaseDistance = DensityUtil.dp2px(getContext(), releaseDistance);
    }

    public void setLoadMoreDistance(float loadMoreDistance) {
        this.mLoadMoreDistance = DensityUtil.dp2px(getContext(), loadMoreDistance);
    }

    public void setPullToRefreshListener(OnPullToRefreshListener pullToRefreshListener) {
        this.mPullToRefreshListener = pullToRefreshListener;
    }

    /*****************NestedScrollingParent2**********************/

    @Override
    public boolean onStartNestedScroll(View view, View view1, int i, int i1) {
        return false;
    }

    @Override
    public void onNestedScrollAccepted(View view, View view1, int i, int i1) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(view, view1, i, i1);
    }

    @Override
    public void onStopNestedScroll(View view, int i) {
        mNestedScrollingParentHelper.onStopNestedScroll(view, i);

    }

    @Override
    public void onNestedScroll(View view, int i, int i1, int i2, int i3, int i4) {
    }

    @Override
    public void onNestedPreScroll(View view, int i, int i1, int[] ints, int i2) {

    }
}