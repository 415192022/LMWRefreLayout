package com.lmw.widget.lmwrefrelayout.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.lmw.widget.lmwrefrelayout.R;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;
import com.lmw.widget.lmwrefreshlayout.lib.widget.DefaultHeaderView;

public class AnimationHeader extends DefaultHeaderView {

    private View ivItem;
    private View mHeader;
    private AnimationDrawable animationDrawable;

    public AnimationHeader(Context context) {
        this(context, null);
    }

    public AnimationHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        mHeader = inflater.inflate(R.layout.animation_refresh_head, this, false);
        ivItem = mHeader.findViewById(R.id.ivItem);
        animationDrawable = (AnimationDrawable) ivItem.getBackground();
        addView(mHeader);
    }

    @Override
    public void onStateChanged(State state) {

        if (state == State.PREPARE_TO_REFRESH) {
            animationDrawable.start();
        } else if (state == State.IDLE) {
            animationDrawable.stop();
        } else if (state == State.RELEASE_TO_REFRESH) {
            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else if (state == State.REFRESHING) {
        } else if (state == State.RELEASE_PRE_FLOOR) {
        }
    }

}
