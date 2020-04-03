package com.lmw.widget.lmwrefrelayout.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.lmw.widget.lmwrefrelayout.R;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;
import com.lmw.widget.lmwrefreshlayout.lib.widget.DefaultFooterView;

public class AnimationFooter extends DefaultFooterView {
    private View ivItem;
    private View mFooter;
    private AnimationDrawable animationDrawable;

    public AnimationFooter(Context context) {
        this(context, null);
    }

    public AnimationFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        mFooter = inflater.inflate(R.layout.animation_load_more, this, false);
        ivItem = mFooter.findViewById(R.id.ivItem);
        animationDrawable = (AnimationDrawable) ivItem.getBackground();
        addView(mFooter);
    }

    @Override
    public void onStateChanged(State state) {
        if (state == State.PREPARE_TO_LOAD){
            animationDrawable.start();
        }else if(state == State.IDLE){
            animationDrawable.stop();
        }else if(state == State.RELEASE_TO_REFRESH){
            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        }else if(state == State.REFRESHING){
        }else if(state == State.RELEASE_PRE_FLOOR){
        }
    }


}
