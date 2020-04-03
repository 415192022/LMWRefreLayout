package com.lmw.widget.lmwrefreshlayout.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;
import com.lmw.widget.lmwrefreshlayout.lib.view.RefreshView;


public class DefaultHeaderView extends RelativeLayout implements RefreshView {
    public DefaultHeaderView(Context context) {
        this(context, null);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onStateChanged(State state) {

    }

}
