package com.lmw.widget.lmwrefrelayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.lmw.widget.lmwrefreshlayout.lib.HorizonRefreshLayout;

public class LMWHorizonRefreshLayout extends HorizonRefreshLayout {

    public LMWHorizonRefreshLayout(Context context) {
        this(context, null);
    }

    public LMWHorizonRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LMWHorizonRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setReleaseDistance(50);
        setLoadMoreDistance(50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected View getHeaderView() {
        return new LMWHorizonHeaderView(getContext());
    }

    @Override
    protected View getFooterView() {
        return new LMWHorizonFooterView(getContext());
    }

}
