package com.lmw.widget.lmwrefrelayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.lmw.widget.lmwrefreshlayout.lib.VerticalRefreshLayout;

public class LMWRefreshLayout extends VerticalRefreshLayout {

    public LMWRefreshLayout(Context context) {
        this(context, null);
    }

    public LMWRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LMWRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setReleaseDistance(50);
        setLoadMoreDistance(50);
        setPreViewDistance(90);
//        setNextViewDistance(70);
        setHeightRadio(6);
        setSupportNestedScroll(true);
        setDuration(1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected View getHeaderView() {
        return new LMWHeaderView(getContext());
    }

    @Override
    protected View getFooterView() {
        return new LMWFooterView(getContext());
    }

}
