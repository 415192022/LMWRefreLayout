package com.lmw.widget.lmwrefrelayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.lmw.widget.lmwrefreshlayout.lib.VerticalRefreshLayout;

public class AnimationRefreshLayout extends VerticalRefreshLayout {

    public AnimationRefreshLayout(Context context) {
        this(context, null);
    }

    public AnimationRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setReleaseDistance(70);
        setLoadMoreDistance(50);
//        setPreViewDistance(90);
//        setNextViewDistance(70);
        setHeightRadio(4);
        setSupportNestedScroll(true);
        setDuration(200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected View getHeaderView() {
        return new AnimationHeader(getContext());
    }

    @Override
    protected View getFooterView() {
        return new AnimationFooter(getContext());
    }

}
