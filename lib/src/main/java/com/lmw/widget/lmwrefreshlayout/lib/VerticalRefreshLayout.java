package com.lmw.widget.lmwrefreshlayout.lib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lmw.widget.lmwrefreshlayout.lib.listener.OnPullToRefreshListener;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;
import com.lmw.widget.lmwrefreshlayout.lib.utils.DensityUtil;
import com.lmw.widget.lmwrefreshlayout.lib.view.RefreshView;

public abstract class VerticalRefreshLayout extends BaseRefreshLayout {
    public static final String TAG = "VerticalRefreshLayout";
    private static final int INVALID_POINTER = -1;
    private final int[] mParentOffsetInWindow = new int[2];
    private final int[] mParentScrollConsumed = new int[2];
    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float mPullDownY = 0;
    private int mActivePointerId = INVALID_POINTER;
    // 上拉的距离
    private float mPullUpY = 0;
    // 按下Y坐标，上一个事件点Y坐标
    private float mDownY, mLastY, mInterceptDownY, mInterceptLastY, mInterceptDownX, mInterceptLastX;
    // 当前状态
    private State mState = State.IDLE;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float mRadio = 5;
    private int minRadio = 5;
    // 释放刷新的距离
    private float mReleaseDistance = 200;
    // 释放加载的距离
    private float mLoadMoreDistance = 50;
    // 下拉进入-1楼的距离
    private float mPreViewDistance = -1;
    // 上拉进入下一楼的距离
    private float mNextViewDistance = -1;
    private int mHeightRadio = 2;
    private ValueAnimator mValueAnimator;
    private OnPullToRefreshListener mPullToRefreshListener;
    private boolean mIsBeingDragged;
    private int mTouchSlop;
    private boolean isLayout;
    private boolean isPullDown;
    private boolean isPullUp;
    private boolean mNestedScrollInProgress;
    private float mReleaseDis;
    private boolean mAnimating;
    private int mHeaderHeight;
    private int mFooterHeight;
    private boolean mProcessing;

    public VerticalRefreshLayout(Context context) {
        this(context, null);
    }

