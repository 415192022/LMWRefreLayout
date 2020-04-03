package com.lmw.widget.lmwrefreshlayout.lib.slide;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

public class SlideMenuHelper {

    public static final int INVALID_POSITION = -1;

    protected Callback mCallback;
    protected ViewConfiguration mViewConfig;
    protected SlideLayout mOldSwipedView;
    protected int mOldTouchedPosition = INVALID_POSITION;

    public SlideMenuHelper(Context context, Callback callback) {
        mCallback = callback;
        mViewConfig = ViewConfiguration.get(context);
    }

    /**
     * Handle touch down event, decide whether intercept or not.
     *
     * @param ev                 Touch event
     * @param defaultIntercepted The default intercept status
     * @return Whether intercept or not
     */
    public boolean handleListDownTouchEvent(MotionEvent ev, boolean defaultIntercepted) {
        boolean isIntercepted = defaultIntercepted;
        View touchingView = findChildViewUnder((int) ev.getX(), (int) ev.getY());
        int touchingPosition;
        if (touchingView != null) {
            touchingPosition = mCallback.getPositionForView(touchingView);
        } else {
            touchingPosition = INVALID_POSITION;
        }
        if (touchingPosition != mOldTouchedPosition && mOldSwipedView != null) {
            // already one swipe menu is opened, so we close it and intercept the event
            if (mOldSwipedView.isOpened()) {
                mOldSwipedView.closeMenu();
                isIntercepted = true;
            }
        }
        touchingView = mCallback.transformTouchingView(touchingPosition, touchingView);
        if (touchingView != null) {
            View itemView = getSlideMenuView((ViewGroup) touchingView);
            if (itemView != null && itemView instanceof SlideLayout) {
                mOldSwipedView = (SlideLayout) itemView;
                mOldTouchedPosition = touchingPosition;
            }
        }
        // if we intercept the event, just reset
        if (isIntercepted) {
            mOldSwipedView = null;
            mOldTouchedPosition = INVALID_POSITION;
        }
        return isIntercepted;
    }

    public void handleListDownTouchEvent(MotionEvent ev) {
        boolean isClosed = false;

        View touchingView = findChildViewUnder((int) ev.getX(), (int) ev.getY());
        int touchingPosition;
        if (touchingView != null) {
            touchingPosition = mCallback.getPositionForView(touchingView);
        } else {
            touchingPosition = INVALID_POSITION;
        }
        if (touchingPosition != mOldTouchedPosition && mOldSwipedView != null) {
            // already one swipe menu is opened, so we close it and intercept the event
            if (mOldSwipedView.isOpened()) {
                mOldSwipedView.closeMenu();
                isClosed = true;
            }
        }

        // if we intercept the event, just reset
        if (isClosed) {
            mOldSwipedView = null;
            mOldTouchedPosition = INVALID_POSITION;
        }
    }

    public void closeOldSwipedView(){
        if (mOldSwipedView!= null && mOldSwipedView.isOpened()) {
            mOldSwipedView.closeMenu();
            mOldSwipedView = null;
            mOldTouchedPosition = INVALID_POSITION;
        }
    }

    public boolean isIntercepted(MotionEvent ev, boolean defaultIntercepted) {
        boolean isIntercepted = defaultIntercepted;
        View touchingView = findChildViewUnder((int) ev.getX(), (int) ev.getY());
        int touchingPosition;
        if (touchingView != null) {
            touchingPosition = mCallback.getPositionForView(touchingView);
        } else {
            touchingPosition = INVALID_POSITION;
        }
        if (touchingPosition != mOldTouchedPosition && mOldSwipedView != null) {
            // already one swipe menu is opened, so we close it and intercept the event
            if (mOldSwipedView.isOpened()) {
                isIntercepted = true;
            }
        }
        if (!isIntercepted) {
            touchingView = mCallback.transformTouchingView(touchingPosition, touchingView);
            if (touchingView != null) {
                View itemView = getSlideMenuView((ViewGroup) touchingView);
                if (itemView != null && itemView instanceof SlideLayout) {
                    mOldSwipedView = (SlideLayout) itemView;
                    mOldTouchedPosition = touchingPosition;
                }
            }
        }

        return isIntercepted;
    }

    public boolean isIntercepted(MotionEvent ev) {
        boolean isIntercepted = false;
        View touchingView = findChildViewUnder((int) ev.getX(), (int) ev.getY());
        int touchingPosition;
        if (touchingView != null) {
            touchingPosition = mCallback.getPositionForView(touchingView);
        } else {
            touchingPosition = INVALID_POSITION;
        }
        if (touchingPosition != mOldTouchedPosition && mOldSwipedView != null) {
            // already one swipe menu is opened, so we close it and intercept the event
            if (mOldSwipedView.isOpened()) {
                isIntercepted = true;
            }
        }
        return isIntercepted;
    }

    public void markViewOpened(MotionEvent ev) {
        View touchingView = findChildViewUnder((int) ev.getX(), (int) ev.getY());
        int touchingPosition;
        if (touchingView != null) {
            touchingPosition = mCallback.getPositionForView(touchingView);
        } else {
            touchingPosition = INVALID_POSITION;
        }

        if (touchingView != null && touchingView instanceof SlideLayout && ((SlideLayout) touchingView).isOpened()) {
            touchingView = mCallback.transformTouchingView(touchingPosition, touchingView);
            if (touchingView != null) {
                View itemView = getSlideMenuView((ViewGroup) touchingView);
                if (itemView != null && itemView instanceof SlideLayout) {
                    mOldSwipedView = (SlideLayout) itemView;
                    mOldTouchedPosition = touchingPosition;
                }
            }
        }
    }

    public boolean isOldSwipedView(MotionEvent ev) {
        View touchingView = findChildViewUnder((int) ev.getX(), (int) ev.getY());
        int touchingPosition;
        if (touchingView != null) {
            touchingPosition = mCallback.getPositionForView(touchingView);
        } else {
            touchingPosition = INVALID_POSITION;
        }
        if (touchingPosition == mOldTouchedPosition) {
            return true;
        }

        return false;
    }

    public View getSlideMenuView(ViewGroup itemView) {
        if (itemView instanceof SlideLayout) {
            return itemView;
        }
        List<View> unvisited = new ArrayList<>();
        unvisited.add(itemView);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            if (!(child instanceof ViewGroup)) { // view
                continue;
            }
            if (child instanceof SlideLayout) {
                return child;
            }
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                unvisited.add(group.getChildAt(i));
            }
        }
        return itemView;
    }

    public void closeAll() {
        final int count = mCallback.getRealChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = mCallback.getRealChildAt(i);
            if (child instanceof SlideLayout) {
                ((SlideLayout) child).closeMenu();
            }
        }
        mOldSwipedView = null;
        mOldTouchedPosition = INVALID_POSITION;
    }

    /**
     * Find the topmost view under the given point.
     *
     * @param x Horizontal position in pixels to search
     * @param y Vertical position in pixels to search
     * @return The child view under (x, y) or null if no matching child is found
     */
    public View findChildViewUnder(float x, float y) {
        final int count = mCallback.getRealChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = mCallback.getRealChildAt(i);
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);
            if (x >= child.getLeft() + translationX &&
                    x <= child.getRight() + translationX &&
                    y >= child.getTop() + translationY &&
                    y <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }

    public interface Callback {
        int getPositionForView(View view);

        int getRealChildCount();

        View getRealChildAt(int index);

        View transformTouchingView(int touchingPosition, View touchingView);
    }

}