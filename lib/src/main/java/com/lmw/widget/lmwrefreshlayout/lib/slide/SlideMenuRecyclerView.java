package com.lmw.widget.lmwrefreshlayout.lib.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SlideMenuRecyclerView extends RecyclerView implements SlideMenuHelper.Callback {

    private static final int INVALID_POINTER = -1;
    protected SlideMenuHelper mHelper;


    public SlideMenuRecyclerView(Context context) {
        this(context, null);
    }

    public SlideMenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setMotionEventSplittingEnabled(false);
        init();
    }

    private void init() {
        mHelper = new SlideMenuHelper(getContext(), this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //假如有开着的Item，直接关闭。加入Layout是打开的状态，则根据点击的点是否落在删除位置上，来判断是否拦截事件
//                for (int i = 0; i < getChildCount(); i++) {
//                    View childView = getChildAt(i);
//                    if (childView instanceof ISlideLayout) {
//                        if (((ISlideLayout) childView).isOpened()) {
//                            //关闭菜单
//                            ((ISlideLayout) childView).closeMenu();
//                        }
//                    }
//                }
        boolean isIntercepted = super.onInterceptTouchEvent(ev);

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isIntercepted = mHelper.handleListDownTouchEvent(ev, isIntercepted);
                break;
        }
        return isIntercepted;
    }

    public int getPositionForView(View touchView) {
        return getChildAdapterPosition(touchView);
    }

    @Override
    public int getRealChildCount() {
        return getChildCount();
    }

    @Override
    public View getRealChildAt(int index) {
        return getChildAt(index);
    }

    @Override
    public View transformTouchingView(int touchingPosition, View touchingView) {
        ViewHolder vh = findViewHolderForAdapterPosition(touchingPosition);
        if (vh != null) {
            return vh.itemView;
        }
        return touchingView;
    }
}