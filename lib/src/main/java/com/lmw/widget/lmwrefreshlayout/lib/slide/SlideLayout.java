package com.lmw.widget.lmwrefreshlayout.lib.slide;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;

public class SlideLayout extends HorizontalScrollView implements ISlideAction {

    private int minSlide;

    private Boolean isOpened = false;
    private Boolean once = false;

    private float mInitX;
    private float mInitY;
    private float mLastX;
    private float mLastY;

    private Handler mHandler = new Handler();

    private float mDistance = 0;

    private int maxScrollX = 0;


    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init(context);

    }

    private void initAttr(Context context, AttributeSet attrs) {

    }

    private void init(Context context) {
        minSlide = ViewConfiguration.get(context).getScaledTouchSlop();
        setOverScrollMode(OVER_SCROLL_NEVER);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitX = ev.getX();
                mInitY = ev.getY();
                mLastX = ev.getX();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //方法二  MOVE事件，仅供参考：
//        mLastX = ev.getX();
//        mLastY = ev.getY();
//        //判断事件改变方向，则要改变落点初值
//        L.e("-----------------------" + isOpened);
//        L.e("-----------------------" + (mLastX - mInitX));
//        if ((!isOpened && mLastX - mInitX > 0) || (isOpened && mInitX - mLastX > 0)) {
//          L.e("-----------------------");
//          mInitX = ev.getX();
//          mInitY = ev.getY();
//        }

                break;
            case MotionEvent.ACTION_UP:
                //方法二 UP事件，仅供参考：
//        mLastX = ev.getX();
//        mLastY = ev.getY();
//
//        if ((Math.abs(mLastX - mInitX) < minSlide && Math.abs(mLastY - mInitY) < minSlide)) {
//
//          break;
//        }
//
//        if (!isOpened && mLastX - mInitX > 0 || isOpened && mInitX - mLastX > 0) {
//          break;
//        }
//
//        if ((!isOpened && mInitX - mLastX > mFunctionWidth / 2) || (isOpened && mLastX - mInitX > 0 && mLastX -
//            mInitX < mFunctionWidth / 2)) {
//          openMenu();
//        } else if (isOpened && mLastX - mInitX > mFunctionWidth / 2 || (!isOpened && mInitX - mLastX < mFunctionWidth
//            / 2 && mInitX - mLastX > 0)) {
//          closeMenu();
//        }
//
//        mDirection = Direction.NONE;


                if ((mDistance < 0 && mDistance <= -(maxScrollX / 2)) || (mDistance > 0 && mDistance <= (maxScrollX /
                        2))) {
                    openMenu();
                } else if (mDistance != 0) {
                    closeMenu();
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                if ((mDistance < 0 && mDistance <= -(maxScrollX / 2)) || (mDistance > 0 && mDistance <= (maxScrollX /
                        2))) {
                    openMenu();
                } else if (mDistance != 0) {
                    closeMenu();
                }
                break;
            default:
                break;

        }

        return super.onTouchEvent(ev);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        maxScrollX = getChildAt(0).getMeasuredWidth() - getMeasuredWidth();

        if (getScrollX() == 0) {
            isOpened = false;
            mDistance = 0;
        } else if (getScrollX() == maxScrollX) {
            isOpened = true;
            mDistance = 0;
        } else {
            mDistance = mDistance + (oldl - l);
        }

    }

    /**
     * 打开菜单
     */
    @Override
    public void openMenu() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                SlideLayout.this.smoothScrollTo(maxScrollX, 0);
            }
        });
    }

    /**
     * 关闭菜单
     */
    @Override
    public void closeMenu() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                SlideLayout.this.smoothScrollTo(0, 0);
            }
        });
    }

    @Override
    public Boolean isOpened() {
        return isOpened;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }


}