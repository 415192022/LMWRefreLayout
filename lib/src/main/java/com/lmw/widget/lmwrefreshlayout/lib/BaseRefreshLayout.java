package com.lmw.widget.lmwrefreshlayout.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;

import com.lmw.widget.lmwrefreshlayout.lib.listener.IScrollListener;

/**
 * <p>
 * 基础布局只负责测量
 */
public abstract class BaseRefreshLayout extends ViewGroup implements NestedScrollingParent2, NestedScrollingChild2 {

    public static final String TAG = "BaseRefreshLayout";

    public static final boolean DEBUG = true;
    protected final NestedScrollingChildHelper mNestedScrollingChildHelper;
    protected final NestedScrollingParentHelper mNestedScrollingParentHelper;
    protected boolean mCanPullDown;
    protected boolean mCanPullUp;
    protected boolean mUserCustomerHeader;
    protected View mHeaderView;
    protected View mFooterView;
    protected View mTarget; // the target of the gesture
    protected boolean mSupportNestedScroll = true;
    protected IScrollListener mScrollListener;


    public BaseRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        initAttribute(context, attrs);
        initListener();
        createHeader();
        createFooter();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void initListener() {

    }

    private void createFooter() {
        if (mCanPullUp) {
            mFooterView = getFooterView();
            addView(mFooterView);
        }
    }

    private void createHeader() {
        if (mUserCustomerHeader && mCanPullDown) {
            mHeaderView = getHeaderView();
            addView(mHeaderView);
        }
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VerticalRefreshLayout);

        mCanPullDown = ta.getBoolean(R.styleable.VerticalRefreshLayout_can_pull_down, false);
        mCanPullUp = ta.getBoolean(R.styleable.VerticalRefreshLayout_can_pull_up, false);
        mUserCustomerHeader = ta.getBoolean(R.styleable.VerticalRefreshLayout_customer_header, false);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        measureChild(mTarget, widthMeasureSpec, heightMeasureSpec);

        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            View view = mHeaderView.findViewWithTag(getContext().getString(R.string.headView));
        }
        if (mFooterView != null) {
            measureChild(mFooterView, widthMeasureSpec, heightMeasureSpec);
            View view = mFooterView.findViewWithTag(getContext().getString(R.string.footView));
        }
        //控件大小以mTarget为准
        setMeasuredDimension(resolveSize(mTarget.getMeasuredWidth(), widthMeasureSpec), resolveSize(mTarget.getMeasuredHeight(), heightMeasureSpec));
    }

    public void ensureTarget() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mHeaderView) && !child.equals(mFooterView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    public void setCanPullDown(boolean canPullDown) {
        this.mCanPullDown = canPullDown;
    }

    public void setCanPullUp(boolean canPullUp) {
        this.mCanPullUp = canPullUp;
    }

    public void setScrollListener(IScrollListener listener) {
        this.mScrollListener = listener;
    }

    protected abstract View getHeaderView();

    protected abstract View getFooterView();

    /*****************NestedScrollingChild2**********************/

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int i, int i1) {
        return mNestedScrollingChildHelper.startNestedScroll(i, i1);
    }

    @Override
    public void stopNestedScroll(int i) {
        mNestedScrollingChildHelper.stopNestedScroll(i);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent(int i) {
        return mNestedScrollingChildHelper.hasNestedScrollingParent(i);
    }

    @Override
    public boolean dispatchNestedScroll(int i, int i1, int i2, int i3, @Nullable int[] ints, int i4) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(i, i1, i2, i3, ints, i4);
    }

    @Override
    public boolean dispatchNestedPreScroll(int i, int i1, @Nullable int[] ints, @Nullable int[] ints1, int i2) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i1, ints, ints1, i2);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}