    public VerticalRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mValueAnimator = ValueAnimator.ofFloat(0, 1f);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (mState == State.PRE_FLOOR) {
                    float distance = (mReleaseDis - (mReleaseDis + mHeaderHeight) * value);
                    mPullDownY = -distance;
                    requestLayout();
                } else if (mState == State.NEXT_FLOOR) {
                    float distance = (mReleaseDis + (mHeaderHeight - mReleaseDis) * value);
                    mPullUpY = -distance;
                    requestLayout();
                } else if (mState == State.REFRESHING) {
                    float distance = (mReleaseDis + (Math.abs(mReleaseDis) - mReleaseDistance) * value);
                    mPullDownY = -distance;
                    requestLayout();
                } else if (mState == State.LOADING) {
                    float distance = (mReleaseDis - (Math.abs(mReleaseDis) - mLoadMoreDistance) * value);
                    mPullUpY = -distance;
                    requestLayout();
                } else {
                    scrollToIdle(value, mReleaseDis);
                }
            }

            private void scrollToIdle(float value, float releaseDis) {
                if (mPullDownY > 0) {
                    float distance = (releaseDis + Math.abs(releaseDis) * value);
                    mPullDownY = -distance;
                    requestLayout();
                } else {
                    float distance = (releaseDis - Math.abs(releaseDis) * value);
                    mPullUpY = -distance;
                    requestLayout();
                }
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;

                if (mState == State.PRE_FLOOR && mHeaderView != null) {
                    ((RefreshView) mHeaderView).onStateChanged(mState);
                } else if (mState == State.NEXT_FLOOR && mFooterView != null) {
                    ((RefreshView) mFooterView).onStateChanged(mState);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
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
            mHeaderHeight = headerHeight;

            final View topChild = mHeaderView;
            final int topChildLeft = topChild.getPaddingLeft();
            final int topChildTop = (int) (topChild.getPaddingTop() + mPullDownY + mPullUpY - headerHeight);
            final int topChildRight = headerWidth - topChild.getPaddingLeft() - topChild.getPaddingRight();
            final int topChildBottom = topChildTop + headerHeight;

            topChild.layout(topChildLeft,
                    topChildTop,
                    topChildRight,
                    topChildBottom);
        }


        final View child = mTarget;
        final int childLeft = child.getPaddingLeft();
        final int childTop = (int) (mPullDownY + mPullUpY + child.getPaddingTop());
        final int childRight = childLeft + child.getMeasuredWidth();
        final int childBottom = childTop + child.getMeasuredHeight();

        child.layout(childLeft,
                childTop,
                childRight,
                childBottom);

        if (mFooterView != null) {
            int footerWidth = mFooterView.getMeasuredWidth();
            int footerHeight = mFooterView.getMeasuredHeight();
            mFooterHeight = footerHeight;

            final View bottomChild = mFooterView;
            final int bottomChildLeft = bottomChild.getPaddingLeft();
            final int bottomChildTop = childBottom + bottomChild.getPaddingTop();
            final int bottomChildRight = bottomChildLeft + footerWidth;
            final int bottomChildBottom = bottomChildTop + footerHeight;
            mFooterView.layout(bottomChildLeft,
                    bottomChildTop,
                    bottomChildRight,
                    bottomChildBottom);
        }

    }

    private void changeState(State state) {
        if (mState == state) {
            return;
        }
        mState = state;

        switch (state) {
            case IDLE:
                if (mHeaderView != null) {
                    ((RefreshView) mHeaderView).onStateChanged(state);
                }
                if (mFooterView != null) {
                    ((RefreshView) mFooterView).onStateChanged(state);
                }
                break;
            case RELEASE_TO_REFRESH:
                if (mHeaderView != null) {
                    ((RefreshView) mHeaderView).onStateChanged(state);
                }
                break;
            case REFRESHING:
                if (mHeaderView != null) {
                    ((RefreshView) mHeaderView).onStateChanged(state);
                }
                // 刷新操作
                if (mPullToRefreshListener != null && !mProcessing) {
                    mProcessing = true;
                    mPullToRefreshListener.onRefresh();
                }
                break;
            case RELEASE_TO_LOAD:
                if (mFooterView != null) {
                    ((RefreshView) mFooterView).onStateChanged(state);
                }
                break;
            case LOADING:
                // 正在加载状态
                if (mFooterView != null) {
                    ((RefreshView) mFooterView).onStateChanged(state);
                }
                // 加载操作
                if (mPullToRefreshListener != null && !mProcessing) {
                    mProcessing = true;
                    mPullToRefreshListener.onLoadMore();
                }
                break;
            case PREPARE_TO_REFRESH:
                if (mHeaderView != null) {
                    ((RefreshView) mHeaderView).onStateChanged(state);
                }
                break;
            case PREPARE_TO_LOAD:
                if (mFooterView != null) {
                    ((RefreshView) mFooterView).onStateChanged(state);
                }
                break;
            case RELEASE_PRE_FLOOR:
                if (mHeaderView != null) {
                    ((RefreshView) mHeaderView).onStateChanged(state);
                }
                break;
            case RELEASE_NEXT_FLOOR:
                if (mFooterView != null) {
                    ((RefreshView) mFooterView).onStateChanged(state);
                }
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = ev.getActionMasked();
        int pointerIndex;

        if (!isEnabled() || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                isPullDown = false;
                isPullUp = false;
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                mDownY = ev.getY(pointerIndex);
                mLastY = mDownY;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                isPullDown = false;
                isPullUp = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    private void startDragging(float y) {
        final float yDiff = y - mDownY;

        if (mPullDownY != 0 || mPullUpY != 0) {
            mIsBeingDragged = true;
        } else if (yDiff > mTouchSlop && !canChildScrollUp() && !mIsBeingDragged) {
            mIsBeingDragged = true;
        } else if (Math.abs(yDiff) > mTouchSlop && yDiff < 0 && !canChildScrollDown() && !mIsBeingDragged) {
            mIsBeingDragged = true;
        }
    }

    private boolean canChildScrollUp() {
        if (mTarget instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mTarget, -1);
        }


        return mTarget.canScrollVertically(-1);
    }

    private boolean canChildScrollDown() {
        if (mTarget instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mTarget, 1);
        } else if (mTarget instanceof RecyclerView) {
            if (((RecyclerView) mTarget).getChildCount() == 0) {
                return true;
            } else if (((RecyclerView) mTarget).getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) ((RecyclerView) mTarget).getLayoutManager();
                return canRecycleViewScrollDown(layoutManager);
            }
        }

        return mTarget.canScrollVertically(1);
    }

    private boolean canRecycleViewScrollDown(LinearLayoutManager layoutManager) {
        return !(layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 1
                && ((RecyclerView) mTarget).getChildAt(layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition()) != null &&
                ((RecyclerView) mTarget).getChildAt(layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition()).getBottom() <= mTarget.getMeasuredHeight());
    }

    /**
     * 完成刷新或者加载
     */
    public void onFinish() {
        mProcessing = false;
        if (mState == State.REFRESHING || mState == State.LOADING) {
            changeState(State.IDLE);
        }
        if (mActivePointerId == INVALID_POINTER && !mAnimating) {
            resilience();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();

        if (!isEnabled() || mNestedScrollInProgress) {
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

                cancelAnimation();

                float diffY = ev.getY() - mLastY;
                moveSpinner(diffY);
                break;
            case MotionEvent.ACTION_UP:
                handleEventUp();
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                return false;
            default:
                break;
        }
        return true;
    }

    public void handleEventUp() {
        switch (mState) {
            case IDLE:

                break;
            case RELEASE_TO_REFRESH:
                changeState(State.REFRESHING);
                break;
            case REFRESHING:

                break;
            case RELEASE_TO_LOAD:
                changeState(State.LOADING);
                break;
            case LOADING:

                break;
            case PREPARE_TO_REFRESH:
                changeState(State.IDLE);
                break;
            case PREPARE_TO_LOAD:
                changeState(State.IDLE);
                break;
            case RELEASE_PRE_FLOOR:
                changeState(State.PRE_FLOOR);
                break;
            case RELEASE_NEXT_FLOOR:
                changeState(State.NEXT_FLOOR);
                break;
            case PRE_FLOOR:

                break;
            case NEXT_FLOOR:

                break;
            default:
                changeState(State.IDLE);
                break;
        }

        resilience();
    }

    private void moveSpinner(float diffY) {
        if (diffY > 0) {
            moveDown(diffY);
        } else if (diffY < 0) {
            moveUp(diffY);
        }

        mLastY = mLastY + diffY;

        if (mState != State.REFRESHING && mState != State.LOADING) {
            if (mPullDownY > 0) {
                if (mPullDownY <= mReleaseDistance) {//下拉刷新
                    changeState(State.PREPARE_TO_REFRESH);
                } else if ((mPreViewDistance > 0 && mPullDownY > mReleaseDistance && mPullDownY <= mPreViewDistance)
                        || (mPreViewDistance < 0) && mPullDownY > mReleaseDistance) {//释放刷新
                    changeState(State.RELEASE_TO_REFRESH);
                } else if (mPreViewDistance > 0 && mPullDownY > mPreViewDistance) {
                    changeState(State.RELEASE_PRE_FLOOR);
                }
            } else if (mPullUpY < 0) {
                if (Math.abs(mPullUpY) <= mLoadMoreDistance) {//上拉加载
                    changeState(State.PREPARE_TO_LOAD);
                } else if ((mNextViewDistance > 0 && Math.abs(mPullUpY) > mLoadMoreDistance && Math.abs(mPullUpY) <= mNextViewDistance)
                        || (mNextViewDistance < 0 && Math.abs(mPullUpY) > mLoadMoreDistance)) {//自动释放加载
                    changeState(State.RELEASE_TO_LOAD);
                    mActivePointerId = INVALID_POINTER;
                    handleEventUp();
                } else if (mNextViewDistance > 0 && Math.abs(mPullUpY) > mNextViewDistance) {
                    changeState(State.RELEASE_NEXT_FLOOR);
                }
            }

            if (mPullDownY != 0 || mPullUpY != 0) {
                if (mScrollListener != null) {
                    mScrollListener.onScrolled(0, mPullDownY + mPullUpY);
                }
            }
        }
    }

    private void resilience() {
        if (mPullDownY != 0 || mPullUpY != 0) {
            mReleaseDis = -(mPullDownY + mPullUpY);
            mValueAnimator.start();
        }

    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    private void moveUp(float diffY) {
        if (mCanPullUp && mPullUpY <= 0 && mPullDownY == 0 && !isPullDown && mState != State.REFRESHING) {
            mRadio = (float) (minRadio + Math.tan(Math.PI / 2 / getMeasuredHeight() * mHeightRadio * (1 + Math.abs(mPullUpY))));
            mPullUpY = mPullUpY + diffY / mRadio;
            if (mPullUpY < -(getMeasuredHeight() / mHeightRadio - 1)) {
                mPullUpY = -(getMeasuredHeight() / mHeightRadio - 1);
            }
            isPullUp = true;
        } else if (mPullUpY == 0 && mPullDownY > 0) {
            mPullDownY = mPullDownY + diffY;
        }

        if (mPullUpY > 0) {
            mPullUpY = 0;
        }

        if (mPullDownY < 0) {
            mPullDownY = 0;
        }

        requestLayout();
    }

    private void moveDown(float diffY) {
        if (mCanPullDown && mPullDownY >= 0 && mPullUpY == 0 && !isPullUp && mState != State.LOADING) {
            mRadio = (float) (minRadio + Math.tan(Math.PI / 2 / getMeasuredHeight() * mHeightRadio * (mPullDownY + 1)));
            mPullDownY = mPullDownY + diffY / mRadio;

            if (mPullDownY > getMeasuredHeight() / mHeightRadio - 1) {
                mPullDownY = getMeasuredHeight() / mHeightRadio - 1;
            }
            isPullDown = true;
        } else if (mPullDownY == 0 && mPullUpY < 0) {
            mPullUpY = mPullUpY + diffY;
        }

        if (mPullUpY > 0) {
            mPullUpY = 0;
        }

        if (mPullDownY < 0) {
            mPullDownY = 0;
        }

        requestLayout();
    }

    public void setPreViewDistance(float distance) {
        this.mPreViewDistance = DensityUtil.dp2px(getContext(), distance);
    }

    public void setNextViewDistance(float distance) {
        this.mNextViewDistance = DensityUtil.dp2px(getContext(), distance);
    }

    public void setReleaseDistance(float releaseDistance) {
        this.mReleaseDistance = DensityUtil.dp2px(getContext(), releaseDistance);
    }

    public void setLoadMoreDistance(float loadMoreDistance) {
        this.mLoadMoreDistance = DensityUtil.dp2px(getContext(), loadMoreDistance);
    }

    public void setHeightRadio(int heightRadio) {
        this.mHeightRadio = heightRadio;
    }

    public void setPullToRefreshListener(OnPullToRefreshListener pullToRefreshListener) {
        this.mPullToRefreshListener = pullToRefreshListener;
    }

    /*****************NestedScrollingParent2**********************/

    @Override
    public boolean onStartNestedScroll(View view, View view1, int nestedScrollAxes, int type) {
        return mSupportNestedScroll && isEnabled()
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View view, View view1, int nestedScrollAxes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(view, view1, nestedScrollAxes, type);
        // Dispatch up to the nested parent
        startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mNestedScrollInProgress = true;
    }

    @Override
    public void onStopNestedScroll(View view, int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(view, type);
        mNestedScrollInProgress = false;

        mActivePointerId = INVALID_POINTER;

        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        handleEventUp();
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];

        if (type == ViewCompat.TYPE_TOUCH) {
            cancelAnimation();
            moveSpinner(-dy);
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll

        float diffY;

        if (mPullDownY > 0 && dy > 0) {
            if (dy > mPullDownY) {
                diffY = mPullDownY;
            } else {
                diffY = dy;
            }
            consumed[1] = (int) diffY;
            cancelAnimation();
            moveSpinner(-diffY);
        } else if (mPullUpY < 0 && dy < 0) {
            if (dy < mPullUpY) {
                diffY = mPullDownY;
            } else {
                diffY = dy;
            }
            consumed[1] = (int) diffY;
            cancelAnimation();
            moveSpinner(-diffY);
        }

        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    private void cancelAnimation() {
        if (mAnimating) {
            mValueAnimator.cancel();
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public void setSupportNestedScroll(boolean bool) {
        mSupportNestedScroll = bool;
    }

    public void setDuration(int duration) {
        mValueAnimator.setDuration(duration);
    }

}